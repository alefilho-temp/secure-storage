package com.safe.storage.views;

import com.safe.storage.controllers.GenericItemController;
import com.safe.storage.models.GenericItem;
import com.safe.storage.models.SafeFile;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GenericItemView extends SplitPane {
    private Label lblId = new Label("");
    private TextField txtName = new TextField();
    private TextField txtValue = new TextField();
    private TextField txtDescription = new TextField();
    private TextField txtPrice = new TextField();
    private Label lblCreatedDate = new Label("");
    private TableView<GenericItem> tableView = new TableView<>();
    private GenericItemController controller;
    private File selectedFile;

    public GenericItemView() {
        controller = new GenericItemController();
        GridPane formPane = createFormPane();

        setDividerPositions(0.5);
        getItems().addAll(tableView, formPane);
        generateColumns();
        loadItems();
    }

    private GridPane createFormPane() {
        GridPane formPane = new GridPane();
        formPane.setStyle("-fx-padding: 10; -fx-background-color: #f9f9f9; -fx-border-color: #cccccc; -fx-border-width: 1;");
        formPane.setAlignment(Pos.CENTER);
        addFormFields(formPane);
        addButtons(formPane);
        return formPane;
    }

    private void addFormFields(GridPane formPane) {
        formPane.add(new Label("ID: "), 0, 0);
        formPane.add(lblId, 1, 0);

        formPane.add(new Label("Nome: "), 0, 1);
        formPane.add(txtName, 1, 1);

        formPane.add(new Label("Valor: "), 0, 2);
        formPane.add(txtValue, 1, 2);

        formPane.add(new Label("Descrição: "), 0, 3);
        formPane.add(txtDescription, 1, 3);

        formPane.add(new Label("Preço: "), 0, 4);
        formPane.add(txtPrice, 1, 4);

        formPane.add(new Label("Data de Criação: "), 0, 5);
        formPane.add(lblCreatedDate, 1, 5);
    }

    private void addButtons(GridPane formPane) {
        Button btnSave = createSaveButton();
        Button btnSearch = createSearchButton();
        Button btnClear = createClearButton();
        Button btnChooseFile = createChooseFileButton();

        formPane.add(btnSave, 0, 6);
        formPane.add(btnSearch, 1, 6);
        formPane.add(btnClear, 2, 6);
        formPane.add(btnChooseFile, 2, 5); // Add file chooser button
    }

    private Button createSaveButton() {
        Button btnSave = new Button("Salvar");
        btnSave.setOnAction(e -> {
            try {
                // Data verification
                if (!isInputValid()) {
                    showAlert("Erro", "Por favor, preencha todos os campos corretamente.");
                    return;
                }

                GenericItem item = new GenericItem();
                if (!lblId.getText().isEmpty()) {
                    item.setId(Integer.parseInt(lblId.getText()));
                }
                item.setName(txtName.getText());
                item.setValue(Float.parseFloat(txtValue.getText()));
                item.setDescription(txtDescription.getText());
                item.setPrice(Double.parseDouble(txtPrice.getText()));
                item.setCreatedDate(new Date()); // Set current date

                // Read the file data if a file was chosen
                if (selectedFile != null) {
                    byte[] fileData = Files.readAllBytes(selectedFile.toPath());
                    if (fileData.length == 0) {
                        showAlert("Erro ", "O arquivo selecionado está vazio.");
                        return;
                    }
                    item.setData(fileData);
                    // Set name from file if name field is empty
                    if (txtName.getText().isEmpty()) {
                        item.setName(selectedFile.getName());
                    }
                }

                controller.save(item);
                loadItems();
                clearFields();
            } catch (Exception err) {
                showAlert("Erro", "Erro ao salvar item: " + err.getMessage());
            }
        });
        return btnSave;
    }

    private boolean isInputValid() {
        try {
            Float.parseFloat(txtValue.getText());
            Double.parseDouble(txtPrice.getText());
            return !txtName.getText().isEmpty() && !txtDescription.getText().isEmpty();
        } catch (NumberFormatException e) {
            return false; // Invalid number format
        }
    }

    private Button createSearchButton() {
        Button btnSearch = new Button("Buscar");
        btnSearch.setOnAction(e -> {
            String searchName = txtName.getText();
            ObservableList<GenericItem> result = controller.searchBy("name", searchName);
            
            if (searchName.trim().length() == 0) {
                System.out.println("searchName: " + searchName);
                clearFields();
                // loadSafeFiles();
                controller.loadAllItems();
            } else {
                if (!result.isEmpty()) {
                    populateFields(result.get(0));
                }
            }
        });
        return btnSearch;
    }

    private Button createClearButton() {
        Button btnClear = new Button("Limpar");
        btnClear.setOnAction(e -> clearFields());
        return btnClear;
    }

    private Button createChooseFileButton() {
        Button btnChooseFile = new Button("Escolher Arquivo");
        btnChooseFile.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            selectedFile = fileChooser.showOpenDialog(new Stage());
            if (selectedFile != null) {
                showAlert("Sucesso", "Arquivo selecionado: " + selectedFile.getName());
                if (txtName.getText().isEmpty()) {
                    txtName.setText(selectedFile.getName());
                }
            }
        });
        return btnChooseFile;
    }

    private void generateColumns() {
        TableColumn<GenericItem, String> colName = createStringColumn("Nome", "name");
        TableColumn<GenericItem, Float> colValue = createFloatColumn("Valor", "value");
        TableColumn<GenericItem, String> colDescription = createStringColumn("Descrição", "description");
        TableColumn<GenericItem, Double> colPrice = createDoubleColumn("Preço", "price");
        TableColumn<GenericItem, Date> colCreatedDate = new TableColumn<>("Data de Criação");
        TableColumn<GenericItem, Void> colActions = new TableColumn<>("Ações");

        colCreatedDate.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        colCreatedDate.setCellFactory(col -> new TableCell<GenericItem, Date>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatDate(item));
                }
            }
        });

        colActions.setCellFactory(col -> new TableCell<GenericItem, Void>() {
            private final Button btnDelete = new Button("Deletar");
            private final Button btnDownload = new Button("Baixar");

            {
                btnDelete.setOnAction(e -> {
                    GenericItem item = getTableView().getItems().get(getIndex());
                    controller.delete(item);
                    loadItems();
                });

                btnDownload.setOnAction(e -> {
                    GenericItem item = getTableView().getItems().get(getIndex());
                    downloadFile(item);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(btnDownload, btnDelete);
                    setGraphic(hbox);
                }
            }
        });

        // Set preferred widths for columns
        colName.setPrefWidth(150);
        colValue.setPrefWidth(100);
        colDescription.setPrefWidth(200);
        colPrice.setPrefWidth(100);
        colCreatedDate.setPrefWidth(150);
        colActions.setPrefWidth(100);

        tableView.getColumns().addAll(colName, colValue, colDescription, colPrice, colCreatedDate, colActions);
        tableView.getSelectionModel().selectedItemProperty()
            .addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    populateFields(newSelection);
                }
            });
    }

    private TableColumn<GenericItem, Float> createFloatColumn(String title , String property) {
        TableColumn<GenericItem, Float> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        return column;
    }

    private TableColumn<GenericItem, Double> createDoubleColumn(String title, String property) {
        TableColumn<GenericItem, Double> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        return column;
    }

    private TableColumn<GenericItem, String> createStringColumn(String title, String property) {
        TableColumn<GenericItem, String> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        return column;
    }

    private void downloadFile(GenericItem item) {
        try {
            if (item.getData() == null || item.getData().length == 0) {
                showAlert("Erro", "Nenhum dado disponível para download.");
                return;
            }
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salvar Arquivo");
            fileChooser.setInitialFileName(item.getName());
            File file = fileChooser.showSaveDialog(new Stage());
            if (file != null) {
                Files.write(file.toPath(), item.getData());
                showAlert("Sucesso", "Arquivo salvo: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            showAlert("Erro", "Erro ao baixar arquivo: " + e.getMessage());
        }
    }

    private void loadItems() {
        tableView.setItems(controller.findAll());
    }

    private void clearFields() {
        lblId.setText("");
        txtName.clear();
        txtValue.clear();
        txtDescription.clear();
        txtPrice.clear();
        lblCreatedDate.setText("");
        selectedFile = null; // Clear the selected file
    }

    private void populateFields(GenericItem item) {
        lblId.setText(String.valueOf(item.getId()));
        txtName.setText(item.getName());
        txtValue.setText(String.valueOf(item.getValue()));
        txtDescription.setText(item.getDescription());
        txtPrice.setText(String.valueOf(item.getPrice()));
        lblCreatedDate.setText(formatDate(item.getCreatedDate())); // Display created date in Brazilian format
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(date);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}