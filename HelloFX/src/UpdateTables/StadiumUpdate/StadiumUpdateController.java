package UpdateTables.StadiumUpdate;

import Database.DatabaseConnection;
import JavaCode.Stadium;
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
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StadiumUpdateController implements Initializable {
    @FXML
    private TableView<Stadium> resultTable;
    @FXML
    private TableColumn<Stadium, String> stdname;
    @FXML
    private TableColumn<Stadium, String> country;
    @FXML
    private TextField searchbox;
    @FXML
    private Label label;

    private ObservableList<Stadium> datalist = FXCollections.observableArrayList();
    private Stadium stadium = null;
    private StadiumUpdateFinalController sufc = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stdname.setCellValueFactory(new PropertyValueFactory<>("stadium_name"));
        country.setCellValueFactory(new PropertyValueFactory<>("country"));
        DatabaseConnection dc = new DatabaseConnection();
        String query = "SELECT * FROM CRICBUZZ.STADIUM";

        ResultSet rs = dc.getQueryResult(query);

        try {
            while (rs.next()) {
                Integer id = rs.getInt("STADIUM_ID");
                String name = rs.getString("STADIUM_NAME");
                String location = rs.getString("LOCATION");
                String state = rs.getString("COUNTRY");
                Integer capacity = rs.getInt("CAPACITY");

                stadium = new Stadium(id, name, location, state, capacity);
                datalist.add(stadium);
            }
        } catch (SQLException e) {
            System.out.println("Failed to get result StadiumUpdate :: " + e);
        }

        FilteredList<Stadium> filterData = new FilteredList<>(datalist, b->true);
        searchbox.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filterData.setPredicate(std -> {
                if(newValue == null || newValue.isEmpty())
                    return true;

                String input = newValue.toLowerCase();

                if (std.getStadium_name().toLowerCase().indexOf(input) != -1) {
                    return true;
                }
                else if (std.getCountry().toLowerCase().indexOf(input) != -1) {
                    return true;
                }
                else    return false;

            });
        });

        SortedList<Stadium> umpireSortedList = new SortedList<Stadium>(filterData);
        umpireSortedList.comparatorProperty().bind(resultTable.comparatorProperty());
        resultTable.setItems(umpireSortedList);

        stdname.setStyle("-fx-alignment:CENTER");
        country.setStyle("-fx-alignment:CENTER");

        resultTable.setOnMouseClicked(e -> {
            rowClickEvent();
        });

    }

    public void rowClickEvent() {
        stadium = null;
        stadium = resultTable.getSelectionModel().getSelectedItem();
        if(stadium == null)    return;

        String text = "Are your sure to update information on the selected stadium?";
        int response = JOptionPane.showConfirmDialog(null, text, "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if(response == JOptionPane.YES_OPTION) {
            FXMLLoader loader = new FXMLLoader();
            Parent root = null;
            try {
                root = loader.load(getClass().getResource("/UpdateTables/StadiumUpdate/StadiumUpdateFinal.fxml").openStream());
            } catch (IOException e) {
                System.out.println("UmpireConfirmController.fxml file not found " + e);;
            }
            Stage window = (Stage) label.getScene().getWindow();
            window.setScene(new Scene(root));
            sufc = (StadiumUpdateFinalController) loader.getController();
            sufc.setStadium(stadium);
            sufc.setStadiumUpdateFinal();
            window.show();
        }
    }

    @FXML
    void goBack(ActionEvent event) {
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
