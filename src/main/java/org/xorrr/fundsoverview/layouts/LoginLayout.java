package org.xorrr.fundsoverview.layouts;

import org.xorrr.fundsoverview.login.User;
import org.xorrr.fundsoverview.login.UserService;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class LoginLayout extends VerticalLayout {
    private Button loginButton = new Button("Login");
    private TextField usernameField = new TextField("username:");
    private TextField passwordField = new TextField("password");

    private UserService userService;

    public void init() {
        addComponent(loginButton);
        addComponent(usernameField);
        addComponent(passwordField);
        loginButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                User user = userService.login(usernameField.getValue(),
                        passwordField.getValue());
                UI.getCurrent().setData(user);
            }
        });
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public TextField getPasswordField() {
        return passwordField;
    }
}
