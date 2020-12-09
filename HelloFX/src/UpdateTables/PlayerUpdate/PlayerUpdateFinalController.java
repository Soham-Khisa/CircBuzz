package UpdateTables.PlayerUpdate;

import Database.DatabaseConnection;
import JavaCode.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.Date;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import javax.swing.*;

public class PlayerUpdateFinalController implements Initializable {

    @FXML
    private TextField firstname;

    @FXML
    private TextField lastname;

    @FXML
    private TextField birthplace;

    @FXML
    private DatePicker dob;

    @FXML
    private Button button;

    @FXML
    private Label origfname;

    @FXML
    private Label origlname;

    @FXML
    private Label origbirthplace;

    @FXML
    private Label origstatus;

    @FXML
    private Label origdob;

    @FXML
    private Label updatemessage;

    @FXML
    private ComboBox<String> status;

    @FXML
    private DatePicker death;

    @FXML
    private Label origdeath;

    @FXML
    private ComboBox<String> role;

    @FXML
    private ComboBox<String> jersey;

    @FXML
    private ImageView playerImage;

    @FXML
    private Button changeimage;

    @FXML
    private Label origjersey;

    @FXML
    private Label origrole;

    private Player player=null;
    private List<String> statusList;
    private List<String> roleList;
    private List<String> jerseyNo;
    private List<String> filelist;
    private FileInputStream fin = null;
    private PlayerStatsController psc = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        statusList = new ArrayList<>();
        statusList = List.of("Active", "Retired", "Died");

        roleList = new ArrayList<>();
        roleList = List.of("Wicket-keeper", "Batsman", "Bowler", "All-rounder", "Wicketkeeper-batsman");

        jerseyNo = new ArrayList<>();
        for(int i=1; i<=100; i++) {
            Integer integer = i;
            jerseyNo.add(integer.toString());
        }

        filelist = new ArrayList<>();
        filelist.add("*.png");
        filelist.add("*.jpg");
        filelist.add("*.JPG");
        filelist.add("*.PNG");
        filelist.add("*.jpeg");
        filelist.add("*.JPEG");
    }


    public void backToPlayerUpdate(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/UpdateTables/PlayerUpdate/PlayerUpdate.fxml"));
            Stage window= (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(root));
            window.show();
        } catch (IOException e) {
            System.out.println("Failed to load PlayerUpdate.fxml UpdateTables\\PlayerUpdate\\PlayerUpdateFinalController :: " + e);;
        }
    }


    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setPlayerFinalController() {
        if(player == null) {
            System.out.println("Sorry Player is not found");
            return;
        }

        firstname.setText(player.getFirst_Name());
        lastname.setText(player.getLast_Name());
        birthplace.setText(player.getBirthplace());
        ObservableList<String> observeStatus = FXCollections.observableArrayList(statusList);
        status.setItems(observeStatus);
        status.setValue(player.getStatus());

        ObservableList<String> observeRole = FXCollections.observableArrayList(roleList);
        role.setItems(observeRole);
        role.setValue(player.getRole());

        setAvailableJersey();

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(player.getDob());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        dob.setValue(LocalDate.of(year, month, day));

        if(player.getDeath() != null) {
            calendar = new GregorianCalendar();
            calendar.setTime(player.getDeath());
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
            day = calendar.get(Calendar.DAY_OF_MONTH);
            death.setValue(LocalDate.of(year, month, day));
        }

        Image img = player.getImageView().getImage();
        playerImage.setImage(img);

        Callback<DatePicker, DateCell> callB = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        String datetext = formatter.format(player.getDob());
                        Date formatedDate = null;
                        try {
                            formatedDate = formatter.parse(datetext);
                        } catch (ParseException e) {
                            System.out.println("Failed to parse umpire.dob UmpireConfirmController :: " + e);
                        }
                        LocalDate birthdate = formatedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        setDisable(empty || item.compareTo(birthdate.plusYears(14)) < 0 || item.compareTo(LocalDate.now())>0);
                    }

                };
            }
        };
        death.setDayCellFactory(callB);


        callB = new Callback<DatePicker, DateCell>() {
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


    public void setAvailableJersey() {
        if(player == null)    return;

        try {
            DatabaseConnection dc = new DatabaseConnection();
            int teamid = -1;
            String teamQuery = "SELECT TEAM_ID FROM CRICBUZZ.PLAYER WHERE PLAYER_ID = " + player.getPlayer_ID();

            ResultSet rs = dc.getQueryResult(teamQuery);
            if(rs.next())   teamid = rs.getInt("TEAM_ID");

            String jerseyQuery = "SELECT JERSEY FROM CRICBUZZ.PLAYER WHERE TEAM_ID = " + teamid;
            rs = dc.getQueryResult(jerseyQuery);

            while (rs.next()) {
                Integer integer = rs.getInt("JERSEY");
                if(player.getJersey_No() != integer)
                    jerseyNo.remove(integer.toString());
            }
        }
        catch (SQLException e) {
            System.out.println("Failed to add available jerseys. :: " + e);
        }

        ObservableList<String> observeJersey = FXCollections.observableArrayList(jerseyNo);
        jersey.setItems(observeJersey);
        Integer integer = player.getJersey_No();
        jersey.setValue(integer.toString());
    }

    public void showOrhideOriginal(ActionEvent event) {
        if(button.getText().equals("SHOW ORIGINALS")) {
            button.setText("HIDE ORIGINALS");
            origfname.setText(player.getFirst_Name());
            origlname.setText(player.getLast_Name());
            origbirthplace.setText(player.getBirthplace());
            origstatus.setText(player.getStatus());
            origrole.setText(player.getRole());
            Integer integer = player.getJersey_No();
            origjersey.setText(integer.toString());

            String pattern = "MM/dd/yyyy";
            DateFormat df = new SimpleDateFormat(pattern);
            String datestring = df.format(player.getDob());
            origdob.setText(datestring);

            if(player.getDeath() != null) {
                pattern = "MM/dd/yyyy";
                df = new SimpleDateFormat(pattern);
                datestring = df.format(player.getDeath());
                origdeath.setText(datestring);
            }
            else
                origdeath.setText("");
        }
        else {
            button.setText("SHOW ORIGINALS");
            origfname.setText("");
            origlname.setText("");
            origbirthplace.setText("");
            origstatus.setText("");
            origjersey.setText("");
            origrole.setText("");
            origdob.setText("");
            origdeath.setText("");
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
            playerImage.setImage(image);
        }
    }

    public void showPlayerStats(ActionEvent event) {
        if(player==null) {
            JOptionPane.showMessageDialog(null, "No player is available");
            return;
        }
        FXMLLoader loader = new FXMLLoader();
        Parent root = null;
        try {
            root = loader.load(getClass().getResource("/UpdateTables/PlayerUpdate/PlayerStats.fxml").openStream());
        } catch (IOException e) {
            System.out.println("PlayerStats.fxml file not found " + e);;
        }
        Stage substage = new Stage();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        substage.initOwner(stage);
        substage.initModality(Modality.WINDOW_MODAL);
        substage.setScene(new Scene(root));
        psc = loader.getController();
        psc.setPlayer(player);
        psc.setAll();
        substage.show();
    }

    public void UpdatePlayer(ActionEvent event) {
        if(firstname.getText().isBlank() || lastname.getText().isBlank() || birthplace.getText().isBlank() ||
        dob.getValue() == null || role.getValue() == null || jersey.getValue() == null || status.getValue() == null) {
            updatemessage.setText("");
            JOptionPane.showMessageDialog(null, "Please fill out all the fields");
            return;
        }
        else if((status.getValue().equals("Active") || status.getValue().equals("Retired")) && death.getValue()!=null) {
            updatemessage.setText("");
            JOptionPane.showMessageDialog(null, "The player cannot have a death date with the status Active or Retired");
            return;
        }
        else if (status.getValue().equals("Died") && death.getValue() == null) {
            updatemessage.setText("");
            JOptionPane.showMessageDialog(null, "Please select the death date");
            return;
        }

        DatabaseConnection dc = new DatabaseConnection();
        Connection connection = dc.cricbuzzConnection();

        java.util.Date birthdate = java.util.Date.from(dob.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        java.sql.Date sqlbirthDate = new java.sql.Date(birthdate.getTime());

        java.util.Date deathdate = null;
        java.sql.Date sqlDeathDate = null;
        if(death.getValue() != null) {
            deathdate = java.util.Date.from(death.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            sqlDeathDate = new java.sql.Date(deathdate.getTime());
        }

        String query1 ="UPDATE CRICBUZZ.PLAYER SET " +
                "FIRST_NAME = ?, LAST_NAME = ?, BORN = ?, DOB = ?, ROLE = ?, JERSEY = ?, STATUS = ?, DEATH = ? " +
                "WHERE PLAYER_ID = " + player.getPlayer_ID();

        String query2 = "UPDATE CRICBUZZ.PLAYER SET " +
                "FIRST_NAME = ?, LAST_NAME = ?, BORN = ?, DOB = ?, ROLE = ?, JERSEY = ?, STATUS = ?, DEATH = ?, PROFILE_PIC = ? " +
                "WHERE PLAYER_ID = " + player.getPlayer_ID();

        PreparedStatement ps = null;
        try {

            if(fin==null) ps = connection.prepareStatement(query1);
            else if(fin!=null)    ps = connection.prepareStatement(query2);

            ps.setString(1, firstname.getText());
            ps.setString(2, lastname.getText());
            ps.setString(3, birthplace.getText());
            ps.setDate(4, sqlbirthDate);
            ps.setString(5, role.getValue());
            ps.setInt(6, Integer.parseInt(jersey.getValue()));
            ps.setString(7, status.getValue());

            if(fin==null && death.getValue()==null) {
                ps.setNull(8, Types.DATE);
            }
            else if(fin==null && death.getValue()!=null) {
                ps.setDate(8, sqlDeathDate);
            }
            else if(fin!=null && death.getValue()==null) {
                ps.setNull(8, Types.DATE);
                ps.setBinaryStream(9, fin, fin.available());
            }
            else if(fin!=null && death.getValue()!=null) {
                ps.setDate(8, sqlDeathDate);
                ps.setBinaryStream(9, fin, fin.available());
            }

            int v = ps.executeUpdate();
            if(v>0) {
                player.setFirst_Name(firstname.getText());
                player.setLast_Name(lastname.getText());
                player.setBirthplace(birthplace.getText());
                player.setDob(birthdate);
                player.setRole(role.getValue());
                player.setJersey_No(Integer.parseInt(jersey.getValue()));
                player.setStatus(status.getValue());
                player.setDeath(deathdate);
                player.setImageView(playerImage);
                updatemessage.setText("TEAM UPDATE SUCCESSFUL");
                if(button.getText().equals("HIDE ORIGINALS")) {
                    origfname.setText(player.getFirst_Name());
                    origlname.setText(player.getLast_Name());
                    origbirthplace.setText(player.getBirthplace());
                    Integer integer = player.getJersey_No();
                    origjersey.setText(integer.toString());
                    origrole.setText(player.getRole());
                    origstatus.setText(player.getStatus());

                    String pattern = "MM/dd/yyyy";
                    DateFormat df = new SimpleDateFormat(pattern);
                    String dobstring = df.format(player.getDob());
                    origdob.setText(dobstring);

                    if(player.getDeath()!=null) {
                        String deathstring = df.format(player.getDeath());
                        origdeath.setText(deathstring);
                    }
                    else    origdeath.setText("");
                }
            }
        }
        catch (SQLException e) {
            System.out.println("failed to load prepared statement playerupdatefinal:: " + e);
        }
        catch (IOException e) {
            System.out.println("failed to load image playerupdatefinal :: " + e);
        }
        fin = null;
    }

    public void clearDeathdate(ActionEvent event) {
        death.setValue(null);
    }
}
