package makeTour;

import Database.DatabaseConnection;
import JavaCode.Match_type;
import JavaCode.Stadium;
import TableView.FixtureTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import JavaCode.Team;
import javafx.util.Callback;

import javax.swing.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller {
    public TextField gameTimeID;
    public DatePicker datePickerID;
    public ComboBox<String> gameFormatID;
    public ComboBox<String> stadiumID;
    public ComboBox<String> hostTeamName;
    public ComboBox<String> opponentTeamName;
    public TextField NoOfTestGameID;
    public TextField NoOfODIGameID;
    public TextField NoOfT20GameID;
    public Button tourDone;
    public TableView<FixtureTable> fixtureTable;
    public TableColumn<FixtureTable,String> fixtureTableDate;
    public TableColumn<FixtureTable,String> fixtureTableMD;
    public TableColumn<FixtureTable,String> fixtureTableTime;
    ObservableList<FixtureTable> oblist = FXCollections.observableArrayList();
    DatabaseConnection dc;
    String[] allTeam;
    int tourID,t20=0,test=0,odi=0;
    int t20Ordering=0,testOrdering=0,odiOrdering=0;
    @FXML
    public void initialize() throws SQLException {
        dc = new DatabaseConnection();
        List<Team> teamList = dc.getAllTeams();
        List<Stadium> stadiumList = dc.getStadium();
        List<Match_type> gameFormatList = dc.getMatchType();
        allTeam = new String[teamList.size()];
        String[] allStadium = new String[stadiumList.size()];
        String[] allMatchType = new String[gameFormatList.size()];
        for (int i = 0; i < teamList.size(); i++) {
            allTeam[i] = teamList.get(i).getTeam_Name();
        }
        for (int i = 0; i < stadiumList.size(); i++) {
            allStadium[i] = stadiumList.get(i).getStadium_name();
        }
        for (int i = 0; i < gameFormatList.size(); i++) {
            allMatchType[i] = gameFormatList.get(i).getMatch_title();
        }
        ObservableList<String> observableHostTeamList = FXCollections.observableArrayList(allTeam);
        hostTeamName.setItems(observableHostTeamList);
        ObservableList<String> observableStadiumList = FXCollections.observableArrayList(allStadium);
        ObservableList<String> match_typeObservableList = FXCollections.observableArrayList(allMatchType);
        gameFormatID.setItems(match_typeObservableList);
        stadiumID.setItems(observableStadiumList);

        fixtureTableDate.setCellValueFactory(new PropertyValueFactory<FixtureTable,String>("date"));
        fixtureTableMD.setCellValueFactory(new PropertyValueFactory<FixtureTable,String>("MatchDetails"));
        fixtureTableTime.setCellValueFactory(new PropertyValueFactory<FixtureTable,String>("time"));
        fixtureTable.setItems(oblist);

        fixtureTableDate.setStyle("-fx-alignment:CENTER");
        fixtureTableMD.setStyle("-fx-alignment:CENTER");
        fixtureTableTime.setStyle("-fx-alignment:CENTER");

        gameFormatID.setPromptText("Match Type");
        stadiumID.setPromptText("Stadium");


        //datePickerID.setChronology(HijrahChronology.INSTANCE);
        datePickerID.setShowWeekNumbers(true);
        Callback<DatePicker, DateCell> callB = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        LocalDate today = LocalDate.now();
                        setDisable(empty || item.compareTo(today) < 0);
                    }

                };
            }

        };
        datePickerID.setDayCellFactory(callB);
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


    public void fixtureDoneAction(ActionEvent event) throws IOException, SQLException {
        if(datePickerID.getValue()!=null && gameFormatID.getValue()!=null && stadiumID.getValue()!=null && gameTimeID.getText()!=null){

            String date = datePickerID.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String matchType = gameFormatID.getValue();
            String stadium = stadiumID.getValue();
            String time = gameTimeID.getText();

            int fixture_id = 1;
            int order=1;
            int matchTypeID=1;
            int stadiumId=1;
            String hostTeamSF = null,opponentSF = null;

            switch (matchType) {
                case "T20":
                    order=++t20Ordering;
                    break;
                case "ODI":
                    order=++odiOrdering;
                    break;
                case "TEST":
                    order=++testOrdering;
                    break;
            }

            if(t20Ordering>t20 && matchType.equals("T20")){
                JOptionPane.showMessageDialog(null,"No more T20 match available in this tour");
                gameTimeID.clear();
                datePickerID.getEditor().clear();
                datePickerID.setValue(null);
                gameFormatID.getSelectionModel().clearSelection();
                stadiumID.getSelectionModel().clearSelection();

                gameFormatID.setPromptText("Match Type");
                stadiumID.setPromptText("Stadium");
                return;
            }
            else if(odiOrdering>odi && matchType.equals("ODI")) {
                JOptionPane.showMessageDialog(null,"No more ODI match available in this tour");
                gameTimeID.clear();
                datePickerID.getEditor().clear();
                datePickerID.setValue(null);
                gameFormatID.getSelectionModel().clearSelection();
                stadiumID.getSelectionModel().clearSelection();

                gameFormatID.setPromptText("Match Type");
                stadiumID.setPromptText("Stadium");
                return;
            }
            else if(testOrdering>test && matchType.equals("TEST")){
                JOptionPane.showMessageDialog(null,"No more Test match available in this tour");
                gameTimeID.clear();
                datePickerID.getEditor().clear();
                datePickerID.setValue(null);
                gameFormatID.getSelectionModel().clearSelection();
                stadiumID.getSelectionModel().clearSelection();

                return;
            }

            String fixtureIdQuery="SELECT MAX(FIXTURE_ID) as MaxFixID FROM CRICBUZZ.FIXTURE";
            ResultSet rs= dc.getQueryResult(fixtureIdQuery);
            try {
                if(rs.next()) fixture_id=rs.getInt("MaxFixID")+1;

            } catch (SQLException e) {
                System.out.println("Exception in fixture ID query::"+e);;
            }
            System.out.println("Here I am - 1");

            String stadiumIDquery="SELECT STADIUM_ID FROM CRICBUZZ.STADIUM " +
                    "WHERE STADIUM_NAME = " + "'" + stadium + "'";
            ResultSet rs2 = dc.getQueryResult(stadiumIDquery);
            if(rs2.next()){
                stadiumId= rs2.getInt("STADIUM_ID");
            }

            System.out.println("Here I am - 2");

            String matchTypeIDquery="SELECT MATCH_TYPE_ID FROM MATCH_TYPE " +
                    "WHERE MATCH_TITLE =" + "'" + matchType + "'";
            ResultSet rs3= dc.getQueryResult(matchTypeIDquery);
            if(rs3.next()) {
                matchTypeID = rs3.getInt("MATCH_TYPE_ID");
            }
            System.out.println("Here I am - 3");
            String insert_query="INSERT INTO CRICBUZZ.FIXTURE(FIXTURE_ID,TOUR_ID,DATETIME,STADIUM_ID,MATCH_TYPE_ID,ORDERING)\n" +
                    "VALUES("+fixture_id+","+tourID+",TO_DATE('"+date+" "+time+"','DD/MM/YYYY HH24:MI:SS'),"+stadiumId+","+matchTypeID+","+order+")";

            System.out.println("Here I am - 4");
            try{
                dc.insert(insert_query);
            }
            catch (SQLException e){
                System.out.println("Insert Fixture:"+e);
            }
            System.out.println("Here I am - 5");

            gameTimeID.clear();
            datePickerID.getEditor().clear();
            datePickerID.setValue(null);
            gameFormatID.getSelectionModel().clearSelection();
            stadiumID.getSelectionModel().clearSelection();
            String temp = null;

            System.out.println("Here I am - 6");

            if(order==1) temp="1st";
            else if(order==2) temp="2nd";
            else if(order==3) temp="3rd";
            else if(order==4) temp="4th";
            else if(order==5) temp="5th";

            System.out.println("Here I am - 7");

            String hostTeamSFquery="SELECT TEAM_SF FROM CRICBUZZ.TEAM WHERE TEAM_NAME="+"'"+hostTeamName.getValue()+"'";
            try (ResultSet rs4 = dc.getQueryResult(hostTeamSFquery)) {
                if (rs4.next()) hostTeamSF = rs4.getString("TEAM_SF");
            } catch (SQLException e){
                System.out.println("Exception in hostTeamSF:"+e);
            }

            System.out.println("Here I am - 8");

            String opponentTeamSFquery="SELECT TEAM_SF FROM CRICBUZZ.TEAM WHERE TEAM_NAME="+"'"+opponentTeamName.getValue()+"'";
            ResultSet rs5=dc.getQueryResult(opponentTeamSFquery);
            if(rs5.next()) opponentSF=rs5.getString("TEAM_SF");

            String matchD= temp+" "+matchType+"              "+hostTeamSF+" VS "+opponentSF+"\n"+stadium;
            FixtureTable ft= new FixtureTable(date,matchD,time);
            oblist.add(ft);
        }
        else JOptionPane.showMessageDialog(null, "Insert all the values");
    }


    public void tourDoneAction(ActionEvent event) {
        try {

            if(hostTeamName.getValue()!=null && opponentTeamName.getValue()!=null && NoOfT20GameID.getText()!=null && NoOfODIGameID.getText()!=null && NoOfTestGameID.getText()!=null) {
                String hostTeam = hostTeamName.getValue();
                String visitingTeam = opponentTeamName.getValue();
                t20 = Integer.parseInt(NoOfT20GameID.getText());
                test = Integer.parseInt(NoOfTestGameID.getText());
                odi = Integer.parseInt(NoOfODIGameID.getText());

                String touridquery = "SELECT MAX(TOUR_ID) as maxtour FROM CRICBUZZ.TOUR";
                ResultSet rs = dc.getQueryResult(touridquery);
                int primarykey = 1;
                if (rs.next())
                    primarykey = rs.getInt("maxtour") + 1;

                tourID = primarykey;
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
                    hostTeamName.setDisable(true);
                    opponentTeamName.setDisable(true);
                    tourDone.setDisable(true);
                    NoOfT20GameID.setDisable(true);
                    NoOfTestGameID.setDisable(true);
                    NoOfODIGameID.setDisable(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to initiate the tour. Try again.");
                }
            }
            else JOptionPane.showMessageDialog(null, "Insert all the values");
        } catch (SQLException e) {
            System.out.println("Failed to insert the query makeTour\\tourDoneAction :: " + e);
        }
    }


    public void hostTeamAction(ActionEvent event) {
        List<String> opponentTeam= new ArrayList<>(Arrays.asList(allTeam));
        opponentTeam.remove(hostTeamName.getValue());
        String[] opponentTeamArray= opponentTeam.toArray(new String[0]);
        ObservableList<String> obOpponentList= FXCollections.observableArrayList(opponentTeamArray);
        opponentTeamName.setItems(obOpponentList);
    }
}