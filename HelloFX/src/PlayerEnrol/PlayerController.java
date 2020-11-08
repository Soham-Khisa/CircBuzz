/*
 * Author MD. Sakibur Reza
 */

package PlayerEnrol;

import Database.DatabaseConnection;
import JavaCode.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import teamEnrol.Controller;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class PlayerController implements Initializable {
    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private TextField jersey;
    @FXML
    private TextField birthplace;
    @FXML
    private DatePicker dob;
    @FXML
    private TextField role;
    @FXML
    private TextField batting;
    @FXML
    private TextField bowling;
    @FXML
    private ImageView playerimage;

    private FileInputStream fin = null;
    private List<String> filelist;
    private Team team = null;
    private Player player = null;
    private Batsman batsman = null;
    private Bowler bowler = null;
    private Wicket_Keeper wicketKeeper = null;
    private Controller teamenrolctrl = null;

    public void playerConfirm(ActionEvent event) {
        try {
            boolean pres = false, bowlres = false, batres = false, wkres = false;

            if (firstname.getText().isBlank() || lastname.getText().isBlank() || jersey.getText().isBlank() ||
                    birthplace.getText().isBlank() || dob.getValue() == null || role.getText().isBlank() ||
                    batting.getText().isBlank() || bowling.getText().isBlank()) {

                JOptionPane.showMessageDialog(null, "Insert all the required info completely");
            } else if (team != null) {
                DatabaseConnection dc = new DatabaseConnection();
                int jnum = Integer.parseInt(jersey.getText());
                String primarykeyquery = "SELECT MAX(PLAYER_ID) as maxpid FROM PLAYER";
                ResultSet rs = dc.getQueryResult(primarykeyquery);
                int primarykey = 1;
                if (rs.next())
                    primarykey = rs.getInt("maxpid") + 1;

                dc.closeConnection();
                //From datepicker -> java.util.date
                java.util.Date date = java.util.Date.from(dob.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                player = new Player(primarykey, firstname.getText(), lastname.getText(), birthplace.getText(), date, role.getText(), team.getTeam_ID(), jnum);
                batsman = new Batsman(primarykey, batting.getText());
                bowler = new Bowler(primarykey, bowling.getText());
                wicketKeeper = new Wicket_Keeper(primarykey);

                pres = true;
                batres = true;
                bowlres = true;

                teamenrolctrl.addPlayerList(player);
                teamenrolctrl.addBatsmanList(batsman);
                teamenrolctrl.addBowlerList(bowler);
                teamenrolctrl.addWicketkeeers(wicketKeeper);
                teamenrolctrl.addplayerInputStream(fin);

            } else {
                JOptionPane.showMessageDialog(null, "Failed to insert player. A team must be assigned for the player at first.");
            }

            if (pres == true && batres == true && bowlres == true) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
                teamenrolctrl.reduceCounter();
                teamenrolctrl.showNumberofPlayer();
            } else
                JOptionPane.showMessageDialog(null, "Player addition failed. Try again");
        }
        catch (SQLException e) {
            System.out.println("Query filed to execute PlayerEnrol\\PlayerController :: " + e);;
        }

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
            playerimage = new ImageView(image);
        }
    }

    public void setPlayerTeam(Team team) {
        this.team = team;
    }

    public void setTeamEnrolController(Controller teamenrolctrl) {
        this.teamenrolctrl = teamenrolctrl;
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
    }
}
