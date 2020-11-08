package startGame;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    public void startGameAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/runningMatch/runningMatch.fxml"));
        Stage window= (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root, 660, 580));
        window.show();
    }

    public void debutDoneAction(ActionEvent event) {
    }

    public void debut1Action(ActionEvent event) {
    }

    public void debut2Action(ActionEvent event) {
    }
}
