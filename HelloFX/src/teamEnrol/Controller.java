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
    private List<Player> playerList;
    private List<Batsman> batsmanList;
    private List<Bowler> bowlerList;
    private List<Wicket_Keeper> wicketKeepers;
    private List<FileInputStream> playerInputStream;
    private final int numplayers = 11;
    private int counter = 11;
    private boolean teamok = false;
    private FileInputStream fin = null;
    private Image image = null;
    private Team team = null;
    private PlayerController pc = null;

    public void teamVerification(ActionEvent event) {

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
                        playerList = new ArrayList<Player>();
                        batsmanList = new ArrayList<Batsman>();
                        bowlerList = new ArrayList<Bowler>();
                        wicketKeepers = new ArrayList<Wicket_Keeper>();
                        playerInputStream = new ArrayList<FileInputStream>();
                        verificationmessage.setText("Your team has been verified successfully");
                        counter = numplayers;
                        showNumberofPlayer();
                    }
                }
                else if(headcoach.getText().isBlank() || boardpresident.getText().isBlank() || establishdate.getValue() == null)
                    JOptionPane.showMessageDialog(null, "insert all the required fields");
                else
                    JOptionPane.showMessageDialog(null, "This team name is taken");
            }
            catch (SQLException e) {
                System.out.println("Result is not returned, teamVerification :: " + e);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Insert a name for the team");
        }
    }

    public void teamConfirm(ActionEvent event) throws IOException {
        if(teamok==true && counter==0) {
            boolean result = team.insertTeam(fin);

            boolean pres=false, batres=false, wkres=false, bowlres=false;
            int size = playerList.size();
            for(int i=0; i<size; i++) {
                FileInputStream f = playerInputStream.get(i);
                Player player = playerList.get(i);
                String role = player.getRole();
                pres = player.insertPlayer(f);
                if (pres) {
                    Batsman batsman = batsmanList.get(i);
                    batres = batsman.insertBatsman();
                    if (batres) {
                        Bowler bowler = bowlerList.get(i);
                        bowlres = bowler.insertBowler();
                        if (bowlres && (role=="Wicket-keeper" || role=="Wicketkeeper-batsman")) {
                            Wicket_Keeper wicketKeeper = wicketKeepers.get(i);
                            wkres = wicketKeeper.insertWicketKeeper();
                        }
                    }
                }
                pres = false;
                batres = false;
                wkres = false;
                bowlres = false;
            }
            Parent root = FXMLLoader.load(getClass().getResource("/frontPage/frontPage.fxml"));
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(root));
            counter = numplayers;
            window.show();
        }
        else {
            JOptionPane.showMessageDialog(null, "Insert all the required info completely");
        }
    }

    public void addPlayerList(Player player) {
        playerList.add(player);
    }
    public void addBatsmanList(Batsman batsman) {
        batsmanList.add(batsman);
    }
    public void addBowlerList(Bowler bowler) {
        bowlerList.add(bowler);
    }
    public void addWicketkeeers(Wicket_Keeper wk) {
        wicketKeepers.add(wk);
    }
    public void addplayerInputStream(FileInputStream f) {
        playerInputStream.add(f);
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
                JOptionPane.showMessageDialog(null, "Team has not been verified successfully");
            } else {
                JOptionPane.showMessageDialog(null, "No player is to be added");
            }
        }
        catch (IOException e) {
            System.out.println("Failed to load teamEnrol\\Controller :: " + e);
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
        playernumbers.setText("Add exactly " + counter + " players");
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

        playernumbers.setText("Add exactly " + counter + " players");
    }
}
