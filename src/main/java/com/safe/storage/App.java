package com.safe.storage;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import com.safe.storage.common.ViewController;
import com.safe.storage.daos.GenericItemDao;
import com.safe.storage.daos.SafeFileDao;
import com.safe.storage.daos.SafePasswordDao;
import com.safe.storage.daos.UserDao;
import com.safe.storage.database.DBConnection;
import com.safe.storage.database.SQLiteConnection;
import com.safe.storage.views.LoginView;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        boolean DBReady = DBSetUp();

        System.out.println("DataBase Ready -> " + DBReady);

        ViewController.setStage(stage);

        Parent initialScreen = new LoginView(); // Cria a visualização inicial (HomeView)

        Scene mainScene = new Scene(initialScreen, 1080, 720); // Cria a cena principal

        // Adiciona um filtro para eventos de teclado
        mainScene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) { // Verifica se a tecla F2 foi pressionada
                // ContainerComponent productManager = new ContainerComponent() {
                //     {
                //         body.getChildren().add(new ReturnBarComponent()); // Adiciona a barra de retorno
                //         body.getChildren().add(new MenuView()); // Adiciona a visualização de gerenciamento de produtos
                //     }
                // };

                // event.consume(); // Consome o evento para evitar propagação

                // viewController.navigate(productManager); // Navega para a nova visualização
                ViewController.navigateBack();
            }
        });

        stage.setScene(mainScene); 

        stage.getIcons().add(new Image("/safe.png")); 
        stage.setTitle("Safe Storage"); 
        stage.show(); 
        stage.centerOnScreen(); // Centraliza o palco na tela

        stage.setOnCloseRequest(arg0 -> {
            DBConnection.disconnect();
        });
    }

    /**
     * Método principal que inicia a aplicação.
     * 
     * @param args Argumentos da linha de comando.
     */
    public static boolean DBSetUp() {
        DBConnection.setInstance(new SQLiteConnection());

        DBConnection.connect();

        // new UserDao().purgeDB();
        // new SafePasswordDao().purgeDB();
        // new SafeFileDao().purgeDB();
        // new GenericItemDao().purgeDB();

        return 
            new UserDao().setUp() &&
            new SafePasswordDao().setUp() &&
            new SafeFileDao().setUp() &&
            new GenericItemDao().setUp()
        ;
    }

    public static void main(String[] args) {
        Application.launch(App.class, args); // Lança a aplicação
    }
}
