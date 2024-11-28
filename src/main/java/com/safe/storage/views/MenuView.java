package com.safe.storage.views;

import com.safe.storage.common.ViewController;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MenuView extends FlowPane {

    protected final VBox vBox;
    protected final Text text;
    protected final Button safaPassowordButton;
    protected final Button safeFileButton;

    final BooleanProperty firstTime = new SimpleBooleanProperty(true); // Variable to store the focus on stage load

    public MenuView() {
        vBox = new VBox();
        text = new Text();
        safaPassowordButton = new Button();
        safeFileButton = new Button();
        
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
        text.setText("Pressione ESC para voltar");
        text.setFont(new Font(24.0));


        safaPassowordButton.setMnemonicParsing(false);
        safaPassowordButton.setStyle("-fx-background-color: rgb(84, 113, 255);");
        safaPassowordButton.setText("Gerenciar Senhas");
        safaPassowordButton.setTextFill(javafx.scene.paint.Color.WHITE);
        safaPassowordButton.setPadding(new Insets(6.0, 30.0, 6.0, 30.0));
        safaPassowordButton.setCursor(Cursor.HAND);
        safaPassowordButton.setOnMouseClicked(arg0 -> {
            ViewController.navigate(new SafePasswordView());
        });

        safeFileButton.setMnemonicParsing(false);
        safeFileButton.setStyle("-fx-background-color: #2196F3;");
        safeFileButton.setText("Gerenciar Arquivos");
        safeFileButton.setTextFill(javafx.scene.paint.Color.WHITE);
        safeFileButton.setPadding(new Insets(6.0, 30.0, 6.0, 30.0));
        safeFileButton.setCursor(Cursor.HAND);
        safeFileButton.setOnMouseClicked(arg0 -> {
            ViewController.navigate(new SafeFileView());
        });

        vBox.getChildren().add(text);
        vBox.getChildren().add(safaPassowordButton);
        vBox.getChildren().add(safeFileButton);
        getChildren().add(vBox);
    }
}
