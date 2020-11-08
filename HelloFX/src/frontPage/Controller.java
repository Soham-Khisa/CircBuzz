package frontPage;

import Database.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class Controller<initialize> {
    @FXML
    private TextField umpireFirstName;
    @FXML
    private TextField umpireCountry;
    @FXML
    private TextField umpireLastName;
    @FXML
    private DatePicker umpire_dob;
    @FXML
    private TextField stadiumName;
    @FXML
    private TextField stadiumLocation;
    @FXML
    private TextField stadiumCountry;
    @FXML
    private TextField seatNoStadium;
    @FXML
    private Button enrolStadium;
    @FXML
    private Button enrolUmpire;
    @FXML
    private Label stadiumlabel;
    @FXML
    private Label umpirelabel;


    public void makeSeriesAction(ActionEvent event) {
    }

    public void umpireEnrolAction(ActionEvent event) {
        try {
            if (umpireFirstName.getText() != null && umpireLastName.getText() != null && umpireCountry.getText() != null && umpire_dob.getValue() != null) {
                DatabaseConnection dc = new DatabaseConnection();

                int primarkey = 1;
                String query = "SELECT MAX(UMPIRE_ID) AS umpid FROM CRICBUZZ.UMPIRE";
                ResultSet rs = dc.getQueryResult(query);
                if (rs.next())
                    primarkey = rs.getInt("umpid") + 1;

                String date = umpire_dob.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                query = "INSERT INTO CRICBUZZ.UMPIRE (UMPIRE_ID, FIRST_NAME, LAST_NAME, COUNTRY, DOB) " +
                        "VALUES (" + primarkey + ", '" + umpireFirstName.getText() + "', '" + umpireLastName.getText() + "', '" + umpireCountry.getText() + "', TO_DATE('" + date + "', 'dd/MM/yyyy'))";
                Boolean verdict = dc.doUpate(query);
                if (verdict) umpirelabel.setText("Umpire enrollment is successful");
                else umpirelabel.setText("Umpire enrollment failed. Try again");
            }
            else {
                JOptionPane.showMessageDialog(null, "Umpire enrollment failed");
            }
        } catch (SQLException e) {
            System.out.println("Failed to return result umpireEnrolAction\\frontPage\\Controller :: " + e);
        }
    }

    public void makeTourAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/makeTour/makeTour.fxml"));
        Stage window= (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root, 660, 586));
        window.show();
    }


    public void enrolStadiumAction(ActionEvent event) throws SQLException {
        try {
            if (stadiumName.getText() != null && stadiumLocation.getText() != null && stadiumCountry.getText() != null && seatNoStadium.getText() != null) {
                int seatNum = Integer.parseInt(seatNoStadium.getText());
                DatabaseConnection dc = new DatabaseConnection();

                int primarkey = 1;
                String query = "SELECT MAX(STADIUM_ID) AS stdmid FROM CRICBUZZ.STADIUM";
                ResultSet rs = dc.getQueryResult(query);
                if (rs.next())
                    primarkey = rs.getInt("stdmid") + 1;

                query = "INSERT INTO CRICBUZZ.STADIUM (STADIUM_ID, STADIUM_NAME, LOCATION, COUNTRY, CAPACITY) " +
                        "VALUES (" + primarkey + ", '" + stadiumName.getText() + "', '" + stadiumLocation.getText() + "', '" + stadiumCountry.getText() + "', " + seatNum + ")";
                Boolean verdict = dc.doUpate(query);
                if (verdict) stadiumlabel.setText("Stadium enrollment is successful");
                else stadiumlabel.setText("Stadium enrollment failed. Try again");
            } else {
                JOptionPane.showMessageDialog(null, "Insert all the fields");
            }
        }
        catch (SQLException e) {
            System.out.println("Failed to return result enrolStadiumAction :: " + e);
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Insert all the fields");
        }
    }

    public void enrolAnUmpireAction(ActionEvent event) {
        if(!umpireFirstName.isVisible() && !umpireLastName.isVisible() && !umpireCountry.isVisible() && !umpire_dob.isVisible() && !enrolUmpire.isVisible()) {
            umpireFirstName.setVisible(true);
            umpireCountry.setVisible(true);
            umpireLastName.setVisible(true);
            umpire_dob.setVisible(true);
            enrolUmpire.setVisible(true);
        }
        else {
            umpireFirstName.setVisible(false);
            umpireCountry.setVisible(false);
            umpireLastName.setVisible(false);
            umpire_dob.setVisible(false);
            enrolUmpire.setVisible(false);
        }
    }

    public void enrolAteamAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/teamEnrol/teamEnrol.fxml"));
        Stage window= (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root));
        window.show();
    }

    public void enrolAstadiumAction(ActionEvent event) {
        if(stadiumName.isVisible() && stadiumLocation.isVisible() && stadiumCountry.isVisible() && seatNoStadium.isVisible() && enrolStadium.isVisible()) {
            stadiumName.setVisible(false);
            stadiumLocation.setVisible(false);
            stadiumCountry.setVisible(false);
            seatNoStadium.setVisible(false);
            enrolStadium.setVisible(false);
        }
        else {
            stadiumName.setVisible(true);
            stadiumLocation.setVisible(true);
            stadiumCountry.setVisible(true);
            seatNoStadium.setVisible(true);
            enrolStadium.setVisible(true);
        }
    }

    public void makeWCaction(ActionEvent event) {
    }

    public void playGame1Action(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/startGame/startGame.fxml"));
        Stage window= (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root, 660, 580));
        window.show();
    }

    public void playGame2Action(ActionEvent event) {
    }

    public void playGame3Action(ActionEvent event) {
    }
}