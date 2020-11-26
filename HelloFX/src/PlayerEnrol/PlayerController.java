/*
 * Author Soham Khisa
 * Author MD. Sakibur Reza
 */

package PlayerEnrol;

import Database.DatabaseConnection;
import JavaCode.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
        boolean pres = false, bowlres = false, batres = false, wkres = false;

        if (firstname.getText().isBlank() || lastname.getText().isBlank() || jersey.getText().isBlank() ||
                birthplace.getText().isBlank() || dob.getValue() == null || role.getText().isBlank() ||
                batting.getText().isBlank()) {

            JOptionPane.showMessageDialog(null, "Insert all the required info completely");
        } else if (team != null) {
            int jnum = Integer.parseInt(jersey.getText());
            int primarykey = 1;

            //From datepicker -> java.util.date
            java.util.Date date = java.util.Date.from(dob.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            player = new Player(firstname.getText(), lastname.getText(), birthplace.getText(), date, role.getText(), team.getTeam_ID(), jnum);
            pres = player.insertPlayer(fin);
            if(pres) {
                primarykey = player.getPlayer_ID();
                batsman = new Batsman(primarykey, batting.getText());

                if (bowling.getText().isBlank())
                    bowler = new Bowler(primarykey);
                else
                    bowler = new Bowler(primarykey, bowling.getText());
                wicketKeeper = new Wicket_Keeper(primarykey);

                batres = batsman.insertBatsman();
                bowlres = bowler.insertBowler();
                wicketKeeper.insertWicketKeeper();
            }
            else JOptionPane.showMessageDialog(null, "Failed to insert player");
        }
        else
            JOptionPane.showMessageDialog(null, "Failed to insert player. A team must be assigned for the player at first.");

        if (pres == true && batres == true && bowlres == true) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
            teamenrolctrl.reduceCounter();
            teamenrolctrl.showNumberofPlayer();
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
