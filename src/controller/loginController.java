package controller;

import DAO.appointmentDAO;
import DAO.userDAO;
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
import model.Appointment;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/** Class for logging a user in, logging all log in attempts, and changing language based on user location / language settings*/
public class loginController implements Initializable {
    @FXML private TextField loginUsername;
    @FXML private TextField loginPassword;
    @FXML private Label loginLabel;
    @FXML private Label loginUsernameLabel;
    @FXML private Label loginPasswordLabel;
    @FXML private Label loginLocationLabel;
    @FXML private TextField loginLocation;
    @FXML private Button loginLoginButton;
    @FXML private Button loginExitButton;



    /**
     * Initializes log in page, changes text and notifications based on users location/language settings
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Locale local = Locale.getDefault();
        Locale.setDefault(local);
        ZoneId zoneId = ZoneId.systemDefault();
        resourceBundle = ResourceBundle.getBundle("languages/lang");

        loginLabel.setText(resourceBundle.getString("loginLabel"));
        loginUsernameLabel.setText(resourceBundle.getString("loginUsernameLabel"));
        loginPasswordLabel.setText(resourceBundle.getString("loginPasswordLabel"));
        loginLoginButton.setText(resourceBundle.getString("loginLoginButton"));
        loginExitButton.setText(resourceBundle.getString("loginExitButton"));
        loginLocationLabel.setText(resourceBundle.getString("loginLocationLabel"));

        loginLocation.setText(String.valueOf(zoneId));


        System.out.println("Log In initialized!");

    }

    /**
     * Initializes log in page, changes text and notifications based on users location/language settings
     * Validates username and password, writes all log in attempt data to text file
     * @param event
     * @throws Exception
     * @throws IOException
     * @throws SQLException
     */
    public void loginButton(ActionEvent event) throws Exception, IOException, SQLException {

        FileWriter writeToFile = new FileWriter("login_activity.txt",true);
        //PrintWriter printToFile = new PrintWriter(writeToFile);

        String userName = loginUsername.getText();
        String password = loginPassword.getText();

        boolean login = userDAO.AttemptLogin(userName, password);

        ObservableList<Appointment> appointments = appointmentDAO.getAllAppointments();
        LocalDateTime last15 = LocalDateTime.now().minusMinutes(15);
        LocalDateTime next15 = LocalDateTime.now().plusMinutes(15);

        if (login) {

            //successful login
            writeToFile.write(userName + " was able to successfully log in at " + Timestamp.valueOf(LocalDateTime.now()) + "\n");
            writeToFile.close();

            Parent parent = FXMLLoader.load(getClass().getResource("/view/appointments.fxml"));
            Scene scene = new Scene(parent);
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setTitle("Appointments");
            window.setScene(scene);
            window.show();

            int id = 0;
            LocalDateTime start = null;
            boolean upcoming = false;

            for(Appointment appointment: appointments){
                start = appointment.getStartDateTime().toLocalDateTime();
                if((start.isEqual(next15) || start.isBefore(next15)) && (start.isEqual(last15) || start.isAfter(last15))){
                    id = appointment.getAppointmentID();
                    upcoming = true;
                    break;
                }
            }
            if(upcoming){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Upcoming appointment within 15 minutes of now: " + id + " that starts at: " + start);
                Optional<ButtonType> confirm = alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "There are no upcoming appointments!");
                Optional<ButtonType> confirm = alert.showAndWait();
            }
        } else {
            //unsuccessful login
            writeToFile.write(userName + " was unsuccessful in loggin in at " + Timestamp.valueOf(LocalDateTime.now()) + "\n");
            writeToFile.close();
            ResourceBundle resourceBundle = ResourceBundle.getBundle("languages/lang");
            Alert alert = new Alert(Alert.AlertType.WARNING, resourceBundle.getString("loginError"));
            Optional<ButtonType> confirm = alert.showAndWait();
        }

    }

    /**
     * exits the applications
     * @param event
     */
    public void exitProgram(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();


    }
}
