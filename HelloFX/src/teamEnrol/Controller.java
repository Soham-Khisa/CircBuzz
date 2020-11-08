/*
 * Author MD. Sakibur Reza
 */

package teamEnrol;

import Database.DatabaseConnection;
import JavaCode.*;
import PlayerEnrol.PlayerController;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TextField teamName;
    @FXML
    private TextField headcoach;
    @FXML
    private TextField boardpresident;
    @FXML
    private DatePicker establishdate;
    @FXML
    private Label verificationmessage;
    @FXML
    private Label playernumbers;
    @FXML
    private ImageView teamlogo;

    private List<String> filelist;
    private final int numplayers = 11;
    private int counter = 11;
    private boolean teamok = false;
    private FileInputStream fin = null;
    private Image image = null;
    private Team team = null;
    private PlayerController pc = null;

    public void teamConfirm(ActionEvent event) {

        DatabaseConnection dc = new DatabaseConnection();

        if(!teamName.getText().isBlank()) {
            String t = teamName.getText();
            String q = "SELECT * FROM CRICBUZZ.TEAM WHERE TEAM_NAME = " + "'" + t + "'";
            ResultSet rs = dc.getQueryResult(q);
            try {
                if(!rs.isBeforeFirst()) {
                    verificationmessage.setText("Team Name is currently available.");
                    if(headcoach.getText().isBlank() || boardpresident.getText().isBlank() || establishdate.getValue() == null)
                        JOptionPane.showMessageDialog(null, "insert all the required fields");
                    else {
                        teamok = true;
                        //Have to write the table insert code in this block;

                        String primarykeyquery = "SELECT MAX(TEAM_ID) as maxteamid FROM CRICBUZZ.TEAM";
                        rs = dc.getQueryResult(primarykeyquery);
                        int primarykey=1;
                        if(rs.next()) {
                            primarykey = rs.getInt("maxteamid") + 1;
                        }
                        //From datepicker -> java.util.date
                        java.util.Date date = java.util.Date.from(establishdate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());


                        team = new Team(primarykey, teamName.getText(), date, headcoach.getText(), boardpresident.getText());
                        team.insertTeam(fin);
                        verificationmessage.setText("Your team is confirmed");
                        showNumberofPlayer();
                    }
                }
                else if(headcoach.getText().isBlank() || boardpresident.getText().isBlank() || establishdate.getValue() == null) {
                    teamok = false;
                    verificationmessage.setText("");
                    JOptionPane.showMessageDialog(null, "insert all the required fields");
                }
                else {
                    String query = "SELECT * FROM CRICBUZZ.TEAM WHERE TEAM_ID = (SELECT MAX(TEAM_ID) AS TEAM_ID FROM CRICBUZZ.TEAM)";
                    rs = dc.getQueryResult(query);
                    if(rs.next() && team!=null) {
                        String name = rs.getString("TEAM_NAME");
                        String coach = rs.getString("HEAD_COACH");
                        String board = rs.getString("BOARD_PRESIDENT");
                        java.sql.Date date = rs.getDate("ESTABLISH_DATE");
                        java.util.Date edate = java.util.Date.from(establishdate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                        java.sql.Date sqlDate = new java.sql.Date(edate.getTime());
                        if(name.equals(teamName.getText()) && coach.equals(headcoach.getText()) && board.equals(boardpresident.getText()) && sqlDate.equals(edate)) {
                            teamok = true;
                            verificationmessage.setText("Your team has been confirmed");
                        }
                        else {
                            teamok = false;
                            verificationmessage.setText("");
                            JOptionPane.showMessageDialog(null, "This team is not available currently");
                        }
                    }
                    else {
                        teamok = false;
                        verificationmessage.setText("");
                        JOptionPane.showMessageDialog(null, "This team name is already taken, try a new one");
                    }
                }
            }
            catch (SQLException e) {
                System.out.println("Result is not returned, teamVerification :: " + e);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Insert a name for the team");
        }
    }


    public void teamAddPlayer(ActionEvent event) {
        try {
            if (teamok == true && counter > 0) {
                //Do add player
                FXMLLoader loader = new FXMLLoader();
                Parent p = loader.load(getClass().getResource("/PlayerEnrol/Player.fxml").openStream());
                pc = (PlayerController) loader.getController();    //loading the current playercontroller instance
                Stage substage = new Stage();
                Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                substage.initOwner(stage);
                substage.initModality(Modality.WINDOW_MODAL);
                substage.setScene(new Scene(p));
                pc.setPlayerTeam(team);                                                 //sending the team instance to the playercontrol instance
                pc.setTeamEnrolController(this);
                substage.show();
            } else if (teamok == false) {
                JOptionPane.showMessageDialog(null, "Team has not been confirmed yet");
            } else {
                JOptionPane.showMessageDialog(null, "No player is to be added");
            }
        }
        catch (IOException e) {
            System.out.println("Failed to load teamEnrol\\Controller :: " + e);
        }
    }

    @FXML
    public void teamEnrolDone(ActionEvent event) throws IOException {
        if(teamok) {
            Parent root = FXMLLoader.load(getClass().getResource("/frontPage/frontPage.fxml"));
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(root));
            window.show();
        }
        else {
            JOptionPane.showMessageDialog(null, "Please confirm the team first");
        }
    }

    @FXML
    public void imageChooserAction(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image files", filelist));
        File file = fc.showOpenDialog(null);
        if(file != null) {
            try {
                fin = new FileInputStream(file.getAbsolutePath());
            } catch (FileNotFoundException e) {
                System.out.println("file not found teamEnrol\\imageChooserAction :: " + e);
            }
            image = new Image(file.toURI().toString());
            teamlogo = new ImageView(image);
        }
    }

    public void reduceCounter() {
        if(counter>0)
            counter -= 1;
    }

    public void showNumberofPlayer() {
        playernumbers.setText("Add " + counter + " players (Optional)");
    }

    public void backtoFront(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/frontPage/frontPage.fxml"));
        Stage window= (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root));
        window.show();
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

        playernumbers.setText("Add " + counter + " players (Optional)");
    }
}
