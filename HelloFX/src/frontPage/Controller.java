package frontPage;

import Database.DatabaseConnection;
import JavaCode.Match;
import TableView.EverydayMatch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class Controller extends Component {

    public TableView<EverydayMatch> todaysMatchTable;
    public TableColumn<EverydayMatch,String> matchDetails;
    public TableColumn<EverydayMatch,String> time;
    public TableColumn<EverydayMatch, Integer> matchID;
    public Button todayMatchButtonID;

    @FXML
    private TextField umpireFirstName;
    @FXML
    private TextField umpireCountry;
    @FXML
    private TextField umpireLastName;
    @FXML
    private DatePicker umpire_dob;
    @FXML
    private TextField stadiumName;
    @FXML
    private TextField stadiumLocation;
    @FXML
    private TextField stadiumCountry;
    @FXML
    private TextField seatNoStadium;
    @FXML
    private Button enrolStadium;
    @FXML
    private Button enrolUmpire;
    @FXML
    private Label stadiumlabel;
    @FXML
    private Label umpirelabel;
    DatabaseConnection dc;
    String fixtureIDQuery;

    ResultSet rs;
    ObservableList<EverydayMatch> oblist= FXCollections.observableArrayList();
    Stage window;

    @FXML
    public void initialize(){
        todaysMatchTable.setVisible(false);
        dc = new DatabaseConnection();
        matchDetails.setCellValueFactory(new PropertyValueFactory<EverydayMatch,String>("matchDetails"));
        time.setCellValueFactory(new PropertyValueFactory<EverydayMatch,String>("time"));
        matchID.setCellValueFactory(new PropertyValueFactory<EverydayMatch, Integer>("matchID"));

        matchDetails.setStyle("-fx-alignment:CENTER");
        time.setStyle("-fx-alignment:CENTER");
        matchID.setStyle("-fx-alignment:CENTER");

        fixtureIDQuery="SELECT FIXTURE_ID\n" +
                "FROM FIXTURE\n" +
                "WHERE TO_CHAR(DATETIME,'YYYY-MM-DD') = '" + LocalDate.now() + "'";
        rs = dc.getQueryResult(fixtureIDQuery);
        Callback<DatePicker, DateCell> callB = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                        LocalDate today = LocalDate.now();
                        setDisable(empty || item.compareTo(today.minusYears(20)) > 0);
                    }

                };
            }

        };
        umpire_dob.setDayCellFactory(callB);
        ///umpire_dob.setValue(LocalDate.now().minusYears(20));
       /* todaysMatchTable.setRowFactory(tv->{
            matchDetails.setOn
        });*/
        todaysMatchTable.setRowFactory( tv -> {
            TableRow<EverydayMatch> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    EverydayMatch rowData = row.getItem();
                    int match_id=rowData.getMatchID();
                    try {
                        Match match= new Match(match_id);
                        switch (match.getMatch_status()) {
                            case "Live":
                                JOptionPane.showMessageDialog(null, "The match is in live");
                                break;
                            case "End":
                                JOptionPane.showMessageDialog(null, "The match has been ended");
                                break;
                            case " ":
                                break;
                            case "Upcoming":
                                int response = JOptionPane.showConfirmDialog(this, "Do you want to start the match?", "Confirmation Dialogue", JOptionPane.YES_NO_OPTION);
                                if (response == JOptionPane.YES_OPTION) {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/startGame/startGame.fxml"));
                                    Parent root = null;
                                    try {
                                        root = loader.load();
                                        assert root != null;
                                        window.setScene(new Scene(root));
                                        window.show();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    startGame.Controller controller = loader.getController();
                                    try {
                                        controller.transferTeams(match.getMatchID());
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row ;
        });
    }


    public void umpireEnrolAction(ActionEvent event) {
        if (umpireFirstName.getText() != null && umpireLastName.getText() != null && umpireCountry.getText() != null && umpire_dob.getValue() != null) {
            dc = new DatabaseConnection();

            String date = umpire_dob.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            String query = "INSERT INTO CRICBUZZ.UMPIRE (FIRST_NAME, LAST_NAME, COUNTRY, DOB) " +
                    "VALUES ('" + umpireFirstName.getText() + "', '" + umpireLastName.getText() + "', '" + umpireCountry.getText() + "', TO_DATE('" + date + "', 'dd/MM/yyyy'))";
            String UmpId[] = {"UMPIRE_ID"};
            Boolean verdict = dc.doUpdate(query, UmpId);
            if (verdict) {
                umpirelabel.setText("Umpire enrollment is successful");
                umpireFirstName.clear();
                umpireLastName.clear();
                umpireCountry.clear();
                umpire_dob.getEditor().clear();
                umpire_dob.setValue(null);
            }
            else {
                umpirelabel.setText("");
                JOptionPane.showMessageDialog(null,"Umpire enrollment failed. Try again");
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Umpire enrollment failed");
        }
    }

    public void makeTourAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/makeTour/makeTour.fxml"));
        window= (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root));
        window.show();
    }


    public void enrolStadiumAction(ActionEvent event) throws SQLException {
        if (stadiumName.getText() != null && stadiumLocation.getText() != null && stadiumCountry.getText() != null && seatNoStadium.getText() != null) {
            int seatNum = Integer.parseInt(seatNoStadium.getText());
            dc = new DatabaseConnection();

            String query = "INSERT INTO CRICBUZZ.STADIUM (STADIUM_NAME, LOCATION, COUNTRY, CAPACITY) " +
                    "VALUES ('" + stadiumName.getText() + "', '" + stadiumLocation.getText() + "', '" + stadiumCountry.getText() + "', " + seatNum + ")";
            String stdmId[] = {"STADIUM_ID"};
            Boolean verdict = dc.doUpdate(query, stdmId);
            if (verdict) {
                stadiumlabel.setText("Stadium enrollment is successful");
                stadiumName.clear();
                stadiumLocation.clear();
                stadiumCountry.clear();
                seatNoStadium.clear();
            }
            else {
                stadiumlabel.setText("");
                JOptionPane.showMessageDialog(null,"Stadium enrollment failed. Try again");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Insert all the fields");
        }
    }

    public void enrolAnUmpireAction(ActionEvent event) {
        if(!umpireFirstName.isVisible() && !umpireLastName.isVisible() && !umpireCountry.isVisible() && !umpire_dob.isVisible() && !enrolUmpire.isVisible()) {
            umpirelabel.setText("");
            umpirelabel.setVisible(true);
            umpireFirstName.setVisible(true);
            umpireCountry.setVisible(true);
            umpireLastName.setVisible(true);
            umpire_dob.setVisible(true);
            enrolUmpire.setVisible(true);
        }
        else {
            umpirelabel.setVisible(false);
            umpireFirstName.setVisible(false);
            umpireCountry.setVisible(false);
            umpireLastName.setVisible(false);
            umpire_dob.setVisible(false);
            enrolUmpire.setVisible(false);
        }
    }

    public void enrolAteamAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/teamEnrol/teamEnrol.fxml"));
        window= (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root));
        window.show();
    }

    public void enrolAstadiumAction(ActionEvent event) {
        if(stadiumName.isVisible() && stadiumLocation.isVisible() && stadiumCountry.isVisible() && seatNoStadium.isVisible() && enrolStadium.isVisible()) {
            stadiumName.setVisible(false);
            stadiumLocation.setVisible(false);
            stadiumCountry.setVisible(false);
            seatNoStadium.setVisible(false);
            enrolStadium.setVisible(false);
            stadiumlabel.setVisible(false);
        }
        else {
            stadiumlabel.setText("");
            stadiumlabel.setVisible(true);
            stadiumName.setVisible(true);
            stadiumLocation.setVisible(true);
            stadiumCountry.setVisible(true);
            seatNoStadium.setVisible(true);
            enrolStadium.setVisible(true);
        }
    }

    public void updateInfo(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/UpdateTables/updateinfo.fxml"));
            window= (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(root));
            window.show();
        } catch (IOException e) {
            System.out.println("Failed to load updateinfo frontPage\\Controller :: " + e);;
        }
    }


    public void todayMatchButtonAction(ActionEvent event) throws SQLException, IOException {
        if(todaysMatchTable.isVisible()) {
            JOptionPane.showMessageDialog(null, "Today's matches are given in the table");
            return;
        }

        window= (Stage) ((Node) event.getSource()).getScene().getWindow();
        int[] fixtureID = new int[10];
        int ordering=0,count=0;

        ResultSet rs2;
        while(rs.next()){
            fixtureID[count++]=rs.getInt("FIXTURE_ID");
        }
        if(count!=0){
            todaysMatchTable.setVisible(true);
        }
        else{
            JOptionPane.showMessageDialog(null,"No Match for today");
            return;
        }
        String hostTeam=null;
        String visitingTeam=null;
        String stadium=null,timE=null;
        int matchID=0;
        for(int i=0; i<count; i++){
            int fixtureId=fixtureID[i];
            String matchType=null,matchDetails=null;
            rs2 = dc.getQueryResult("SELECT ORDERING FROM FIXTURE\n" +
                    "WHERE FIXTURE_ID=" + fixtureId);
            if(rs2.next()) ordering= rs2.getInt("ORDERING");
            rs2.close();
            rs2=dc.getQueryResult("SELECT MATCH_ID FROM MATCH\n" +
                    "WHERE FIXTURE_ID="+fixtureId);
            if(rs2.next()) matchID=rs2.getInt("MATCH_ID");
            rs2.close();
            rs2=dc.getQueryResult("SELECT TEAM_SF FROM TEAM\n" +
                    "WHERE TEAM_ID = (SELECT HOST_TEAM FROM MATCH WHERE FIXTURE_ID="+fixtureId+")");
            if(rs2.next()) hostTeam=rs2.getString("TEAM_SF");
            rs2.close();
            rs2=dc.getQueryResult("SELECT TEAM_SF FROM TEAM\n" +
                    "WHERE TEAM_ID = (SELECT VISITING_TEAM FROM MATCH WHERE FIXTURE_ID="+fixtureId+")");
            if(rs2.next()) visitingTeam=rs2.getString("TEAM_SF");
            rs2.close();
            rs2=dc.getQueryResult("SELECT STADIUM_NAME FROM STADIUM\n" +
                    "WHERE STADIUM_ID=(SELECT STADIUM_ID FROM FIXTURE WHERE FIXTURE_ID="+fixtureId+")");
            if(rs2.next()) stadium=rs2.getString("STADIUM_NAME");
            rs2.close();
            rs2=dc.getQueryResult("SELECT MATCH_TITLE FROM MATCH_TYPE\n" +
                    "WHERE MATCH_TYPE_ID=(SELECT MATCH_TYPE_ID FROM FIXTURE WHERE FIXTURE_ID="+fixtureId+")");
            if(rs2.next()) matchType=rs2.getString("MATCH_TITLE");
            rs2.close();
            rs2=dc.getQueryResult("SELECT TO_CHAR(DATETIME,'HH24:MI:SS') AS TIME FROM FIXTURE\n" +
                    "WHERE FIXTURE_ID="+fixtureId);
            if(rs2.next()) timE=rs2.getString("TIME");

            if(ordering==1) matchType="1st "+matchType;
            else if(ordering==2) matchType="2nd "+matchType;
            else if(ordering==3) matchType="3rd "+matchType;
            else if(ordering==4) matchType="4th "+matchType;
            else if(ordering==5) matchType="5th "+matchType;
            matchDetails=matchType+"             "+hostTeam+" vs "+visitingTeam+"\n"+stadium;
            EverydayMatch em= new EverydayMatch(matchDetails,timE,hostTeam,visitingTeam,matchID);
            oblist.add(em);
        }
        todaysMatchTable.setItems(oblist);
    }
}