package UpdateTables.TeamUpdate;

import Database.DatabaseConnection;
import JavaCode.Team;
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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.scene.image.ImageView;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TeamUpdateFinalController implements Initializable {
    @FXML
    private TextField teamName;
    @FXML
    private TextField headcoach;
    @FXML
    private TextField boardpresident;
    @FXML
    private DatePicker establishdate;
    @FXML
    private TextField teamshort;
    @FXML
    private Label origName;
    @FXML
    private Label origShortForm;
    @FXML
    private Label origCoach;
    @FXML
    private Label origPresident;
    @FXML
    private Label origEdate;
    @FXML
    private ImageView teamImg;
    @FXML
    private Button button;
    @FXML
    private Label updatemsg;

    private Team team;
    private List<String> filelist;
    private FileInputStream fin = null;
    private TeamStatsController tsc = null;

    public void setTeam(Team team) {
        this.team = team;
    }

    public void updateTeam(ActionEvent event) {
        if(teamName.getText().isBlank() || teamshort.getText().isBlank() || boardpresident.getText().isBlank() ||
            headcoach.getText().isBlank() || establishdate.getValue()==null) {
            updatemsg.setText("");
            JOptionPane.showMessageDialog(null, "Please fill out all the fields");
            return;
        }
        DatabaseConnection dc = new DatabaseConnection();
        Connection connection = dc.cricbuzzConnection();

        java.util.Date date = java.util.Date.from(establishdate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String query1 = "UPDATE CRICBUZZ.TEAM SET " +
                "TEAM_NAME = ?, TEAM_SF = ?, HEAD_COACH = ?, BOARD_PRESIDENT = ?, ESTABLISH_DATE = ?, TEAM_LOGO = ? " +
                "WHERE TEAM_ID = " + team.getTeam_ID();

        String query2 = "UPDATE CRICBUZZ.TEAM SET " +
                "TEAM_NAME = ?, TEAM_SF = ?, HEAD_COACH = ?, BOARD_PRESIDENT = ?, ESTABLISH_DATE = ? " +
                "WHERE TEAM_ID = " + team.getTeam_ID();

        PreparedStatement ps = null;
        try {
            if (fin != null)
                ps = connection.prepareStatement(query1);
            else
                ps = connection.prepareStatement(query2);

            ps.setString(1, teamName.getText());
            ps.setString(2, teamshort.getText());
            ps.setString(3, headcoach.getText());
            ps.setString(4, boardpresident.getText());
            ps.setDate(5, sqlDate);
            if(fin != null)
                ps.setBinaryStream(6, fin, fin.available());

            int v = ps.executeUpdate();
            if(v > 0) {
                team.setTeam_Name(teamName.getText());
                team.setTeam_sf(teamshort.getText());
                team.setHead_coach(headcoach.getText());
                team.setBoard_president(boardpresident.getText());
                team.setEstablished_date(date);
                team.setImage(teamImg);
                updatemsg.setText("TEAM UPDATE SUCCESSFUL");
                if(button.getText().equals("HIDE ORIGINALS")) {
                    origName.setText(team.getTeam_Name());
                    origShortForm.setText(team.getTeam_sf());
                    origCoach.setText(team.getHead_coach());
                    origPresident.setText(team.getBoard_president());

                    String pattern = "MM/dd/yyyy";
                    DateFormat df = new SimpleDateFormat(pattern);
                    String dobstring = df.format(team.getEstablished_date());
                    origEdate.setText(dobstring);
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to Update TeamUpdateFinalController :: " + e);
        }
        catch (IOException e) {
            System.out.println("Failed to update the image TeamUpdateFinalController :: " + e);
        }
        fin = null;
    }

    public void setTeamFinalController() {
        if(team==null) {
            System.out.println("Sorry Team is not found");
            return;
        }

        teamName.setText(team.getTeam_Name());
        teamshort.setText(team.getTeam_sf());
        headcoach.setText(team.getHead_coach());
        boardpresident.setText(team.getBoard_president());
        Image img = team.getImageholder().getImage();
        teamImg.setImage(img);

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(team.getEstablished_date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        establishdate.setValue(LocalDate.of(year, month, day));
    }

    public void showOrhideOriginal(ActionEvent event) {
        if(button.getText().equals("SHOW ORIGINALS")) {
            button.setText("HIDE ORIGINALS");
            origName.setText(team.getTeam_Name());
            origShortForm.setText(team.getTeam_sf());
            origCoach.setText(team.getHead_coach());
            origPresident.setText(team.getBoard_president());

            String pattern = "MM/dd/yyyy";
            DateFormat df = new SimpleDateFormat(pattern);
            String dobstring = df.format(team.getEstablished_date());
            origEdate.setText(dobstring);
        }
        else {
            button.setText("SHOW ORIGINALS");
            origName.setText("");
            origShortForm.setText("");
            origCoach.setText("");
            origPresident.setText("");
            origEdate.setText("");
        }
    }

    public void changeImage(ActionEvent event) {
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
            teamImg.setImage(image);
        }
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

    public void backToTeamUpdate(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/UpdateTables/TeamUpdate/TeamUpdate.fxml"));
            Stage window= (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(root));
            window.show();
        } catch (IOException e) {
            System.out.println("Failed to load UmpireUpdate UpdateTables\\TeamUpdate\\TeamUpdateFinalController :: " + e);;
        }
    }

    public void clickTeamStats(ActionEvent event) {
        if(team==null) {
            JOptionPane.showMessageDialog(null, "No player is available");
            return;
        }
        FXMLLoader loader = new FXMLLoader();
        Parent root = null;
        try {
            root = loader.load(getClass().getResource("/UpdateTables/TeamUpdate/TeamStats.fxml").openStream());
        } catch (IOException e) {
            System.out.println("PlayerStats.fxml file not found " + e);;
        }
        Stage substage = new Stage();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        substage.initOwner(stage);
        substage.initModality(Modality.WINDOW_MODAL);
        substage.setScene(new Scene(root));
        tsc = loader.getController();
        tsc.setPlayer(team);
        tsc.setAll();
        substage.show();
    }
}
