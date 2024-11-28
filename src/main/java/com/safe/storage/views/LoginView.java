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

public class LoginView extends FlowPane {

    protected final VBox vBox;
    protected final Text text;
    protected final TextField usernameInput;
    protected final PasswordField passwordInput;
    protected final Button loginButton;
    protected final Button createUserButton;

    final BooleanProperty firstTime = new SimpleBooleanProperty(true); // Variable to store the focus on stage load

    public LoginView() {
        vBox = new VBox();
        text = new Text();
        usernameInput = new TextField();
        passwordInput = new PasswordField();
        loginButton = new Button();
        createUserButton = new Button();
        
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
        text.setText("Login");
        text.setFont(new Font(24.0));

        usernameInput.setPromptText("Digite seu Username");
        
        usernameInput.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
                vBox.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });

        passwordInput.setPromptText("Digite sua Senha");

        loginButton.setMnemonicParsing(false);
        loginButton.setStyle("-fx-background-color: rgb(84, 113, 255);");
        loginButton.setText("Logar");
        loginButton.setTextFill(javafx.scene.paint.Color.WHITE);
        loginButton.setPadding(new Insets(6.0, 30.0, 6.0, 30.0));
        loginButton.setCursor(Cursor.HAND);
        loginButton.setOnMouseClicked(arg0 -> {
            Login.logIn(usernameInput.getText(), passwordInput.getText());
        });

        createUserButton.setMnemonicParsing(false);
        createUserButton.setStyle("-fx-background-color: transparent;");
        createUserButton.setText("Criar Conta");
        createUserButton.setTextFill(javafx.scene.paint.Color.CADETBLUE);
        createUserButton.setCursor(Cursor.HAND);
        createUserButton.setOnMouseClicked(arg0 -> {
            ViewController.navigate(new SignInView());
        });

        vBox.getChildren().add(text);
        vBox.getChildren().add(usernameInput);
        vBox.getChildren().add(passwordInput);
        vBox.getChildren().add(loginButton);
        vBox.getChildren().add(createUserButton);
        getChildren().add(vBox);

        addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();
                Login.logIn(usernameInput.getText(), passwordInput.getText());
            }
        });
    }
}
