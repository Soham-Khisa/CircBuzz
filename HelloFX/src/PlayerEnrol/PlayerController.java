/*
 * Author Soham Khisa
 * Author MD. Sakibur Reza
 */

package PlayerEnrol;

import Database.DatabaseConnection;
import JavaCode.*;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import teamEnrol.Controller;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PlayerController implements Initializable {
    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private TextField birthplace;
    @FXML
    private DatePicker dob;
    @FXML
    private ComboBox<String> role;
    @FXML
    private ComboBox<String> batting;
    @FXML
    private ComboBox<String> bowling;
    @FXML
    private ComboBox<String> jersey;
    @FXML
    private ImageView playerimage;

    private FileInputStream fin = null;
    private List<String> filelist;
    private List<String> battingStyle;
    private List<String> bowlingStyle;
    private List<String> playerRole;
    private List<String> jerseyNo;
    private Team team = null;
    private Player player = null;
    private Batsman batsman = null;
    private Bowler bowler = null;
    private Wicket_Keeper wicketKeeper = null;
    private Controller teamenrolctrl = null;

    public void playerConfirm(ActionEvent event) {
        boolean pres = false, bowlres = false, batres = false, wkres = false;

        if (firstname.getText().isBlank() || lastname.getText().isBlank() || jersey.getValue()==null ||
                birthplace.getText().isBlank() || dob.getValue() == null || role.getValue()==null ||
                batting.getValue()==null) {

            JOptionPane.showMessageDialog(null, "Insert all the required info completely");
        } else if (team != null) {
            int jnum = Integer.parseInt(jersey.getValue());
            int primarykey = 1;

            //From datepicker -> java.util.date
            java.util.Date date = java.util.Date.from(dob.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            player = new Player(firstname.getText(), lastname.getText(), birthplace.getText(), date, role.getValue(), team.getTeam_ID(), jnum);
            pres = player.insertPlayer(fin);
            if(pres) {
                primarykey = player.getPlayer_ID();
                for(int i=1; i<=3; i++) {
                    batsman = new Batsman(primarykey, i, batting.getValue());
                    batres = batsman.insertBatsman();
                }

                if(role.getValue().equals("Bowler") || role.getValue().equals("All-rounder") || bowling.getValue()!=null) {
                    for(int i=1; i<=3; i++) {
                        if (bowling.getValue() == null)
                            bowler = new Bowler(primarykey, i);
                        else
                            bowler = new Bowler(primarykey, i, bowling.getValue());
                        bowlres = bowler.insertBowler();
                    }
                }
                else if(role.getValue().equals("Wicket-keeper") || role.getValue().equals("Wicketkeeper-batsman")) {
                    for(int i=1; i<=3; i++) {
                        wicketKeeper = new Wicket_Keeper(primarykey, i);
                        wkres = wicketKeeper.insertWicketKeeper();
                    }
                }
            }
            else JOptionPane.showMessageDialog(null, "Failed to insert player");
        }
        else
            JOptionPane.showMessageDialog(null, "Failed to insert player. A team must be assigned for the player at first.");

        if (pres == true && batres == true) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
            if(teamenrolctrl != null) {
                teamenrolctrl.reduceCounter();
                teamenrolctrl.showNumberofPlayer();
            }
        } else JOptionPane.showMessageDialog(null, "Player addition failed. Try again");
    }

    @FXML
    public void playerImageChooser(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image files", filelist));
        File file = fc.showOpenDialog(null);
        if(file != null) {
            try {
                fin = new FileInputStream(file.getAbsolutePath());
            } catch (FileNotFoundException e) {
                System.out.println("file not found PlayerEnrol\\imageChooserAction :: " + e);
            }
            Image image = new Image(file.toURI().toString());
            playerimage.setImage(image);
        }
    }

    public void setPlayerTeam(Team team) {
        this.team = team;
    }

    public void setTeamEnrolController(Controller teamenrolctrl) {
        this.teamenrolctrl = teamenrolctrl;
    }

    public void setAvailableJersey() {
        if(team == null)    return;
        String jerseyQuery = "SELECT JERSEY FROM CRICBUZZ.PLAYER WHERE TEAM_ID = " + team.getTeam_ID();
        DatabaseConnection dc = new DatabaseConnection();
        ResultSet rs = dc.getQueryResult(jerseyQuery);

        jerseyNo = new ArrayList<>();
        for(int i=1; i<=100; i++) {
            Integer integer = i;
            jerseyNo.add(integer.toString());
        }
        try {
            while (rs.next()) {
                Integer integer = rs.getInt("JERSEY");
                jerseyNo.remove(integer.toString());
            }
        }
        catch (SQLException e) {
            System.out.println("Failed to add available jerseys. :: " + e);
        }

        ObservableList<String> observeJersey = FXCollections.observableArrayList(jerseyNo);
        jersey.setItems(observeJersey);
    }

    @Override
    public void initialize (URL url, ResourceBundle rb) {
        filelist = new ArrayList<>();
        filelist.add("*.png");
        filelist.add("*.jpg");
        filelist.add("*.JPG");
        filelist.add("*.PNG");
        filelist.add("*.jpeg");
        filelist.add("*.JPEG");

        battingStyle = new ArrayList<>();
        battingStyle.add("Right-handed");
        battingStyle.add("Left-handed");

        bowlingStyle = List.of("Right-arm fast", "Right-arm fast-medium", "Right-arm medium-fast", "Right-arm medium", "Right-arm medium-slow",
                "Right-arm slow-medium", "Right-arm slow", "Left-arm fast", "Left-arm fast-medium",
                "Left-arm medium-fast", "Left-arm medium", "Left-arm medium-slow",
                "Left-arm slow-medium", "Left-arm slow", "Right-arm off break",
                "Right-arm leg break", "Right-arm leg break googly", "Right-arm leg spin",
                "Slow left-arm orthodox", "Slow left-arm wrist spin", "Left-arm googly", "Left-arm leg spin");

        playerRole = List.of("Wicket-keeper", "Batsman", "Bowler", "All-rounder", "Wicketkeeper-batsman");

        ObservableList<String> observeBatting = FXCollections.observableArrayList(battingStyle);
        batting.setItems(observeBatting);
        ObservableList<String> observeBowling = FXCollections.observableArrayList(bowlingStyle);
        bowling.setItems(observeBowling);
        ObservableList<String> observeRole = FXCollections.observableArrayList(playerRole);
        role.setItems(observeRole);

        Callback<DatePicker, DateCell> callB = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        setDisable(empty || item.compareTo(LocalDate.now())>0 || item.compareTo(LocalDate.now().minusYears(14))>0);
                    }

                };
            }
        };
        dob.setDayCellFactory(callB);
    }
}
