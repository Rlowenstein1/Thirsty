package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lib.Debug;
import model.Authenticator;
import model.User;

public class LoginScreenController implements Initializable {

    private Stage stage;
    private boolean authed = false;

    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField pwField;
    
    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = pwField.getText();
        Debug.debug("Verifying user credentials: \"%s\":\"%s\"", username, password);
        User authenticated = Authenticator.authenticate(username, password);
        if (authenticated != null) {
            Debug.debug("User authentication successful!");
            authed = true;
            stage.close();
        } else {
            Debug.debug("User authentication failed!");
        }
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public boolean loginSuccessful() {
        return (authed);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
}
