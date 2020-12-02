package UpdateTables.StadiumUpdate;

import Database.DatabaseConnection;
import JavaCode.Stadium;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class StadiumUpdateFinalController {
    @FXML
    private Button button;
    @FXML
    private TextField name;
    @FXML
    private TextField loca;
    @FXML
    private TextField country;
    @FXML
    private TextField capacty;
    @FXML
    private Label origname;
    @FXML
    private Label origlocation;
    @FXML
    private Label origcountry;
    @FXML
    private Label origcapacity;
    @FXML
    private Label updatemsg;

    private Stadium stadium = null;

    public void setStadium(Stadium stadium) {
        this.stadium = stadium;
    }

    public void setStadiumUpdateFinal() {
        if(stadium == null) return;
        name.setText(stadium.getStadium_name());
        country.setText(stadium.getCountry());
        Integer cap = stadium.getCapacity();
        capacty.setText(cap.toString());
        loca.setText(stadium.getLocation());
    }

    @FXML
    void backToStadiumUpdate(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/UpdateTables/StadiumUpdate/StadiumUpdate.fxml"));
            Stage window= (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(root));
            window.show();
        } catch (IOException e) {
            System.out.println("Failed to load stadiumUpdate StadiumUpdateFinalController :: " + e);;
        }
    }

    @FXML
    void showOrhideOriginal(ActionEvent event) {
        if(stadium == null) return;

        if(button.getText().equals("SHOW ORIGINALS")) {
            button.setText("HIDE ORIGINALS");
            origname.setText(stadium.getStadium_name());
            origlocation.setText(stadium.getLocation());
            origcountry.setText(stadium.getCountry());
            Integer cap = stadium.getCapacity();
            origcapacity.setText(cap.toString());
        }
        else {
            button.setText("SHOW ORIGINALS");
            origname.setText("");
            origlocation.setText("");
            origcountry.setText("");
            origcapacity.setText("");
        }
    }

    @FXML
    void updateClick(ActionEvent event) {
        if(name.getText().isBlank() || loca.getText().isBlank() || country.getText().isBlank() || capacty.getText().isBlank()) {
            updatemsg.setText("");
            JOptionPane.showMessageDialog(null, "Please insert all the fields");
            return;
        }
        else if(!isNumeric(capacty.getText())) {
            updatemsg.setText("");
            JOptionPane.showMessageDialog(null, "Capacity must be numeric");
            return;
        }

        DatabaseConnection dc = new DatabaseConnection();
        String query = "UPDATE CRICBUZZ.STADIUM SET "+
                "STADIUM_NAME = '" + name.getText() + "', LOCATION = '" + loca.getText() +
                "', COUNTRY = '" + country.getText() + "', CAPACITY = " + Integer.parseInt(capacty.getText()) +
                " WHERE STADIUM_ID = " + stadium.getStadiumID();

        boolean verdict = dc.doUpdate(query);
        if(verdict) {
            stadium.setStadium_name(name.getText());
            stadium.setLocation(loca.getText());
            stadium.setCountry(country.getText());
            stadium.setCapacity(Integer.parseInt(capacty.getText()));
            updatemsg.setText("TEAM UPDATE SUCCESSFUL");

            if(button.getText().equals("HIDE ORIGINALS")) {
                origname.setText(stadium.getStadium_name());
                origlocation.setText(stadium.getLocation());
                origcountry.setText(stadium.getCountry());
                Integer cap = stadium.getCapacity();
                origcapacity.setText(cap.toString());
            }
        }
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
