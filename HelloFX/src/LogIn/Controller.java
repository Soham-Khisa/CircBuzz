package LogIn;

import Database.DatabaseConnection;
import JavaCode.Admin;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Controller {
    public PasswordField password;
    public TextField userID;

    public void logInAction(ActionEvent event)  {
        try {
            DatabaseConnection dc = new DatabaseConnection();
            if(userID.getText().isBlank()) {
                JOptionPane.showMessageDialog(null, "Try Again");
                return;
            }
            else if(password.getText().isBlank()) {
                JOptionPane.showMessageDialog(null, "Try Again");
                return;
            }
            int user = Integer.parseInt(userID.getText());
            boolean check = dc.logInCheck(user, password.getText());
            if (check) {
                Parent root = FXMLLoader.load(getClass().getResource("/frontPage/frontPage.fxml"));
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(new Scene(root));
                window.show();
            }
            else JOptionPane.showMessageDialog(null, "Try Again");

            dc.closeConnection();
        }
        catch (IOException e) {
            System.out.println("FXML failed to load in Log in :: " + e);
        }
        catch (SQLException e) {
            System.out.println("Connection failed to close :: " + e);
        }
        catch (NumberFormatException e) {
            System.out.println("invalid :: " + e);
            JOptionPane.showMessageDialog(null, "Try Again");
        }
    }
}
