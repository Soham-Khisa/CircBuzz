/*
 * Author Soham Khisa
 * Author MD. Sakibur Reza
 */

package teamEnrol;

import Database.DatabaseConnection;
import JavaCode.*;
import PlayerEnrol.PlayerController;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.transform.Result;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TextField teamName;
    @FXML
    private TextField teamshort;
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
    private Button imageChooser;
    @FXML
    private ImageView teamlogo;
    @FXML
    private Button confirmteam;

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
        if(teamName.getText().isBlank() || teamshort.getText().isBlank() || headcoach.getText().isBlank() || boardpresident.getText().isBlank() || establishdate.getValue() == null)
            JOptionPane.showMessageDialog(null, "insert all the required fields");
        else {
            teamok = true;
            //Have to write the table insert code in this block;

            //From datepicker -> java.util.date
            java.util.Date date = java.util.Date.from(establishdate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

            team = new Team(teamName.getText(), teamshort.getText(), date, headcoach.getText(), boardpresident.getText());
            boolean verdict = team.insertTeam(fin);
            if(verdict) {
                verificationmessage.setText("Your team is confirmed");
                teamName.setDisable(true);
                headcoach.setDisable(true);
                boardpresident.setDisable(true);
                establishdate.setDisable(true);
                imageChooser.setDisable(true);
                confirmteam.setDisable(true);
                teamshort.setDisable(true);
            }
            else
                JOptionPane.showMessageDialog(null, "The team is not available");

            showNumberofPlayer();
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

                BufferedImage bufferedImage = ImageIO.read(file);
                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                teamlogo.setImage(image);
            } catch (FileNotFoundException e) {
                System.out.println("file not found teamEnrol\\imageChooserAction :: " + e);
            } catch (IOException e) {
                System.out.println("failed to read the image to bufferedImage :: " + e);
            }
            //image = new Image(file.toURI().toString());
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
