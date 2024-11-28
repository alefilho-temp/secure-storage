package com.safe.storage.common;

import java.util.ArrayList;

import com.safe.storage.views.LoginView;
import com.safe.storage.views.SignInView;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * Classe responsável pelo controle de navegação de visualizações em uma aplicação JavaFX.
 */
public class ViewController {
    private static Stage stage;
    public static void setStage(Stage newStage) {
        stage = newStage;
        history = new ArrayList<Parent>();
    }

    private static ArrayList<Parent> history;

    /**
     * Navega para uma nova visualização.
     *
     * Este método navega para a visualização especificada, armazenando a visualização atual na história.
     *
     * @param root o nó raiz da nova visualização
     */
    public static void navigate(Parent root) {
        navigate(root, false);
    }

    /**
     * Navega para uma nova visualização, indicando se está voltando ou não.
     *
     * Este método navega para a visualização especificada. Se o parâmetro back for verdadeiro,
     * a visualização atual não será adicionada à história.
     *
     * @param root o nó raiz da nova visualização
     * @param back indica se a navegação é uma ação de voltar
     */
    public static void navigate(Parent root, boolean back) {
        if (root == stage.getScene().getRoot()) return;

        if (!back) {
            history.add(stage.getScene().getRoot());
            // showAlert("Navegação", "Navegando para uma nova visualização.");
        }
        stage.getScene().setRoot(root);
        System.out.println("Navegando");
    }

    /**
     * Volta para a visualização anterior, se houver.
     *
     * Este método navega de volta para a última visualização armazenada na história.
     * Se não houver visualizações anteriores, uma mensagem será exibida.
     */
    public static void navigateBack() {
        if (history.size() > 0) {
            if (
                history.get(history.size() - 1).getClass() == (new LoginView()).getClass() ||
                history.get(history.size() - 1).getClass() == (new SignInView()).getClass()
            ) {
                Login.logOut();
            }
            navigate(history.remove(history.size() - 1), true);
            // showAlert("Navegação", "Voltando para a visualização anterior.");
            System.out.println("Voltando");
        } else {
            showAlert("Erro", "Não tem pra onde voltar. Voltando.");
            System.out.println("Não tem pra onde voltar. Voltando");
        }
    }

    /**
     * Retorna a janela principal da aplicação.
     *
     * @return o objeto Stage que representa a janela principal
     */
    public static Stage getStage() {
        return stage;
    }

    /**
     * Exibe um alerta com uma mensagem.
     *
     * @param title o título do alerta
     * @param message a mensagem a ser exibida no alerta
     */
    public static void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
