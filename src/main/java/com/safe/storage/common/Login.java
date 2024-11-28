package com.safe.storage.common;

import java.util.List;
import java.util.Optional;

import com.safe.storage.daos.UserDao;
import com.safe.storage.models.User;
import com.safe.storage.security.Password;
import com.safe.storage.views.MenuView;

public class Login {
    private static boolean logged = false;

    private static User user;
    public static User getUser() {
        return user;
    }
    public static void setUser(User newUser) {
        user = newUser;
    }

    public static void logIn(String username, String password) {
        if (logged) return;
        
        List<User> users = new UserDao().searchBy("username", username);

        if (users.size() == 0) {
            ViewController.showAlert("Erro", "Username Incorreto");
            return;
        }

        User selectUser = users.get(0);

        boolean valid = Password.verifyPassword(password, selectUser.getPassword());

        if (!valid) {
            ViewController.showAlert("Erro", "Senha Incorreta");
            return;
        }

        user = selectUser;

        user.setPassword(password);

        if (!logged) {
            logged = true;
            
            System.out.println("Logado como " + user.getUsername() + " com id " + user.getId());
            
            ViewController.showAlert("Sucesso", "Logado com sucesso");
            ViewController.navigate(new MenuView());
        }
    }

    public static void signIn(String username, String password) {
        if (logged) return;

        List<User> users = new UserDao().searchBy("username", username);

        if (users.size() > 0) {
            ViewController.showAlert("Erro", "Este Username ja existe");
            return;
        }

        Optional<User> newUser = new UserDao().create(new User(0, username, Password.hashPassword(password)));

        if (!newUser.isPresent()) {
            ViewController.showAlert("Erro", "Algo deu errado");
            return;
        }

        user = newUser.get();

        user.setPassword(password);

        if (!logged) {
            logged = true;

            ViewController.navigate(new MenuView());

            System.out.println("Logado como " + user.getUsername() + " com id " + user.getId());

            ViewController.showAlert("Sucesso", "Conta criada com sucesso");
        }
    }

    public static void logOut() {
        user = null;
        logged = false;
    }
}
