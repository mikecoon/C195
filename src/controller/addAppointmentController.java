package controller;

import DAO.appointmentDAO;
import DAO.userDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.time.*;

public class addAppointmentController {
    @FXML TextField addAppointmentID; //might not need
    @FXML TextField addAppointmentTitle;
    @FXML TextField addAppointmentType;
    @FXML TextField addAppointmentDescription;
    @FXML DatePicker addAppointmentStartDate;
    @FXML TextField addAppointmentLocation;
    @FXML DatePicker addAppointmentEndDate;
    @FXML ComboBox<String> addAppointmentStartTime;
    @FXML ComboBox<String> addAppointmentEndTime;
    @FXML ComboBox<Integer> addAppointmentCustomerID;
    @FXML ComboBox<String> addAppointmentContact; //might not need
    @FXML ComboBox<Integer> addAppointmentUserID; //might not need
    @FXML Button addAppointmentSaveButton;
    @FXML Button addAppointmentCancelButton;

    public void addAppointmentSaveButton(ActionEvent event) throws IOException, SQLException {
        try{
            //field variables
            String title = addAppointmentTitle.getText();
            String type = addAppointmentType.getText();
            String location = addAppointmentLocation.getText();
            String description = addAppointmentDescription.getText();
            Integer customerID = addAppointmentCustomerID.getValue();
            Integer userID = addAppointmentUserID.getValue();

            //Time/Date variables
            //String startDate = addAppointmentStartDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            //String endDate = addAppointmentEndDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate startDate = addAppointmentStartDate.getValue();
            LocalDate endDate = addAppointmentEndDate.getValue();
            DateTimeFormatter dtformat = DateTimeFormatter.ofPattern("HH:mm");
            LocalDateTime startDT = LocalDateTime.of(addAppointmentStartDate.getValue(),
                    LocalTime.parse(addAppointmentStartTime.getValue(), dtformat));
            LocalDateTime endDT = LocalDateTime.of(addAppointmentEndDate.getValue(),
                    LocalTime.parse(addAppointmentEndTime.getValue(), dtformat));
            //String startTime = addAppointmentStartTime.getValue();
            //String endTime = addAppointmentEndTime.getValue();
            //LocalDate localStartDate = addAppointmentStartDate.getValue();
            //LocalDate localEndDate = addAppointmentEndDate.getValue();

            //input validation
            if(!addAppointmentTitle.getText().isBlank() && !addAppointmentType.getText().isBlank() && !addAppointmentDescription.getText().isBlank() &&
                    addAppointmentStartDate.getValue() != null && !addAppointmentLocation.getText().isBlank() && addAppointmentEndDate.getValue() != null &&
                    addAppointmentStartTime.getValue() != null && addAppointmentEndTime.getValue() != null && addAppointmentCustomerID.getValue() != null ){

                checkDate(startDT,endDT,startDate,endDate);
                //validateOverlap

            }
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }

    public void addAppointmentCancelButton(ActionEvent event) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/appointments.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Appointments");
        window.setScene(scene);
        window.show();
    }

    public void checkOverlap(LocalDateTime startDT, LocalDateTime endDT, LocalDate startDate, LocalDate endDate, Integer customerID){
        ObservableList<Appointment> appointments = appointmentDAO.getCustomerAppointments()
    }

    public void checkDate(LocalDateTime startDT, LocalDateTime endDT, LocalDate startDate, LocalDate endDate ){
        ZonedDateTime hoursStart = ZonedDateTime.of(startDate, LocalTime.of(8,0), ZoneId.of("America/New_York"));
        ZonedDateTime hoursEnd = ZonedDateTime.of(endDate, LocalTime.of(22, 0), ZoneId.of("America/New_York"));
        ZonedDateTime zonedDTstart = ZonedDateTime.of(startDT, userDB.getTimeZone());
        ZonedDateTime zonedDTend = ZonedDateTime.of(endDT, userDB.getTimeZone());

        //Checks if inputted time and date complies with business hours.
        if(zonedDTstart.isAfter(zonedDTend) && zonedDTend.isBefore(hoursStart) && zonedDTstart.isBefore(hoursStart) && zonedDTstart.isBefore(hoursEnd) && zonedDTend.isBefore(hoursEnd)) {

            System.out.println("Date / Time is out of range.");
            String e = "Business hours are invalid, try again.";
            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalidInput = new Alert(Alert.AlertType.WARNING, e, clickOkay);
            invalidInput.showAndWait();
            return;

        }
    }


}
