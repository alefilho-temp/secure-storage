// package com.safe.storage.views;

// import com.safe.storage.common.ViewController;
// import com.safe.storage.controllers.SafePasswordController;
// import com.safe.storage.models.SafePassword;
// import javafx.beans.binding.Bindings;
// import javafx.beans.property.SimpleObjectProperty;
// import javafx.collections.ObservableList;
// import javafx.scene.control.*;
// import javafx.scene.control.cell.PropertyValueFactory;
// import javafx.scene.image.Image;
// import javafx.scene.image.ImageView;
// import javafx.scene.layout.BorderPane;
// import javafx.scene.layout.GridPane;
// import javafx.stage.FileChooser;
// import javafx.stage.Stage;
// import javafx.util.Callback;

// import java.io.File;

// public class SafePasswordView extends BorderPane {
//     private Label lblId = new Label(""); // Label to hold the ID but not displayed in the UI
//     private TextField txtName = new TextField("");
//     private TextField txtUsername = new TextField("");
//     private TextField txtPassword = new TextField("");
//     private TextField txtAccessUrl = new TextField("");
//     private TextArea txtNote = new TextArea(""); // Changed to TextArea for multi-line input
//     private TextField txtImagePath = new TextField(""); // Field for image path
//     private ImageView imageView = new ImageView("/placeholder.png"); // Placeholder image

//     private TableView<SafePassword> tableView = new TableView<>();
//     private SafePasswordController controller;

//     public SafePasswordView() {
//         controller = new SafePasswordController();

//         GridPane formPane = createFormPane();
//         setTop(formPane);
//         setCenter(tableView);
//         generateColumns();
//         bindProperties();
//         loadSafePasswords();
//     }

//     private GridPane createFormPane() {
//         GridPane formPane = new GridPane();
//         addFormFields(formPane);
//         addButtons(formPane);
//         return formPane;
//     }

//     private void addFormFields(GridPane formPane) {
//         formPane.add(new Label("ID: "), 0, 0);
//         formPane.add(lblId, 1, 0);
//         formPane.add(new Label("Name: "), 0, 1);
//         formPane.add(txtName, 1, 1);
//         formPane.add(new Label("Username: "), 0, 2);
//         formPane.add(txtUsername, 1, 2);
//         formPane.add(new Label("Password: "), 0, 3);
//         formPane.add(txtPassword, 1, 3);
//         formPane.add(new Label("Access URL: "), 0, 4);
//         formPane.add(txtAccessUrl, 1, 4);
//         formPane.add(new Label("Note: "), 0, 5);
//         formPane.add(txtNote, 1, 5);
//         formPane.add(new Label("Image Path: "), 0, 6);
//         formPane.add(txtImagePath, 1, 6);
        
//         // Set the ImageView properties
//         imageView.setFitHeight(150);
//         imageView.setFitWidth(150);
//         imageView.setPreserveRatio(true);
//         formPane.add(imageView, 1, 8); // Add ImageView to the form

//         // Add a listener to the image path text field
//         txtImagePath.textProperty().addListener((observable, oldValue, newValue) -> {
//             if (!newValue.isEmpty()) {
//                 displayImage(new File(newValue)); // Display the image when the text field changes
//             } else {
//                 imageView.setImage(new Image("/placeholder.png")); // Reset to placeholder if empty
//             }
//         });
//     }

//     private void addButtons(GridPane formPane) {
//         Button btnChooseImage = createChooseImageButton();
//         formPane.add(btnChooseImage, 2, 6); // Add button to form

//         Button btnSave = createSaveButton();
//         Button btnSearch = createSearchButton();
//         Button btnClear = createClearButton();

//         formPane.add(btnSave, 0, 7);
//         formPane.add(btnSearch, 1, 7);
//         formPane.add(btnClear, 2, 0);
//     }

//     private Button createChooseImageButton() {
//         Button btnChooseImage = new Button("Choose Image");
//         btnChooseImage.setOnAction(e -> {
//             FileChooser fileChooser = new FileChooser();
//             File file = fileChooser.showOpenDialog(new Stage());
//             if (file != null) {
//                 txtImagePath.setText(file.getAbsolutePath());
//                 displayImage(file);
//                 if (!validImage(file)) {
//                     ViewController.showAlert("Error", "Unsupported image format. Please use PNG or JPG.");
//                 }
//             }
//         });
//         return btnChooseImage;
//     }

//     private Button createSaveButton() {
//         Button btnSave = new Button("Save");
//         btnSave.setOnAction(e -> {
//             try {
//                 SafePassword safePassword = new SafePassword();
//                 if (!lblId.getText().isEmpty()) {
//                     safePassword.setId(Integer.parseInt(lblId.getText()));
//                 }
//                 populateSafePassword(safePassword);
//                 controller.save(safePassword);
//                 tableView.refresh();
//                 clear();
//             } catch (Exception err) {
//                 ViewController.showAlert("Error", "Error saving SafePassword");
//             }
//         });
//         return btnSave;
//     }

//     private Button createSearchButton() {
//         Button btnSearch = new Button("Search");
//         btnSearch.setOnAction(e -> {
//             String searchName = txtName.getText();

//             if (searchName.trim().length() == 0) {
//                 clear();
//                 controller.loadAllPasswords();
//             } else {
//                 ObservableList<SafePassword> result = controller.searchBy("name", searchName);
//                 if (!result.isEmpty()) {
//                     populateFields(result.get(0));
//                     displayImage(new File(result.get(0).getImagePath()));
//                 }
//             }
//         });

//         return btnSearch;
//     }

//     private Button createClearButton() {
//         Button btnClear = new Button("Clear");
//         btnClear.setOnAction(e -> clear());
//         return btnClear;
//     }

//     private void populateSafePassword(SafePassword safePassword) {
//         safePassword.setName(txtName.getText());
//         safePassword.setUsername(txtUsername.getText());
//         safePassword.setPassword(txtPassword.getText());
//         safePassword.setAccessUrl(txtAccessUrl.getText());
//         safePassword.setNote(txtNote.getText());
//         safePassword.setImagePath(txtImagePath.getText());
//     }

//     private void populateFields(SafePassword safePassword) {
//         lblId.setText(String.valueOf(safePassword.getId()));
//         txtName.setText(safePassword.getName());
//         txtUsername.setText(safePassword.getUsername());
//         txtPassword.setText(safePassword.getPassword());
//         txtAccessUrl.setText(safePassword.getAccessUrl());
//         txtNote.setText(safePassword.getNote());
//         txtImagePath.setText(safePassword.getImagePath());
//     }

//     private void loadSafePasswords() {
//         try {
//             tableView.setItems(controller.findAll());
//         } catch (Exception e) {
//             ViewController.showAlert("Error", "Error loading SafePasswords");
//         }
//     }

//     private void clear() {
//         controller.clearFields();
//         tableView.getSelectionModel().clearSelection();
//         lblId.setText("");
//     }

//     private void displayImage(File file) {
//         String filePath = file.toURI().toString();
//         if (validImage(file)) {
//             imageView.setImage(new Image(filePath));
//         } else {
//             imageView.setImage(new Image("/placeholder.png"));
//         }
//     }

//     private boolean validImage(File file) {
//         String filePath = file.toURI().toString();
//         String fileExtension = getFileExtension(filePath);

//         return 
//             fileExtension.equalsIgnoreCase("png") || 
//             fileExtension.equalsIgnoreCase("jpg") || 
//             fileExtension.equalsIgnoreCase("jpeg")
//         ;
//     }

//     private String getFileExtension(String filePath) {
//         int lastIndexOfDot = filePath.lastIndexOf('.');

//         return (lastIndexOfDot == -1) ? "" : filePath.substring(lastIndexOfDot + 1);
//     }

//     public void generateColumns() {
//         TableColumn<SafePassword, Image> colImage = createImageColumn();
//         TableColumn<SafePassword, String> colName = createStringColumn("Name", "name");
//         TableColumn<SafePassword, String> colUsername = createStringColumn("Username", "username");
//         TableColumn<SafePassword, String> colPassword = createStringColumn("Password", "password");
//         TableColumn<SafePassword, String> colAccessUrl = createStringColumn("Access URL", "accessUrl");
//         TableColumn<SafePassword, String> colNote = createStringColumn("Note", "note");
//         TableColumn<SafePassword, Void> colActions = createActionColumn();

//         tableView.getColumns().addAll(colImage, colName, colUsername, colPassword, colAccessUrl, colNote, colActions);
//         tableView.getSelectionModel().selectedItemProperty()
//             .addListener((obs, oldSelection, newSelection) -> {
//                 if (newSelection != null) {
//                     populateFields(newSelection);
//                     displayImage(new File(newSelection.getImagePath()));
//                 }
//             });
//     }

//     private TableColumn<SafePassword, Image> createImageColumn() {
//         TableColumn<SafePassword, Image> colImage = new TableColumn<>("Image");
//         colImage.setCellValueFactory(param -> {
//             ImageView imageView = new ImageView();
//             imageView.setFitHeight(50);
//             imageView.setFitWidth(50);
//             String imagePath = param.getValue().getImagePath();
//             imageView.setImage(validImage(new File(imagePath)) ? new Image(new File(imagePath).toURI().toString()) : new Image("/placeholder.png"));
//             return new SimpleObjectProperty<>(imageView.getImage());
//         });

//         colImage.setCellFactory(new Callback<TableColumn<SafePassword, Image>, TableCell<SafePassword, Image>>() {
//             @Override
//             public TableCell<SafePassword, Image> call(TableColumn<SafePassword, Image> param) {
//                 return new TableCell<SafePassword, Image>() {
//                     private final ImageView imageView = new ImageView();

//                     @Override
//                     protected void updateItem(Image item, boolean empty) {
//                         super.updateItem(item, empty);
//                         if (empty || item == null) {
//                             setGraphic(null);
//                         } else {
//                             imageView.setImage(item);
//                             imageView.setFitHeight(50);
//                             imageView.setFitWidth(50);
//                             setGraphic(imageView);
//                         }
//                     }
//                 };
//             }
//         });
//         return colImage;
//     }

//     private TableColumn<SafePassword, String> createStringColumn(String title, String property) {
//         TableColumn<SafePassword, String> column = new TableColumn<>(title);
//         column.setCellValueFactory(new PropertyValueFactory<>(property));
//         return column;
//     }

//     private TableColumn<SafePassword, Void> createActionColumn() {
//         TableColumn<SafePassword, Void> colActions = new TableColumn<>("Actions");
//         colActions.setCellFactory(new Callback<>() {
//             @Override
//             public TableCell<SafePassword, Void> call(TableColumn<SafePassword, Void> param) {
//                 TableCell<SafePassword, Void> cell = new TableCell<>() {
//                     final Button btnDelete = new Button("Delete");
//                     {
//                         btnDelete.setOnAction(e -> {
//                             try {
//                                 SafePassword safePassword = tableView.getItems().get(getIndex());
//                                 controller.delete(safePassword);
//                                 tableView.getItems().remove(safePassword);
//                             } catch (Exception err) {
//                                 ViewController.showAlert("Error", "Error deleting SafePassword");
//                             }
//                         });
//                     }
//                     public void updateItem(Void item, boolean empty) {
//                         super.updateItem(item, empty);
//                         setGraphic(empty ? null : btnDelete);
//                     }
//                 };
//                 return cell;
//             }
//         });
//         return colActions;
//     }

//     public void bindProperties() {
//         Bindings.bindBidirectional(txtName.textProperty(), controller.nameProperty());
//         Bindings.bindBidirectional(txtUsername.textProperty(), controller.usernameProperty());
//         Bindings.bindBidirectional(txtPassword.textProperty(), controller.passwordProperty());
//         Bindings.bindBidirectional(txtAccessUrl.textProperty(), controller.accessUrlProperty());
//         Bindings.bindBidirectional(txtNote.textProperty(), controller.noteProperty());
//         Bindings.bindBidirectional(txtImagePath.textProperty(), controller.imagePathProperty());
//     }
// }

















































package com.safe.storage.views;

import com.safe.storage.common.ViewController;
import com.safe.storage.controllers.SafePasswordController;
import com.safe.storage.models.SafePassword;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SafePasswordView extends SplitPane {
    private Label lblId = new Label(""); // Rótulo para armazenar o ID, mas não exibido na IU
    private TextField txtName = new TextField("");
    private TextField txtUsername = new TextField("");
    private TextField txtPassword = new TextField("");
    private TextField txtAccessUrl = new TextField("");
    private TextArea txtNote = new TextArea(""); // Alterado para TextArea para entrada de várias linhas
    private Label lblImagePath = new Label(""); // Rótulo para o caminho da imagem, apenas leitura
    private ImageView imageView = new ImageView("/placeholder.png"); // Imagem de espaço reservado

    private TableView<SafePassword> tableView = new TableView<>();
    private SafePasswordController controller;

    public SafePasswordView() {
        controller = new SafePasswordController();

        GridPane formPane = createFormPane();

        setDividerPositions(0.5); // Adjust the divider position as needed
        getItems().addAll(tableView, formPane);
        generateColumns();
        bindProperties();
        loadSafePasswords();
    }

    private GridPane createFormPane() {
        GridPane formPane = new GridPane();
        formPane.setStyle("-fx-padding: 10; -fx-background-color: #f9f9f9; -fx-border-color: #cccccc; -fx-border-width: 1;"); // Estilo do painel
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
        txtName.setFocusTraversable(true);

        formPane.add(createCopyButton(txtName), 2, 1); // Copy button for Name
        formPane.add(new Label("Usuário: "), 0, 2);
        formPane.add(txtUsername, 1, 2);
        txtUsername.setStyle("-fx-font-size: 14px; -fx-padding: 5; -fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-width: 1;");
        txtUsername.setFocusTraversable(true);

        formPane.add(createCopyButton(txtUsername), 2, 2); // Copy button for Username
        formPane.add(new Label("Senha: "), 0, 3);
        formPane.add(txtPassword, 1, 3);
        txtPassword.setStyle("-fx-font-size: 14px; -fx-padding: 5; -fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-width: 1;");
        txtPassword.setFocusTraversable(true);

        formPane.add(createCopyButton(txtPassword), 2, 3); // Copy button for Password
        formPane.add(new Label("URL de Acesso: "), 0, 4);
        formPane.add(txtAccessUrl, 1, 4);
        txtAccessUrl.setStyle("-fx-font-size: 14px; -fx-padding: 5; -fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-width: 1;"); 
        txtAccessUrl.setFocusTraversable(true);
        
        formPane.add(createCopyButton(txtAccessUrl), 2, 4); // Copy button for Access URL
        formPane.add(new Label("Nota: "), 0, 5);
        formPane.add(txtNote, 1, 5);
        txtNote.setStyle("-fx-font-size: 14px; -fx-padding: 5; -fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-width: 1;"); 
        txtNote.setFocusTraversable(true);

        formPane.add(createCopyButton(txtNote), 2, 5); // Copy button for Note
        formPane.add(new Label("Caminho da Imagem: "), 0, 6);
        formPane.add(lblImagePath, 1, 6); // Usar Label em vez de TextField

        // Definindo propriedades da ImageView
        imageView.setFitHeight(300);
        imageView.setFitWidth(300);
        imageView.setPreserveRatio(true);
        formPane.add(imageView, 1, 7); // Adiciona ImageView ao formulário

        // Enable wrapping for the text area
        txtNote.setWrapText(true);
        txtNote.setPrefRowCount(3); // Set preferred row count for the TextArea
    }

    private Button createCopyButton(TextInputControl textField) {
        Button createCopyButton = new Button("Copiar");
        createCopyButton.setCursor(javafx.scene.Cursor.HAND); // Cursor de mão
        createCopyButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5;"); // Style for copy button
        createCopyButton.setOnAction(e -> {
            String content = textField.getText();
            if (!content.isEmpty()) {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection selection = new StringSelection(content);
                clipboard.setContents(selection, null);
                ViewController.showAlert("Sucesso", "Texto copiado para a área de transferência!");
            }
        });
        return createCopyButton;
    }

    private void addButtons(GridPane formPane) {
        Button btnChooseImage = createChooseImageButton();
        btnChooseImage.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5;"); // Style for choose image button
        formPane.add(btnChooseImage, 2, 6); // Adiciona botão ao formulário

        Button btnSave = createSaveButton();
        btnSave.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5;"); // Style for save button
        Button btnSearch = createSearchButton();
        btnSearch.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5;"); // Style for search button
        Button btnClear = createClearButton();
        btnClear.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5;"); // Style for clear button

        formPane.add(btnSave, 0, 8);
        formPane.add(btnSearch, 1, 8);
        formPane.add(btnClear, 2, 8);
    }

    private Button createChooseImageButton() {
        Button btnChooseImage = new Button("Escolher Imagem");
        btnChooseImage.setCursor(javafx.scene.Cursor.HAND); // Cursor de mão
        btnChooseImage.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(new Stage());
            if (file != null) {
                lblImagePath.setText(file.getAbsolutePath()); // Atualiza o Label com o caminho da imagem
                displayImage(file);
                if (!validImage(file)) {
                    ViewController.showAlert("Erro", "Formato de imagem não suportado. Por favor, use PNG ou JPG.");
                }
            }
        });
        return btnChooseImage;
    }

    private Button createSaveButton() {
        Button btnSave = new Button("Salvar");
        btnSave.setCursor(javafx.scene.Cursor.HAND); // Cursor de mão
        btnSave.setOnAction(e -> {
            try {
                SafePassword safePassword = new SafePassword();
                if (!lblId.getText().isEmpty()) {
                    safePassword.setId(Integer.parseInt(lblId.getText()));
                }
                populateSafePassword(safePassword);
                controller.save(safePassword);
                tableView.refresh();
                clear();
            } catch (Exception err) {
                ViewController.showAlert("Erro", "Erro ao salvar SafePassword");
            }
        });
        return btnSave;
    }

    private Button createSearchButton() {
        Button btnSearch = new Button("Buscar");
        btnSearch.setCursor(javafx.scene.Cursor.HAND); // Cursor de mão
        btnSearch.setOnAction(e -> {
            String searchName = txtName.getText();

            if (searchName.trim().length() == 0) {
                clear();
                controller.loadAllPasswords();
            } else {
                ObservableList<SafePassword> result = controller.searchBy("name", searchName);
                if (!result.isEmpty()) {
                    populateFields(result.get(0));
                    displayImage(new File(result.get(0).getImagePath()));
                }
            }
        });

        return btnSearch;
    }

    private Button createClearButton() {
        Button btnClear = new Button("Limpar");
        btnClear.setCursor(javafx.scene.Cursor.HAND); // Cursor de mão
        btnClear.setOnAction(e -> clear());
        return btnClear;
    }

    private void populateSafePassword(SafePassword safePassword) {
        safePassword.setName(txtName.getText());
        safePassword.setUsername(txtUsername.getText());
        safePassword.setPassword(txtPassword.getText());
        safePassword.setAccessUrl(txtAccessUrl.getText());
        safePassword.setNote(txtNote.getText());
        safePassword.setImagePath(lblImagePath.getText()); // Usar o Label para o caminho da imagem
    }

    private void populateFields(SafePassword safePassword) {
        lblId.setText(String.valueOf(safePassword.getId()));
        txtName.setText(safePassword.getName());
        txtUsername.setText(safePassword.getUsername());
        txtPassword.setText(safePassword.getPassword());
        txtAccessUrl.setText(safePassword.getAccessUrl());
        txtNote.setText(safePassword.getNote());
        lblImagePath.setText(safePassword.getImagePath()); // Atualiza o Label com o caminho da imagem
    }

    private void loadSafePasswords() {
        try {
            tableView.setItems(controller.findAll());
        } catch (Exception e) {
            ViewController.showAlert("Erro", "Erro ao carregar SafePasswords");
        }
    }

    private void clear() {
        controller.clearFields();
        tableView.getSelectionModel().clearSelection();
        lblId.setText("");
        lblImagePath.setText(""); // Limpa o Label do caminho da imagem
        imageView.setImage(new Image("/placeholder.png"));
    }

    private void displayImage(File file) {
        String filePath = file.toURI().toString();
        if (validImage(file)) {
            imageView.setImage(new Image(filePath));
        } else {
            imageView.setImage(new Image("/placeholder.png"));
        }
    }

    private boolean validImage(File file) {
        String filePath = file.toURI().toString();
        String fileExtension = getFileExtension(filePath);

        return 
            fileExtension.equalsIgnoreCase("png") || 
            fileExtension.equalsIgnoreCase("jpg") || 
            fileExtension.equalsIgnoreCase("jpeg");
    }

    private String getFileExtension(String filePath) {
        int lastIndexOfDot = filePath.lastIndexOf('.');

        return (lastIndexOfDot == -1) ? "" : filePath.substring(lastIndexOfDot + 1);
    }

    public void generateColumns() {
        TableColumn<SafePassword, Image> colImage = createImageColumn();
        TableColumn<SafePassword, String> colName = createStringColumn("Nome", "name");
        TableColumn<SafePassword, String> colUsername = createStringColumn("Usuário", "username");
        TableColumn<SafePassword, String> colPassword = createStringColumn("Senha", "password");
        TableColumn<SafePassword, String> colAccessUrl = createAccessUrlColumn(); // Change to hyperlink column
        TableColumn<SafePassword, String> colNote = createStringColumn("Nota", "note");
        TableColumn<SafePassword, Void> colActions = createActionColumn();

        tableView.getColumns().addAll(colImage, colName, colUsername, colPassword, colAccessUrl, colNote, colActions);
        tableView.getSelectionModel().selectedItemProperty()
            .addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    populateFields(newSelection);
                    displayImage(new File(newSelection.getImagePath()));
                }
            });

        // Estilo da tabela
        tableView.setStyle("-fx-border-color: transparent; -fx-background-color: transparent; -fx-padding: 0;"); // Remover cor de fundo
        for (TableColumn<SafePassword, ?> column : tableView.getColumns()) {
            column.setStyle("-fx-border-color: transparent; -fx-background-color: transparent; -fx-alignment: CENTER_LEFT; -fx-font-size: 14px;"); // Remover cor de fundo das colunas e alinhar texto à esquerda
            column.setPrefWidth(150); // Define uma largura preferida para as colunas
        }
        
        // Adicionando borda superior às linhas e definindo cor de fundo para células vazias
        tableView.setRowFactory(tv -> {
            TableRow<SafePassword> row = new TableRow<SafePassword>() {
                @Override
                protected void updateItem(SafePassword item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setStyle("-fx-border-color: transparent; -fx-background-color: white;"); // Células vazias em branco
                    } else {
                        setStyle("-fx-border-color: transparent; -fx-border-width: 2 0 0 0; -fx-border-style: solid;"); // Borda superior mais grossa
                    }
                }
            };
            return row;
        });
    }

    private TableColumn<SafePassword, String> createAccessUrlColumn() {
        TableColumn<SafePassword, String> colAccessUrl = new TableColumn<>("URL de Acesso");
        colAccessUrl.setCellValueFactory(new PropertyValueFactory<>("accessUrl"));
        colAccessUrl.setCellFactory(new Callback<TableColumn<SafePassword, String>, TableCell<SafePassword, String>>() {
            @Override
            public TableCell<SafePassword, String> call(TableColumn<SafePassword, String> param) {
                return new TableCell<SafePassword, String>() {
                    private final Hyperlink hyperlink = new Hyperlink();

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            hyperlink.setText(item);
                            hyperlink.setStyle("-fx-text-fill: blue; -fx-underline: true;"); // Style for hyperlink
                            hyperlink.setOnAction(e -> openUrl(item));
                            setGraphic(hyperlink);
                        }
                    }
                };
            }
        });
        return colAccessUrl;
    }

    private void openUrl(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            ViewController.showAlert("Erro", "Erro ao abrir URL: " + e.getMessage());
        }
    }

    private TableColumn<SafePassword, Image> createImageColumn() {
        TableColumn<SafePassword, Image> colImage = new TableColumn<>("Imagem");
        colImage.setCellValueFactory(param -> {
            ImageView imageView = new ImageView();
            imageView.setFitHeight(50);
            imageView.setFitWidth(150); // Ajuste para manter a proporção 3:1
            String imagePath = param.getValue().getImagePath();
            imageView.setImage(validImage(new File(imagePath)) ? new Image(new File(imagePath).toURI().toString()) : new Image("/placeholder.png"));
            return new SimpleObjectProperty<>(imageView.getImage());
        });

        colImage.setCellFactory(new Callback<TableColumn<SafePassword, Image>, TableCell<SafePassword, Image>>() {
            @Override
            public TableCell<SafePassword, Image> call(TableColumn<SafePassword, Image> param) {
                return new TableCell<SafePassword, Image>() {
                    private final ImageView imageView = new ImageView();

                    @Override
                    protected void updateItem(Image item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            imageView.setImage(item);
                            imageView.setPreserveRatio(true);
                            imageView.setFitHeight(50);
                            imageView.setFitWidth(150); // Ajuste para manter a proporção 3:1
                            setGraphic(imageView);
                        }
                    }
                };
            }
        });
        return colImage;
    }

    private TableColumn<SafePassword, String> createStringColumn(String title, String property) {
        TableColumn<SafePassword, String> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setMinWidth(100); // Define uma largura mínima para as colunas
        column.setPrefWidth(150); // Define uma largura preferida para as colunas
        return column;
    }

    private TableColumn<SafePassword, Void> createActionColumn() {
        TableColumn<SafePassword, Void> colActions = new TableColumn<>("Ações");
        colActions.setCellFactory(new Callback<>() {
            @Override
            public TableCell<SafePassword, Void> call(TableColumn<SafePassword, Void> param) {
                TableCell<SafePassword, Void> cell = new TableCell<>() {
                    final Button btnDelete = new Button("Excluir");
                    {
                        btnDelete.setCursor(javafx.scene.Cursor.HAND); // Cursor de mão
                        btnDelete.setOnAction(e -> {
                            try {
                                SafePassword safePassword = tableView.getItems().get(getIndex());
                                controller.delete(safePassword);
                                tableView.getItems().remove(safePassword);
                            } catch (Exception err) {
                                ViewController.showAlert("Erro", "Erro ao excluir SafePassword");
                            }
                        });
                    }
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : btnDelete);
                    }
                };
                return cell;
            }
        });
        return colActions;
    }

    public void bindProperties() {
        Bindings.bindBidirectional(txtName.textProperty(), controller.nameProperty());
        Bindings.bindBidirectional(txtUsername.textProperty(), controller.usernameProperty());
        Bindings.bindBidirectional(txtPassword.textProperty(), controller.passwordProperty());
        Bindings.bindBidirectional(txtAccessUrl.textProperty(), controller.accessUrlProperty());
        Bindings.bindBidirectional(txtNote.textProperty(), controller.noteProperty());
        // Bind the image path label to the controller's image path property
        // lblImagePath.textProperty().bind(controller.imagePathProperty());
    }
}