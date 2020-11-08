package makeTour;

import Database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import JavaCode.Team;

import javax.swing.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Controller {
    public TextField gameTimeID;
    public DatePicker datePickerID;
    public ComboBox gameFormatID;
    public ComboBox stadiumID;
    public ComboBox<String> hostTeamName;
    public ComboBox<String> opponentTeamName;
    public TextField NoOfTestGameID;
    public TextField NoOfODIGameID;
    public TextField NoOfT20GameID;
    DatabaseConnection dc;
    @FXML
    public void initialize() throws SQLException {
        dc= new DatabaseConnection();
        List<Team> teamList=dc.getAllTeams();
        String[] allTeam=new String[teamList.size()];
        for(int i=0;i<teamList.size();i++){
            allTeam[i]=teamList.get(i).getTeam_Name();
        }
        ObservableList<String> observableList= FXCollections.observableArrayList(allTeam);
        hostTeamName.setItems(observableList);
        opponentTeamName.setItems(observableList);

    }


    public void backButtonAction(ActionEvent event) throws IOException {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/frontPage/frontPage.fxml"));
        } catch (IOException e) {
            System.out.println("Exception in loading Front page:"+e);
        }
        Stage window= (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root, 660, 586));
        window.show();
    }
    public void fixtureDoneAction(ActionEvent event) throws IOException{
    }
    public void tourDoneAction(ActionEvent event) {
        try {

            if(hostTeamName.getValue()!=null && opponentTeamName.getValue()!=null && NoOfT20GameID.getText()!=null && NoOfODIGameID.getText()!=null && NoOfTestGameID.getText()!=null) {
                String hostTeam = hostTeamName.getValue();
                String visitingTeam = opponentTeamName.getValue();
                int t20 = Integer.parseInt(NoOfT20GameID.getText());
                int test = Integer.parseInt(NoOfTestGameID.getText());
                int odi = Integer.parseInt(NoOfODIGameID.getText());

                String touridquery = "SELECT MAX(TOUR_ID) as maxtour FROM CRICBUZZ.TOUR";
                ResultSet rs = dc.getQueryResult(touridquery);
                int primarykey = 1;
                if (rs.next())
                    primarykey = rs.getInt("maxtour") + 1;


                String hostteamquery = "SELECT TEAM_ID FROM CRICBUZZ.TEAM WHERE TEAM_NAME =" + "'" + hostTeam + "'";
                int hostteamid = -1;
                rs = dc.getQueryResult(hostteamquery);
                if (rs.next())
                    hostteamid = rs.getInt("TEAM_ID");

                String visitteamquery = "SELECT TEAM_ID FROM CRICBUZZ.TEAM WHERE TEAM_NAME =" + "'" + visitingTeam + "'";
                int visitteamid = -1;
                rs = dc.getQueryResult(visitteamquery);
                if (rs.next())
                    visitteamid = rs.getInt("TEAM_ID");


                if (hostteamid != -1 && visitteamid != -1) {
                    String insert_query = "INSERT INTO CRICBUZZ.TOUR(TOUR_ID, HOST_TEAM, VISITING_TEAM, T20, ODI, TEST) " +
                            "VALUES (" + primarykey + ", " + hostteamid + ", " + visitteamid + ", " + t20 + ", " + odi + ", " + test + ")";
                    dc.insert(insert_query);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to initiate the tour. Try again.");
                }
            }
            else JOptionPane.showMessageDialog(null, "Insert all the values");
        } catch (SQLException e) {
            System.out.println("Failed to insert the query makeTour\\tourDoneAction :: " + e);
        }
    }
}