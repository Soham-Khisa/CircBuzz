package startGame;

import Database.DatabaseConnection;
import JavaCode.*;
import PlayerEnrol.PlayerController;
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
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Controller extends Component {
    public ComboBox<String> umpire1ID;
    public ComboBox<String> umpire2ID;
    public ComboBox<String> umpire3ID;
    public ChoiceBox<String> tossID;
    public ChoiceBox<String> chooseID;
    public Button debut1ID;
    public Button debut2ID;
    public ListView<String> team1ListVID;
    public ListView<String> team2ListVID;
    public ListView<String> team1ListViewSelectedID;
    public ListView<String> team2ListViewSelectedID;
    public Button team1;
    public Button team2;
    public Button team1ConfirmButton;
    public Button team2ConfirmButton;
    public Button startGameID;
    public ChoiceBox<String> captain1;
    public ChoiceBox<String> wk1;
    public ChoiceBox<String> captain2;
    public ChoiceBox<String> wk2;
    public Button confirmButtonID;
    public Label c1label;
    public Label wk1label;
    public Label c2Label;
    public Label wk2Label;
    DatabaseConnection dc;
    ResultSet rs;
    List<String> umpiresName;
    ArrayList<Player> players1,players2;
    List<Player> selectedPlayers1,selectedPlayers2;
    Team t1,t2;
    Match match;
    Innings in1,in2;
    boolean isTeam2Confirm=false,isTeam1Confirm=false;
    @FXML
    public void initialize() throws IOException, SQLException {
             dc = new DatabaseConnection();
             umpiresName = dc.getUmpiresName();
             selectedPlayers1=new ArrayList<>(11);
             selectedPlayers2=new ArrayList<>(11);
        ObservableList<String> umpire1observableList= FXCollections.observableArrayList(umpiresName);
        umpire1ID.setItems(umpire1observableList);
        String[] choose= {"Elected to bat first","Elected to bowl first"};
        ObservableList<String> batbowlChoose=FXCollections.observableArrayList(choose);
        chooseID.setItems(batbowlChoose);

        debut1ID.setVisible(false);
        debut2ID.setVisible(false);
        team1ListVID.setVisible(false);
        team1ListViewSelectedID.setVisible(false);
        team2ListVID.setVisible(false);
        team2ListViewSelectedID.setVisible(false);
        startGameID.setVisible(false);
        team1ConfirmButton.setVisible(false);
        team2ConfirmButton.setVisible(false);
        team1.setVisible(false);
        team2.setVisible(false);
        captain1.setVisible(false);
        wk1.setVisible(false);
        captain2.setVisible(false);
        wk2.setVisible(false);
        c1label.setVisible(false);
        wk1label.setVisible(false);
        c2Label.setVisible(false);
        wk2Label.setVisible(false);



        ObservableList<String> observableList1=FXCollections.observableArrayList();
        ObservableList<String> observableList2= FXCollections.observableArrayList();

        captain1.setItems(observableList1);
        wk1.setItems(observableList1);
        captain2.setItems(observableList2);
        wk2.setItems(observableList2);

       /* team2ListVID.setOnMouseClicked(new EventHandler<>() {

            @Override
            public void handle(javafx.scene.input.MouseEvent mouseEvent) {
                if (!team2ListVID.getSelectionModel().getSelectedItem().isEmpty()) {  //Can't handle the empty cell
                    observableList2.add(team2ListVID.getSelectionModel().getSelectedItem());
                }
            }
        });*/
        team1ListVID.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(item);
                }
            };
            cell.setOnMouseClicked(e -> {
                if (!cell.isEmpty() && e.getClickCount()==2) {
                    //System.out.println("You clicked on " + cell.getItem());
                    observableList1.add(cell.getItem());
                    String[] name=cell.getItem().split(" ");
                    selectedPlayers1.add(new Player(name[0],name[1]));

                    e.consume();
                }
            });
            return cell;
        });

        /*team1ListVID.setOnMouseClicked(e -> {
            System.out.println("You clicked on an empty cell");
        });*/
        team1ListViewSelectedID.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(item);
                }
            };
            cell.setOnMouseClicked(e -> {
                if (!cell.isEmpty() && e.getClickCount()==2) {
                    //System.out.println("You clicked on " + cell.getItem());
                    int response=JOptionPane.showConfirmDialog(this,"Do you want to delete it?","Deletion",JOptionPane.YES_NO_OPTION);
                    if(response==JOptionPane.YES_OPTION){
                        String temp=cell.getItem();
                        observableList1.remove(temp);
                        String[] name=temp.split(" ");
                        Player temp2=new Player(name[0],name[1]);
                        boolean bool = selectedPlayers1.remove(temp2);
                        if(bool) System.out.println("deleted");
                        else System.out.println("No delete");

                    }
                    e.consume();
                }
            });
            return cell;
        });
        team2ListVID.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(item);
                }
            };
            cell.setOnMouseClicked(e -> {
                if (!cell.isEmpty() && e.getClickCount()==2) {
                    observableList2.add(cell.getItem());
                    String[] name=cell.getItem().split(" ");
                    selectedPlayers2.add(new Player(name[0],name[1]));
                    e.consume();
                }
            });
            return cell;
        });

        team2ListViewSelectedID.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(item);
                }
            };
            cell.setOnMouseClicked(e -> {
                if (!cell.isEmpty() && e.getClickCount()==2) {
                    int response=JOptionPane.showConfirmDialog(this,"Do you want to delete it?","Deletion",JOptionPane.YES_NO_OPTION);
                    if(response==JOptionPane.YES_OPTION){
                        String[] name=cell.getItem().split(" ");
                        observableList2.remove(cell.getItem());
                        boolean bool = selectedPlayers2.remove(new Player(name[0],name[1]));
                        if(bool) System.out.println("deleted");
                        else System.out.println("No delete");
                    }
                    e.consume();
                }
            });
            return cell;
        });

        team1ListViewSelectedID.setItems(observableList1);
        team2ListViewSelectedID.setItems(observableList2);
    }


    public void transferTeams(int match_id) throws SQLException {
        match = new Match(match_id);
        t1= new Team(match.getHostTeam());
        t2= new Team(match.getVisitingTeam());

        team1.setText(new Team(match.getHostTeam()).getTeam_sf());
        team2.setText(new Team(match.getVisitingTeam()).getTeam_sf());
        ObservableList<String> tossList= FXCollections.observableArrayList();
        tossList.add(t1.getTeam_sf());
        tossList.add(t2.getTeam_sf());
        tossID.setItems(tossList);

    }

    public void startGameAction(ActionEvent event) throws IOException, SQLException {

        DatabaseConnection dc= new DatabaseConnection();
        String query="UPDATE MATCH\n" +
                "SET UMPIRE_ONE="+match.getU1()+",UMPIRE_TWO="+match.getU2()+
                ",UMPIRE_THREE="+match.getU3()+",TOS_WINNER="+match.getTossWinner()+
                ",TOS_DECISION='"+match.getTossDecision()+"',HOST_TEAM_CAPTAIN="+match.getHostTeamCaptain()+
                ",VISITING_TEAM_CAPTAIN="+match.getVisitingTeamCaptain()+",HOST_TEAM_WK="+match.getHostTeamWK()+
                ",VISITING_TEAM_WK="+match.getVisitingTeamWK()+",MATCH_STATUS='Live' WHERE MATCH_ID="+
                match.getMatchID();

        dc.doUpdate(query);


        Parent root = FXMLLoader.load(getClass().getResource("/runningMatch/runningMatch.fxml"));


        Stage window= (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root, 660, 580));
        window.setTitle("Live Match");
        window.show();
    }

    public void debut1Action(ActionEvent event) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/PlayerEnrol/Player.fxml"));
        Parent root = loader.load();
        PlayerController controller= loader.getController();
        controller.setPlayerTeam(t1);
        controller.setAvailableJersey();
          Stage stage=new Stage();
          stage.setScene(new Scene(root));
          stage.setTitle("Insert a Player");
          stage.show();
    }

    public void debut2Action(ActionEvent event) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/PlayerEnrol/Player.fxml"));
        Parent root = loader.load();
        PlayerController controller= loader.getController();
        controller.setPlayerTeam(t2);
        controller.setAvailableJersey();
        Stage stage=new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Insert a Player");
        stage.show();
    }

    public void umpire1Action(ActionEvent event) {
        umpiresName.remove(umpire1ID.getValue());
        ObservableList<String> observableUmpire2List=FXCollections.observableArrayList(umpiresName);
        umpire2ID.setItems(observableUmpire2List);
    }

    public void umpire2Action(ActionEvent event) {
        umpiresName.remove(umpire2ID.getValue());
        ObservableList<String> observableUmpire3List= FXCollections.observableArrayList(umpiresName);
        umpire3ID.setItems(observableUmpire3List);
    }

    public void team1ButtonAction(ActionEvent event) throws SQLException {
        team1ListVID.setVisible(true);
        team1ListViewSelectedID.setVisible(true);
        debut1ID.setVisible(true);
        team1ConfirmButton.setVisible(true);
        c1label.setVisible(true);
        wk1label.setVisible(true);
        captain1.setVisible(true);
        wk1.setVisible(true);
        players1=dc.activePlayerOfTeam(t1.getTeam_ID());
        String[] pfullname=new String[players1.size()];
        for(int i=0;i<players1.size();i++){
            pfullname[i]=players1.get(i).getFirst_Name()+" "+players1.get(i).getLast_Name();
        }
        ObservableList<String> observableList= FXCollections.observableArrayList(pfullname);
        team1ListVID.setItems(observableList);

    }

    public void team2ButtonAction(ActionEvent mouseEvent) throws SQLException {
        team2ListVID.setVisible(true);
        team2ListViewSelectedID.setVisible(true);
        debut2ID.setVisible(true);
        team2ConfirmButton.setVisible(true);
        c2Label.setVisible(true);
        wk2Label.setVisible(true);
        captain2.setVisible(true);
        wk2.setVisible(true);
        players2=dc.activePlayerOfTeam(t2.getTeam_ID());
        String[] pfullname=new String[players2.size()];
        for(int i=0;i<players2.size();i++){
            pfullname[i]=players2.get(i).getFirst_Name()+" "+players2.get(i).getLast_Name();
        }
        ObservableList<String> observableList= FXCollections.observableArrayList(pfullname);
        team2ListVID.setItems(observableList);
    }

    public void confirm1Action(ActionEvent event) throws SQLException {
        if(captain1.getSelectionModel().isEmpty()){
            JOptionPane.showMessageDialog(null,"Choose Captain");
            return;
        }
        else if(wk1.getSelectionModel().isEmpty()){
            JOptionPane.showMessageDialog(null,"Choose Wicket-Keeper");
            return;
        }
        isTeam1Confirm=true;
        team1.setDisable(true);
        team1ListVID.setDisable(true);
        team1ListViewSelectedID.setDisable(true);
        wk1.setDisable(true);
        captain1.setDisable(true);
        debut1ID.setDisable(true);
        team2.setVisible(true);

        String[] name=captain1.getValue().split(" ");
        Player p= new Player(name[0],name[1]);
        p.makePlayerWithFirstLastName(t1.getTeam_ID());
        int c1=p.getPlayer_ID();
        name=wk1.getValue().split(" ");
        p= new Player(name[0],name[1]);
        p.makePlayerWithFirstLastName(t1.getTeam_ID());
        int w1=p.getPlayer_ID();

        match.setHostTeamCaptain(c1);
        match.setHostTeamWK(w1);

        if(isTeam2Confirm) startGameID.setVisible(true);
        int in_num;
        if(in1.getBatting_team()==t1.getTeam_ID()) in_num=1;
        else in_num=2;
        for(Player x:selectedPlayers1){
            x.makePlayerWithFirstLastName(t1.getTeam_ID());
            x.UpdateNumOfMatches(x.getPlayer_ID());
            BattingScoreBoard bb = new BattingScoreBoard(x.getPlayer_ID(),in_num,match.getMatchID());
            bb.insert();
        }

    }

    public void confirm2Action(ActionEvent event) throws SQLException {
        if(captain2.getSelectionModel().isEmpty()){
            JOptionPane.showMessageDialog(null,"Choose Captain");
            return;
        }
        else if(wk2.getSelectionModel().isEmpty()){
            JOptionPane.showMessageDialog(null,"Choose Wicket-Keeper");
            return;
        }
        isTeam2Confirm=true;
        team2.setDisable(true);
        team2ListVID.setDisable(true);
        team2ListViewSelectedID.setDisable(true);
        wk2.setDisable(true);
        captain2.setDisable(true);
        debut2ID.setDisable(true);

        if(isTeam1Confirm) startGameID.setVisible(true);

        String[] name=captain2.getValue().split(" ");
        Player p= new Player(name[0],name[1]);
        p.makePlayerWithFirstLastName(t2.getTeam_ID());
        int c2=p.getPlayer_ID();
        name=wk2.getValue().split(" ");
        p= new Player(name[0],name[1]);
        p.makePlayerWithFirstLastName(t2.getTeam_ID());
        int w2=p.getPlayer_ID();

        match.setVisitingTeamCaptain(c2);
        match.setVisitingTeamWK(w2);

        int in_num;
        if(in1.getBatting_team()==t2.getTeam_ID()) in_num=1;
        else in_num=2;
        for(Player x:selectedPlayers2){
            x.makePlayerWithFirstLastName(t2.getTeam_ID());
            x.UpdateNumOfMatches(x.getPlayer_ID());
            BattingScoreBoard bb= new BattingScoreBoard(x.getPlayer_ID(), in_num, match.getMatchID());
            bb.insert();
        }
    }

    public void confirmButtonAction(ActionEvent event) throws SQLException {
        if (tossID.getSelectionModel().isEmpty()){
            JOptionPane.showMessageDialog(null,"Click the toss winner");
            return;
        }
        else if(chooseID.getSelectionModel().isEmpty()) {JOptionPane.showMessageDialog(null,"Click the choice of toss winner");return;}
        else if(umpire1ID.getSelectionModel().isEmpty()){ JOptionPane.showMessageDialog(null,"Insert first Umpire");return;}
        else if(umpire2ID.getSelectionModel().isEmpty()){ JOptionPane.showMessageDialog(null,"Insert second Umpire");return;}
        else if(umpire3ID.getSelectionModel().isEmpty()){ JOptionPane.showMessageDialog(null,"Insert third Umpire");return;}

        String[]name =umpire1ID.getValue().split(" ");
        Umpire umpire= new Umpire(name[0], name[1]);
        umpire.makeUmpireWithFirstLastName();
        int u1=umpire.getUmpire_ID();
        name = umpire2ID.getValue().split(" ");
        umpire=new Umpire(name[0],name[1]);
        umpire.makeUmpireWithFirstLastName();
        int u2=umpire.getUmpire_ID();
        name = umpire3ID.getValue().split(" ");
        umpire=new Umpire(name[0],name[1]);
        umpire.makeUmpireWithFirstLastName();
        int u3=umpire.getUmpire_ID();
        String toss_winner= tossID.getValue();
        Team team=new Team(toss_winner);
        team.makeTeamWithSF();
        int tossW=team.getTeam_ID();
        String toss_decision=chooseID.getValue();

        match.setU1(u1);
        match.setU2(u2);
        match.setU3(u3);
        match.setTossWinner(tossW);
        match.setTossDecision(toss_decision);

        int tossLoser;
        if(match.getTossWinner()==match.getHostTeam()) {
            tossLoser=match.getVisitingTeam();
        }
        else {
            tossLoser=match.getHostTeam();
        }
        if(match.getTossDecision().equals("Elected to bat first")){
            in1= new Innings(1,match.getMatchID(),match.getTossWinner(),tossLoser);
            in2= new Innings(2,match.getMatchID(),tossLoser,match.getTossWinner());
        }
        else if(match.getTossDecision().equals("Elected to bowl first")){
            in2= new Innings(2,match.getMatchID(),match.getTossWinner(),tossLoser);
            in1= new Innings(1,match.getMatchID(),tossLoser,match.getTossWinner());
        }
        if(match.getMatchID()==3){
            match.updateDay(match.getMatchID());
        }

        team1.setVisible(true);
        tossID.setDisable(true);
        chooseID.setDisable(true);
        umpire1ID.setDisable(true);
        umpire2ID.setDisable(true);
        umpire3ID.setDisable(true);
    }
}
