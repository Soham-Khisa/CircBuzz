package UpdateTables.PlayerUpdate;

import Database.DatabaseConnection;
import JavaCode.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

public class PlayerUpdateController implements Initializable {
    @FXML
    private TableView<Player> resultTable;
    @FXML
    private TableColumn<Player, String> playername;
    @FXML
    private TableColumn<Player, String> playerteam;
    @FXML
    private TableColumn<Player, ImageView> photo;
    @FXML
    private TextField searchbox;
    @FXML
    private Label label;

    private ObservableList<Player> datalist = FXCollections.observableArrayList();
    private Player player;
    private HashMap<Integer, String> teamList;
    private PlayerUpdateFinalController pufc;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        playername.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        playerteam.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        photo.setCellValueFactory(new PropertyValueFactory<>("imageView"));

        DatabaseConnection dc = new DatabaseConnection();
        String teamQuery = "SELECT TEAM_ID, TEAM_NAME FROM CRICBUZZ.TEAM";
        ResultSet rs = dc.getQueryResult(teamQuery);
        teamList = new HashMap<Integer, String>();
        try {
            while (rs.next()) {
                String tname = rs.getString("TEAM_NAME");
                Integer id = rs.getInt("TEAM_ID");
                teamList.put(id, tname);
            }
        } catch (SQLException e) {
            System.out.println("Could not load the teams PlayerUpdateController.java :: " + e);
        }

        String query = "SELECT PLAYER_ID, FIRST_NAME, LAST_NAME, BORN, DOB, DEATH, ROLE, PROFILE_PIC, STATUS, TEAM_ID, JERSEY " +
                "FROM CRICBUZZ.PLAYER";

        rs = dc.getQueryResult(query);
        try {
            while (rs.next()) {
                Integer pid = rs.getInt("PLAYER_ID");
                String fname = rs.getString("FIRST_NAME");
                String lname = rs.getString("LAST_NAME");
                String birthplace = rs.getString("BORN");
                String role = rs.getString("ROLE");
                String status = rs.getString("STATUS");
                Integer teamid = rs.getInt("TEAM_ID");
                Integer jersey = rs.getInt("JERSEY");
                String tmName = teamList.get(teamid);

                java.sql.Date birthdate = rs.getDate("DOB");
                java.util.Date dob = new java.sql.Date(birthdate.getTime());

                java.sql.Date deathdate = rs.getDate("DEATH");
                java.util.Date death = null;
                if(deathdate!=null) {
                    death = new java.sql.Date(deathdate.getTime());
                }

                Blob blob = rs.getBlob("PROFILE_PIC");
                ImageView imageView = new ImageView();
                imageView.setFitHeight(160);
                imageView.setFitWidth(160);
                if(blob != null) {
                    byte[] data = blob.getBytes(1, (int) blob.length());
                    Image image = new Image(new ByteArrayInputStream(data));
                    imageView.setImage(image);
                }
                player = new Player(pid, fname, lname, status, birthplace, dob, death, role, teamid, jersey, tmName, imageView);
                datalist.add(player);
            }
        } catch (SQLException e) {
            System.out.println("Failed to load the result(Resultset) PlayerUpdate :: " + e);
        }

        FilteredList<Player> filterData = new FilteredList<>(datalist, b->true);
        searchbox.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filterData.setPredicate(plr -> {
                if(newValue == null || newValue.isEmpty())
                    return true;

                String input = newValue.toLowerCase();
                if (plr.getFullname().toLowerCase().indexOf(input) != -1) {
                    return true;
                }
                else if(plr.getTeamName().toLowerCase().indexOf(input) != -1) {
                    return true;
                }
                else    return false;
            });
        });

        SortedList<Player> teamSortedList = new SortedList<Player>(filterData);
        teamSortedList.comparatorProperty().bind(resultTable.comparatorProperty());
        resultTable.setItems(teamSortedList);

        playername.setStyle("-fx-alignment:CENTER");
        playerteam.setStyle("-fx-alignment:CENTER");
        photo.setStyle("-fx-alignment:CENTER");

        try {
            dc.closeConnection();
        } catch (SQLException e) {
            System.out.println("Failed to close connection in PlayerUpdateController.java :: " + e);
        }

        resultTable.setRowFactory( rst -> {
            TableRow<Player> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && (! row.isEmpty()) )
                    rowClickEvent();
            });
            return row;
        });
    }

    public void rowClickEvent() {
        player = null;
        player = resultTable.getSelectionModel().getSelectedItem();

        if(player == null)    return;

        String text = "Are your sure to edit and update information on the selected player?";
        int response = JOptionPane.showConfirmDialog(null, text, "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if(response == JOptionPane.YES_OPTION) {
            FXMLLoader loader = new FXMLLoader();
            Parent root = null;
            try {
                root = loader.load(getClass().getResource("/UpdateTables/PlayerUpdate/PlayerUpdateFinal.fxml").openStream());
            } catch (IOException e) {
                System.out.println("PlayerUpdateFinal.fxml file not found " + e);;
            }
            Stage window = (Stage) label.getScene().getWindow();
            window.setScene(new Scene(root));
            //pass the essential player instance as parameter
            pufc = loader.getController();
            pufc.setPlayer(player);
            pufc.setPlayerFinalController();
            window.show();
        }
    }

    public void goBack(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/UpdateTables/updateinfo.fxml"));
            Stage window= (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(root));
            window.show();
        } catch (IOException e) {
            System.out.println("Failed to load goBack UpdateTables\\UmpireUpdate\\UmpireUpdateController :: " + e);;
        }
    }
}
