package com.safe.storage.views;

import com.safe.storage.common.Login;
import com.safe.storage.common.ViewController;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class SignInView extends FlowPane {

    protected final VBox vBox;
    protected final Text text;
    protected final TextField usernameInput;
    protected final PasswordField passwordInput;
    protected final PasswordField passwordConfirmationInput;
    protected final Button signInButton;
    protected final Button alreadyUserButton;

    final BooleanProperty firstTime = new SimpleBooleanProperty(true); // Variable to store the focus on stage load

    public SignInView() {

        vBox = new VBox();
        text = new Text();
        usernameInput = new TextField();
        passwordInput = new PasswordField();
        passwordConfirmationInput = new PasswordField();
        signInButton = new Button();
        alreadyUserButton = new Button();
        
        setAlignment(javafx.geometry.Pos.CENTER);
        setColumnHalignment(javafx.geometry.HPos.CENTER);
        setMaxHeight(USE_PREF_SIZE);
        setMaxWidth(USE_PREF_SIZE);
        setMinHeight(USE_PREF_SIZE);
        setMinWidth(USE_PREF_SIZE);
        setPrefHeight(400.0);
        setPrefWidth(600.0);

        vBox.setAlignment(javafx.geometry.Pos.CENTER);
        vBox.setSpacing(40.0);

        text.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        text.setStrokeWidth(0.0);
        text.setText("Criar Conta");
        text.setFont(new Font(24.0));

        usernameInput.setPromptText("Digite seu Username");
        
        usernameInput.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
                vBox.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });

        passwordInput.setPromptText("Digite sua Senha");
        passwordConfirmationInput.setPromptText("Repita sua Senha");

        signInButton.setMnemonicParsing(false);
        signInButton.setStyle("-fx-background-color: rgb(84, 113, 255);");
        signInButton.setText("Criar");
        signInButton.setTextFill(javafx.scene.paint.Color.WHITE);
        signInButton.setPadding(new Insets(6.0, 30.0, 6.0, 30.0));
        signInButton.setCursor(Cursor.HAND);
        signInButton.setOnMouseClicked(arg0 -> {
            if (!passwordInput.getText().equals(passwordConfirmationInput.getText())) {
                ViewController.showAlert("Erro", "As senhas não batem");
            } else {
                Login.signIn(usernameInput.getText(), passwordInput.getText());
            }
        });

        alreadyUserButton.setMnemonicParsing(false);
        alreadyUserButton.setStyle("-fx-background-color: transparent;");
        alreadyUserButton.setText("Já tenho uma Conta");
        alreadyUserButton.setTextFill(javafx.scene.paint.Color.CADETBLUE);
        alreadyUserButton.setCursor(Cursor.HAND);
        alreadyUserButton.setOnMouseClicked(arg0 -> {
            ViewController.navigate(new LoginView());
        });

        vBox.getChildren().add(text);
        vBox.getChildren().add(usernameInput);
        vBox.getChildren().add(passwordInput);
        vBox.getChildren().add(passwordConfirmationInput);
        vBox.getChildren().add(signInButton);
        vBox.getChildren().add(alreadyUserButton);
        getChildren().add(vBox);

        addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();
                if (!passwordInput.getText().equals(passwordConfirmationInput.getText())) {
                    ViewController.showAlert("Erro", "As senhas não batem");
                } else {
                    Login.signIn(usernameInput.getText(), passwordInput.getText());
                }
            }
        });
    }
}
