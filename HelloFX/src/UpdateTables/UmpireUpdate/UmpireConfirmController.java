package UpdateTables.UmpireUpdate;

import Database.DatabaseConnection;
import JavaCode.Umpire;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class UmpireConfirmController implements Initializable {
    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private TextField country;
    @FXML
    private ComboBox<String> status;
    @FXML
    private DatePicker dob;
    @FXML
    private Label origfname;
    @FXML
    private Label origlname;
    @FXML
    private Label origcountry;
    @FXML
    private Label origstatus;
    @FXML
    private Label origdob;
    @FXML
    private Button button;
    @FXML
    private Label updatemessage;

    private List<String> statusList;
    private Umpire umpire;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        statusList = new ArrayList<>();
        statusList = List.of("Active", "Retired");
    }

    public void setUmpireConfirmController(Umpire ump) {
        this.umpire = ump;
        if(umpire == null) {
            System.out.println("sorry umpire does not exist ");return;}

        firstname.setText(umpire.getFirst_name());
        lastname.setText(umpire.getLast_name());
        country.setText(umpire.getCountry());
        ObservableList<String> observeStatus = FXCollections.observableArrayList(statusList);
        status.setItems(observeStatus);

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(umpire.getDob());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        dob.setValue(LocalDate.of(year, month, day));
    }

    public void showOrhideOriginal(ActionEvent event) {
        if(button.getText().equals("SHOW ORIGINALS")) {
            button.setText("HIDE ORIGINALS");
            origfname.setText(umpire.getFirst_name());
            origlname.setText(umpire.getLast_name());
            origcountry.setText(umpire.getCountry());
            origstatus.setText(umpire.getStatus());

            String pattern = "dd/MM/yyyy";
            DateFormat df = new SimpleDateFormat(pattern);
            String dobstring = df.format(umpire.getDob());
            origdob.setText(dobstring);
        }
        else {
            button.setText("SHOW ORIGINALS");
            origfname.setText("");
            origlname.setText("");
            origcountry.setText("");
            origstatus.setText("");
            origdob.setText("");
        }
    }

    public void updateUmpire(ActionEvent event) {
        if(firstname.getText().isBlank() || lastname.getText().isBlank() || country.getText().isBlank() || status.getValue()==null || dob.getValue()==null) {
            JOptionPane.showMessageDialog(null, "Please fill out all the fields");
            return;
        }
        DatabaseConnection dc = new DatabaseConnection();

        java.util.Date date = java.util.Date.from(dob.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        String querydate = dob.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        String updatequery = "UPDATE CRICBUZZ.UMPIRE SET " +
                "FIRST_NAME = '" + firstname.getText() + "', LAST_NAME = '" + lastname.getText() + "', " +
                "COUNTRY = '" + country.getText() + "', STATUS = '" + status.getValue() + "', DOB = TO_DATE('" + querydate + "', 'DD/MM/YYYY')" +
                " WHERE UMPIRE_ID = " + umpire.getID();

        boolean bool = dc.doUpdate(updatequery);
        if(bool) {
            updatemessage.setText("UPDATE IS SUCCESSFUL");
            umpire.setFirst_name(firstname.getText());
            umpire.setLast_name(lastname.getText());
            umpire.setCountry(country.getText());
            umpire.setStatus(status.getValue());
            umpire.setDob(date);

            if(button.getText().equals("HIDE ORIGINALS")) {
                origfname.setText(umpire.getFirst_name());
                origlname.setText(umpire.getLast_name());
                origcountry.setText(umpire.getCountry());
                origstatus.setText(umpire.getStatus());

                String pattern = "dd/MM/yyyy";
                DateFormat df = new SimpleDateFormat(pattern);
                String dobstring = df.format(umpire.getDob());
                origdob.setText(dobstring);
            }
        }
        else    JOptionPane.showMessageDialog(null, "Failed to update");
    }


    public void backToUmpireUpdate(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/UpdateTables/UmpireUpdate/UmpireUpdate.fxml"));
            Stage window= (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(new Scene(root));
            window.show();
        } catch (IOException e) {
            System.out.println("Failed to load UmpireUpdate UpdateTables\\UmpireUpdate\\UmpireUpdateController :: " + e);;
        }
    }

}
