package UpdateTables.UmpireUpdate;

import Database.DatabaseConnection;
import JavaCode.Stadium;
import JavaCode.Umpire;
import PlayerEnrol.PlayerController;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UmpireUpdateController extends Component implements Initializable {
    @FXML
    private Label label;
    @FXML
    private TextField searchbox;
    @FXML
    private TableView<Umpire> resultTable;
    @FXML
    private TableColumn<Umpire, String> name;
    @FXML
    private TableColumn<Umpire, String> country;
    @FXML
    private TableColumn<Umpire, String> status;
    @FXML
    private TableColumn<Umpire, Integer> age;

    private ObservableList<Umpire> datalist = FXCollections.observableArrayList();
    private UmpireConfirmController ucc = null;
    private Umpire umpire;

    @Override
    public void initialize (URL url, ResourceBundle rb) {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        country.setCellValueFactory(new PropertyValueFactory<>("country"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        age.setCellValueFactory(new PropertyValueFactory<>("age"));

        DatabaseConnection dc = new DatabaseConnection();

        String query = "SELECT * FROM CRICBUZZ.UMPIRE";
        ResultSet rs = dc.getQueryResult(query);
        try {
            while (rs.next()) {
                Integer id = rs.getInt("UMPIRE_ID");
                String fname = rs.getString("FIRST_NAME");
                String lname = rs.getString("LAST_NAME");
                String umpcountry = rs.getString("COUNTRY");
                String status = rs.getString("STATUS");

                java.sql.Date date = rs.getDate("DOB");
                java.util.Date dob = new java.sql.Date(date.getTime());

                umpire = new Umpire(id, fname, lname, umpcountry, status, dob);
                datalist.add(umpire);
            }
        }
        catch (SQLException e) {
            System.out.println("Failed to load result initialize UmpireUpdateController" + e);
        }

        FilteredList<Umpire> filterData = new FilteredList<>(datalist, b->true);
        searchbox.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filterData.setPredicate(ump -> {
                if(newValue == null || newValue.isEmpty())
                    return true;

                String input = newValue;
                boolean numeric = isNumeric(input);
                if(numeric == false)    input = input.toLowerCase();
                else    return false;

                if (ump.getName().toLowerCase().indexOf(input) != -1) {
                    return true;
                }
                else if (ump.getCountry().toLowerCase().indexOf(input) != -1) {
                    return true;
                }
                else if (ump.getStatus().toLowerCase().indexOf(input) != -1) {
                    return true;
                }
                else    return false;

            });
        });

        SortedList<Umpire> umpireSortedList = new SortedList<Umpire>(filterData);
        umpireSortedList.comparatorProperty().bind(resultTable.comparatorProperty());
        resultTable.setItems(umpireSortedList);

        name.setStyle("-fx-alignment:CENTER");
        country.setStyle("-fx-alignment:CENTER");
        age.setStyle("-fx-alignment:CENTER");
        status.setStyle("-fx-alignment:CENTER");

        resultTable.setOnMouseClicked(e -> {
            rowClickEvent();
        });
    }

    private void rowClickEvent() {
        umpire = null;
        umpire = resultTable.getSelectionModel().getSelectedItem();
        if(umpire==null)    return;

        String text = "Are your sure to update information on the selected umpire?";
        int response = JOptionPane.showConfirmDialog(null, text, "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if(response == JOptionPane.YES_OPTION) {
            FXMLLoader loader = new FXMLLoader();
            Parent root = null;
            try {
                root = loader.load(getClass().getResource("/UpdateTables/UmpireUpdate/UmpireConfirm.fxml").openStream());
            } catch (IOException e) {
                System.out.println("UmpireConfirmController.fxml file not found " + e);;
            }
            Stage window = (Stage) label.getScene().getWindow();
            window.setScene(new Scene(root));
            ucc = (UmpireConfirmController) loader.getController();
            ucc.setUmpireConfirmController(umpire);
            window.show();
        }
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
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
