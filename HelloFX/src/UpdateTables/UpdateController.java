package UpdateTables;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UpdateController {
    public void backToFront(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/frontPage/frontPage.fxml"));
            Stage window= (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(root));
            window.show();
        } catch (IOException e) {
            System.out.println("Failed to load backToFront UpdateTables\\UpdateController :: " + e);;
        }
    }

    public void teamUpdate(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/UpdateTables/TeamUpdate/TeamUpdate.fxml"));
            Stage window= (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(root));
            window.show();
        } catch (IOException e) {
            System.out.println("Failed to load teamUpdate UpdateTables\\UpdateController :: " + e);;
        }
    }

    public void playerUpdate(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/UpdateTables/PlayerUpdate/PlayerUpdate.fxml"));
            Stage window= (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(root));
            window.show();
        } catch (IOException e) {
            System.out.println("Failed to load playerUpdate UpdateTables\\UpdateController :: " + e);;
        }
    }

    public void stadiumUpdate(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/UpdateTables/StadiumUpdate/StadiumUpdate.fxml"));
            Stage window= (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(root));
            window.show();
        } catch (IOException e) {
            System.out.println("Failed to load stadiumUpdate UpdateTables\\UpdateController :: " + e);;
        }
    }

    public void tourRescheduleUpdate(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/UpdateTables/RescheduleTour/RescheduleTour.fxml"));
            Stage window= (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(root));
            window.show();
        } catch (IOException e) {
            System.out.println("Failed to load tourRescheduleUpdate UpdateTables\\UpdateController :: " + e);;
        }
    }

    public void umpireUpdate(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/UpdateTables/UmpireUpdate/UmpireUpdate.fxml"));
            Stage window= (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(root));
            window.show();
        } catch (IOException e) {
            System.out.println("Failed to load umpireUpdate UpdateTables\\UpdateController :: " + e);;
        }
    }
}
