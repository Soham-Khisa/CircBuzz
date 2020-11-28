package UpdateTables.TeamUpdate;

import Database.DatabaseConnection;
import JavaCode.Team;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TeamUpdateController implements Initializable {
    @FXML
    private TableView<Team> resultTable;
    @FXML
    private TableColumn<Team, String> title;
    @FXML
    private TableColumn<Team, ImageView> photo;
    @FXML
    private TextField searchbox;

    private ObservableList<Team> datalist = FXCollections.observableArrayList();
    private Team team;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        title.setCellValueFactory(new PropertyValueFactory<>("team_Name"));
        photo.setCellValueFactory(new PropertyValueFactory<Team, ImageView>("imageholder"));

        DatabaseConnection dc = new DatabaseConnection();
        String query = "SELECT TEAM_ID, TEAM_NAME, TEAM_SF, ESTABLISH_DATE, TEAM_LOGO, HEAD_COACH, BOARD_PRESIDENT" +
                " FROM CRICBUZZ.TEAM";
        ResultSet rs = dc.getQueryResult(query);

        try {
            while (rs.next()) {
                Integer id = rs.getInt("TEAM_ID");
                String name = rs.getString("TEAM_NAME");
                String sf = rs.getString("TEAM_SF");
                String coach = rs.getString("HEAD_COACH");
                String president = rs.getString("BOARD_PRESIDENT");

                java.sql.Date date = rs.getDate("ESTABLISH_DATE");
                java.util.Date edate = new java.sql.Date(date.getTime());

                Blob blob = rs.getBlob("TEAM_LOGO");
                ImageView imageView = new ImageView();
                imageView.setFitHeight(160);
                imageView.setFitWidth(160);

//                Circle myCircle = new Circle();
//                myCircle.setRadius(80.0f);
//                myCircle.setFill(Color.WHITE);

                if(blob != null) {
                    byte[] data = blob.getBytes(1, (int) blob.length());
                    Image image = new Image(new ByteArrayInputStream(data));
                    imageView.setImage(image);

//                    myCircle.setStroke(Color.SEAGREEN);
//                    myCircle.setFill(new ImagePattern(image));
//                    myCircle.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
                }

                team = new Team(id, name, sf, edate, coach, president, imageView);
                datalist.add(team);
            }
        }
        catch (SQLException e) {
            System.out.println("Failed to load result initialize UmpireUpdateController" + e);
        }

        FilteredList<Team> filterData = new FilteredList<>(datalist, b->true);
        searchbox.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filterData.setPredicate(tm -> {
                if(newValue == null || newValue.isEmpty())
                    return true;

                String input = newValue;
                if (tm.getTeam_Name().toLowerCase().indexOf(input) != -1) {
                    return true;
                }
                else    return false;

            });
        });

        SortedList<Team> teamSortedList = new SortedList<Team>(filterData);
        teamSortedList.comparatorProperty().bind(resultTable.comparatorProperty());
        resultTable.setItems(teamSortedList);

        title.setStyle("-fx-alignment:CENTER");
        photo.setStyle("-fx-alignment:CENTER");
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
