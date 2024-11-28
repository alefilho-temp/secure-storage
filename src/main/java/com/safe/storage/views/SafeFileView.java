package com.safe.storage.views;

import com.safe.storage.common.ViewController;
import com.safe.storage.controllers.SafeFileController;
import com.safe.storage.models.SafeFile;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class SafeFileView extends SplitPane {
    private Label lblId = new Label("");
    private TextField txtName = new TextField("");
    private TextField txtOriginalName = new TextField();
    private TextArea txtNote = new TextArea();
    private Label lblFilePath = new Label("");
    private Label lblFileSize = new Label("");

    private File selectedFile;

    private TableView<SafeFile> tableView = new TableView<>();
    private SafeFileController controller;

    public SafeFileView() {
        controller = new SafeFileController();

        GridPane formPane = createFormPane();

        setDividerPositions(0.5);
        getItems().addAll(tableView, formPane);
        generateColumns();
        bindProperties();
        loadSafeFiles();
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
        txtName.setStyle("-fx-font-size: 14px; -fx-padding: 5; -fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-width: 1;");

        formPane.add(createCopyButton(txtName), 2, 1); // Copy button for Name
        
        formPane.add(new Label("Nome Original: "), 0, 2);
        formPane.add(txtOriginalName, 1, 2);
        txtOriginalName.setStyle("-fx-font-size: 14px; -fx-padding: 5; -fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-width: 1;");
        
        formPane.add(createCopyButton(txtOriginalName), 2, 2); // Copy button for Original Name
        
        formPane.add(new Label("Nota: "), 0, 3);
        formPane.add(txtNote, 1, 3);
        txtNote.setStyle("-fx-font-size: 14px; -fx-padding: 5; -fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-width: 1;");
        
        formPane.add(createCopyButton(txtNote), 2, 3); // Copy button for Note
        
        formPane.add(new Label("Caminho do Arquivo: "), 0, 4);
        formPane.add(lblFilePath, 1, 4);
        
        formPane.add(new Label("Tamanho do Arquivo: "), 0, 5);
        formPane.add(lblFileSize, 1, 5);
    }

    private Button createCopyButton(TextInputControl textField) {
        Button copyButton = new Button("Copiar ");
        copyButton.setOnAction(e -> {
            String content = textField.getText();
            if (!content.isEmpty()) {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection selection = new StringSelection(content);
                clipboard.setContents(selection, null);
                ViewController.showAlert("Sucesso", "Texto copiado para a área de transferência!");
            }
        });
        copyButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5;");
        return copyButton;
    }

    private void addButtons(GridPane formPane) {
        Button btnChooseFile = createChooseFileButton();
        btnChooseFile.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5;");
        formPane.add(btnChooseFile, 2, 4);

        Button btnSave = createSaveButton();
        btnSave.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5;");
        Button btnSearch = createSearchButton();
        btnSearch.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5;");
        Button btnClear = createClearButton();
        btnClear.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5;");

        formPane.add(btnSave, 0, 6);
        formPane.add(btnSearch, 1, 6);
        formPane.add(btnClear, 2, 6);
    }

    private Button createChooseFileButton() {
        Button btnChooseFile = new Button("Escolher Arquivo");
        btnChooseFile.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            selectedFile = fileChooser.showOpenDialog(new Stage());
            if (selectedFile != null) {
                lblFilePath.setText(selectedFile.getAbsolutePath());
                lblFileSize.setText(formatBytes(selectedFile.length()));
                txtOriginalName.setText(selectedFile.getName()); // Update original name when file is chosen
            }
        });
        return btnChooseFile;
    }

    private Button createSaveButton() {
        Button btnSave = new Button("Salvar");
        btnSave.setOnAction(e -> {
            try {
                SafeFile safeFile = new SafeFile();
                if (!lblId.getText().isEmpty()) {
                    safeFile.setId(Integer.parseInt(lblId.getText()));
                }
                populateSafeFile(safeFile);
                if (selectedFile != null) {
                    byte[] fileData = Files.readAllBytes(selectedFile.toPath());
                    safeFile.setData(fileData);
                    safeFile.setSize(selectedFile.length());
                }
                controller.save(safeFile);
                loadSafeFiles();
                clear();
            } catch (Exception err) {
                ViewController.showAlert("Erro", "Erro ao salvar SafeFile: " + err.getMessage());
            }
        });
        return btnSave;
    }

    private Button createSearchButton() {
        Button btnSearch = new Button("Buscar");
        btnSearch.setOnAction(e -> {
            String searchName = txtName.getText();

            if (searchName.trim().length() == 0) {
                System.out.println("searchName: " + searchName);
                clear();
                // loadSafeFiles();
                controller.loadAllFiles();
            } else {
                ObservableList<SafeFile> result = controller.searchBy("name", searchName);

                if (!result.isEmpty()) {
                    populateFields(result.get(0));
                }
            }
        });
        return btnSearch;
    }

    private Button createClearButton() {
        Button btnClear = new Button("Limpar");
        btnClear.setOnAction(e -> clear());
        return btnClear;
    }

    private void generateColumns() {
        TableColumn<SafeFile, String> colName = createStringColumn("Nome", "name");
        TableColumn<SafeFile, String> colOriginalName = createStringColumn("Nome Original", "originalName");
        TableColumn<SafeFile, String> colNote = createStringColumn("Nota", "note");
        TableColumn<SafeFile, String> colFilePath = createStringColumn("Caminho do Arquivo", "originalName");
        TableColumn<SafeFile, String> colFileSize = createStringColumn("Tamanho do Arquivo", "size");
        TableColumn<SafeFile, Void> colActions = createActionColumn();

        colFileSize.setCellValueFactory(cellData -> {
            SafeFile safeFile = cellData.getValue();
            return new SimpleObjectProperty<>(formatBytes(safeFile.getSize()));
        });

        tableView.getColumns().addAll(colName, colOriginalName, colNote, colFilePath, colFileSize, colActions);
        tableView.getSelectionModel().selectedItemProperty()
            .addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    populateFields(newSelection);
                }
            });
    }

    private TableColumn<SafeFile, String> createStringColumn(String title, String property) {
        TableColumn<SafeFile, String> column = new TableColumn<>(title);
        column.setStyle("-fx-border-color: transparent; -fx-background-color: transparent; -fx-alignment: CENTER_LEFT; -fx-font-size: 14px;"); 
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setMinWidth(100);
        column.setPrefWidth(150);
        return column;
    }

    private TableColumn<SafeFile, Void> createActionColumn() {
        TableColumn<SafeFile, Void> colActions = new TableColumn<>("Ações");
        colActions.setCellFactory(new Callback<>() {
            @Override
            public TableCell<SafeFile, Void> call(TableColumn<SafeFile, Void> param) {
                return new TableCell<SafeFile, Void>() {
                    final Button btnDownload = new Button("Baixar");
                    final Button btnDelete = new Button("Excluir");

                    {
                        btnDownload.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-padding: 5;");
                        btnDownload.setCursor(javafx.scene.Cursor.HAND);
                        btnDownload.setOnAction(e -> {
                            try {
                                SafeFile safeFile = tableView.getItems().get(getIndex());
                                FileChooser fileChooser = new FileChooser();
                                fileChooser.setInitialFileName(safeFile.getOriginalName());
                                File downloadFile = fileChooser.showSaveDialog(new Stage());
                                if (downloadFile != null) {
                                    Files.write(downloadFile.toPath(), safeFile.getData());
                                }
                            } catch (IOException ex) {
                                ViewController.showAlert("Erro", "Erro ao baixar arquivo: " + ex.getMessage());
                            }
                        });

                        btnDelete.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-padding: 5;");
                        btnDelete.setCursor(javafx.scene.Cursor.HAND);
                        btnDelete.setOnAction(e -> {
                            try {
                                SafeFile safeFile = tableView.getItems().get(getIndex());
                                controller.delete(safeFile);
                                loadSafeFiles(); // Refresh the table view
                                clear(); // Clear the form fields
                            } catch (Exception ex) {
                                ViewController.showAlert("Erro", "Erro ao excluir arquivo: " + ex.getMessage());
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox hBox = new HBox(5, btnDownload, btnDelete);
                            setGraphic(hBox);
                        }
                    }
                };
            }
        });
        return colActions;
    }

    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int unit = 1024;
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int idx = 0;
        while (bytes >= unit && idx < units.length - 1) {
            bytes /= unit;
            idx++;
        }
        return String.format("%.1f %s", (double) bytes, units[idx]);
    }

    private void loadSafeFiles() {
        try {
            tableView.setItems(controller.findAll());
        } catch (Exception e) {
            ViewController.showAlert("Erro", "Erro ao carregar SafeFiles: " + e.getMessage());
        }
    }

    private void clear() {
        controller.clearFields();
        tableView.getSelectionModel().clearSelection();
        lblId.setText("");
        lblFilePath.setText("");
        lblFileSize.setText("");
        txtOriginalName.clear();
        selectedFile = null;
    }

    private void bindProperties() {
        Bindings.bindBidirectional(txtName.textProperty(), controller.nameProperty());
        Bindings.bindBidirectional(txtNote.textProperty(), controller.noteProperty());
        // Bind the original name and file size labels if needed
    }

    // private void populateFields(SafeFile safeFile) {
    //     lblId.setText(String.valueOf(safeFile.getId()));
    //     txtName.setText(safeFile.getName());
    //     txtOriginalName.setText(safeFile.getOriginalName());
    //     txtNote.setText(safeFile.getNote());
    //     lblFilePath.setText(safeFile.getOriginalName());
    //     lblFileSize.setText(formatBytes(safeFile.getSize()));
    //     if (selectedFile == null) {
    //         selectedFile = new File("src/main/resources/temp");
    //         try {
    //             Files.write(Paths.get("src/main/resources/temp"), safeFile.getData(), StandardOpenOption.CREATE_NEW);
    //         } catch (IOException e) {
    //             // TODO Auto-generated catch block
    //             e.printStackTrace();
    //         }
    //     }
    // }

    private void populateFields(SafeFile safeFile) {
        lblId.setText(String.valueOf(safeFile.getId()));
        txtName.setText(safeFile.getName());
        txtOriginalName.setText(safeFile.getOriginalName());
        txtNote.setText(safeFile.getNote());
        lblFilePath.setText(safeFile.getOriginalName());
        lblFileSize.setText(formatBytes(safeFile.getSize()));
    
        // Create a temporary file with a unique name
        if (selectedFile == null) {
            try {
                // Create a temporary file in the default temporary file directory
                Path tempFile = Files.createTempFile("safeFile_", ".tmp");
                selectedFile = tempFile.toFile(); // Update selectedFile to point to the temp file
                System.out.println("Temporary file created at: " + selectedFile.getAbsolutePath());
    
                // Write the data to the temporary file
                Files.write(tempFile, safeFile.getData(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // If the selected file already exists, you may want to overwrite it or handle it
            try {
                // Write to the existing temporary file
                Files.write(selectedFile.toPath(), safeFile.getData(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void populateSafeFile(SafeFile safeFile) {
        safeFile.setName(txtName.getText());
        safeFile.setOriginalName(txtOriginalName.getText());
        safeFile.setNote(txtNote.getText());
        // safeFile.setFilePath(lblFilePath.getText());
        // safeFile.setFileSize(lblFileSize.getText());
    }
}
