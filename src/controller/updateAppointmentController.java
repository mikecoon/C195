package controller;

import DAO.appointmentDAO;
import DAO.contactDAO;
import DAO.userDAO;
import helper.JDBC;
import javafx.collections.FXCollections;
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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;

/** Handles updating preexisting appointments in the database*/
public class updateAppointmentController {

    @FXML TextField updateAppointmentID; //might not need
    @FXML TextField updateAppointmentTitle;
    @FXML TextField updateAppointmentType;
    @FXML TextField updateAppointmentDescription;
    @FXML
    DatePicker updateAppointmentStartDate;
    @FXML TextField updateAppointmentLocation;
    @FXML DatePicker updateAppointmentEndDate;
    @FXML
    ComboBox<String> updateAppointmentStartTime;
    @FXML ComboBox<String> updateAppointmentEndTime;
    @FXML ComboBox<Integer> updateAppointmentCustomerID;
    @FXML ComboBox<String> updateAppointmentContact;
    @FXML ComboBox<Integer> updateAppointmentUserID; //might not need
    @FXML
    Button updateAppointmentSaveButton;
    @FXML Button updateAppointmentCancelButton;

    /**
     * Saves updated appointments and updates in the database
     * validates that all inputs are valid, alerts user of errors
     * @param event
     * @throws SQLException
     * @throws IOException
     */
    public void updateAppointmentSaveButton(ActionEvent event) throws SQLException, IOException {
        try {

            //field variables
            Integer id = Integer.parseInt(updateAppointmentID.getText());
            String title = updateAppointmentTitle.getText();
            String type = updateAppointmentType.getText();
            String location = updateAppointmentLocation.getText();
            String description = updateAppointmentDescription.getText();
            Integer customerID = updateAppointmentCustomerID.getValue();
            Integer userID = updateAppointmentUserID.getValue();
            String contactName = updateAppointmentContact.getValue();
            String contactID = contactDAO.getContactByID(contactName);

            //time vars
            LocalDate startDate = updateAppointmentStartDate.getValue();
            LocalDate endDate = updateAppointmentEndDate.getValue();
            LocalDateTime startDT = null;
            LocalDateTime endDT = null;

            try {
                DateTimeFormatter dtformat = DateTimeFormatter.ofPattern("HH:mm");
                 startDT = LocalDateTime.of(updateAppointmentStartDate.getValue(),
                        LocalTime.parse(updateAppointmentStartTime.getValue(), dtformat));
                 endDT = LocalDateTime.of(updateAppointmentEndDate.getValue(),
                        LocalTime.parse(updateAppointmentEndTime.getValue(), dtformat));
            }catch(NullPointerException e){
                System.out.println("Entered the time wrong.");

                startDate = null;
                endDate = null;

            }



            //validate user input
            if(title.isBlank() || type.isBlank() || location.isBlank() || description.isBlank() || customerID == null ||
                    userID == null || contactName == null || startDate == null || endDate == null || startDT == null || endDT == null){

                //throw error, maybe validate on specific errors?
                System.out.println("Input error.");
                String e = "Input error in one or more fields, Make sure you enter valid input for all fields, try again.";
                ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert invalidInput = new Alert(Alert.AlertType.WARNING, e, clickOkay);
                invalidInput.showAndWait();
                return;

            } else{

                Boolean validateDT = checkDateTime(startDT,endDT,startDate,endDate);
                if(!validateDT){
                    System.out.println("Input error.");
                    String e = "Error with dates or times entered. Make sure appointment is within business days and hours of operation. Make sure start and end time arent the same and that start time is before end time. Make sure the start and end dates are the same.";
                    ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                    Alert invalidInput = new Alert(Alert.AlertType.WARNING, e, clickOkay);
                    invalidInput.showAndWait();
                    return;
                }

                Boolean validateOverlap = checkOverlap(startDT,endDT,startDate,endDate, customerID);
                if(!validateOverlap){
                    System.out.println("Input error.");
                    String e = "Error with dates or times entered. Make sure appointment is within business days and hours of operation. Make sure start and end time arent the same and that start time is before end time.";
                    ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                    Alert invalidInput = new Alert(Alert.AlertType.WARNING, e, clickOkay);
                    invalidInput.showAndWait();
                    return;
                }

                //end of error handling
                String currentUser = userDAO.getCurrentUser().getUserName();
                ZonedDateTime zonedStartDT = ZonedDateTime.of(startDT,userDAO.getTimeZone()).withZoneSameInstant(ZoneOffset.UTC);
                ZonedDateTime zonedEndDT = ZonedDateTime.of(endDT,userDAO.getTimeZone()).withZoneSameInstant(ZoneOffset.UTC);

                String sql = "UPDATE appointments SET Title=?, Description=?, Location=?, Type=?, Start=?, End=?, Create_Date=?, Created_By=?, Last_Update=?,Last_Updated_By=?, " +
                        "Customer_ID=?, User_ID=?, Contact_ID=? WHERE Appointment_ID = ?";
                PreparedStatement ps = JDBC.connection.prepareStatement(sql);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String start = zonedStartDT.format(formatter);
                String end = zonedEndDT.format(formatter);

                ps.setString(1, title);
                ps.setString(2, description);
                ps.setString(3, location);
                ps.setString(4, type);
                ps.setString(5, start);
                ps.setString(6, end);
                ps.setString(7, ZonedDateTime.now(ZoneOffset.UTC).format(formatter));
                ps.setString(8, currentUser);
                ps.setString(9, ZonedDateTime.now(ZoneOffset.UTC).format(formatter));
                ps.setString(10, currentUser);
                ps.setInt(11, customerID);
                ps.setInt(12, userID);
                ps.setString(13, contactID);
                //implement
                ps.setString(14, id.toString());

                try{
                    ps.executeUpdate();
                    ps.close();
                } catch(SQLException e){
                    e.printStackTrace();
                    ps.close();
                    ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                    Alert invalidInput = new Alert(Alert.AlertType.WARNING, "Unable to update appointment, try again.", clickOkay);
                    invalidInput.showAndWait();
                }

                ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Appointment succesfully updated.", clickOkay);
                alert.showAndWait();
                Parent parent = FXMLLoader.load(getClass().getResource("/view/appointments.fxml"));
                Scene scene = new Scene(parent);
                Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
                window.setTitle("Appointments");
                window.setScene(scene);
                window.show();


            }
        } catch(Error e){
            System.out.println(e);
        }

    }

    /**
     * cancels an appointment update, redirects back to appointmentView
     * @param event
     * @throws Exception
     */
    public void updateAppointmentCancelButton(ActionEvent event) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/appointments.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Appointments");
        window.setScene(scene);
        window.show();
    }

    /**
     * Method checks the date to make sure it is between business hours.
     * @param startDT start date time
     * @param endDT end date tine
     * @param startDate start date of appt
     * @param endDate end date of appt
     */
    public Boolean checkDateTime(LocalDateTime startDT, LocalDateTime endDT, LocalDate startDate, LocalDate endDate ){
        //get the hours of start and end of appt time
        ZonedDateTime hoursStart = ZonedDateTime.of(startDate, LocalTime.of(8,0), ZoneId.of("America/New_York"));
        ZonedDateTime hoursEnd = ZonedDateTime.of(endDate, LocalTime.of(22, 0), ZoneId.of("America/New_York"));
        ZonedDateTime zonedDTstart = ZonedDateTime.of(startDT, userDAO.getTimeZone());
        ZonedDateTime zonedDTend = ZonedDateTime.of(endDT, userDAO.getTimeZone());

        DayOfWeek startDay = startDate.getDayOfWeek();
        DayOfWeek endDay = endDate.getDayOfWeek();
        int startDayInt = startDay.getValue();
        int endDayInt = endDay.getValue();
        int weekBegin = DayOfWeek.MONDAY.getValue();
        int weekEnd = DayOfWeek.FRIDAY.getValue();


        //validate correct date

        //weekend appointments should be allowed.
        /*
        if(startDayInt < weekBegin || startDayInt > weekEnd || endDayInt < weekBegin || endDayInt > weekEnd){
            return false;
        }

         */

        //validate time within business hours
        if(zonedDTstart.isBefore(hoursStart) | zonedDTstart.isAfter(hoursEnd) | zonedDTend.isBefore(hoursStart)
        | zonedDTend.isAfter(hoursEnd) | zonedDTstart.isAfter(hoursEnd)){
            return false;
        }
        //validate start before end
        if(startDT.isAfter(endDT)){
            return false;
        }
        //validate start not equal to end
        if(startDT.isEqual(endDT)){
            return false;
        }
        if(startDayInt != endDayInt){
            return false;
        }

        return true;
    }

    /**
     * Method checks to make sure new appointment doesnt overlap with a preexisting appointment.
     * @param startDT start date time
     * @param endDT end date time
     * @param startDate start date of appt
     * @param endDate end date of appt
     * @param customerID is the customers ID
     * @throws SQLException
     */
    public Boolean checkOverlap(LocalDateTime startDT, LocalDateTime endDT, LocalDate startDate, LocalDate endDate, Integer customerID) throws SQLException{
        ObservableList<Appointment> appointments = appointmentDAO.getCustomerAppointments(startDate,endDate,customerID);

        for (Appointment appointment : appointments){
            LocalDateTime start = appointment.getStartDateTime().toLocalDateTime();
            LocalDateTime end = appointment.getEndDateTime().toLocalDateTime();

            if(start.isEqual(startDT) & end.isEqual(endDT)){
                System.out.println("Duplicate appointment, error.");
                String e = "An appointment already exists at this time for this customer, try again.";
                ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert invalidInput = new Alert(Alert.AlertType.WARNING, e, clickOkay);
                invalidInput.showAndWait();
                return true;
            }

            else if ((start.isBefore(startDT) & end.isAfter(endDT)) || (start.isBefore(endDT) & start.isAfter(startDT)) || (end.isBefore(endDT) & end.isAfter(startDT))){

                System.out.println("Customer overlap, error.");
                String e = "There is an overlap in customer appointments, try again.";
                ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert invalidInput = new Alert(Alert.AlertType.WARNING, e, clickOkay);
                invalidInput.showAndWait();
                return false;
            }
        }
        return true;

    }

    /**
     * Populates needed fields on the updateAppointmentPage upon initialization
     * @param selectedAppointment
     */
    public void populateFields(Appointment selectedAppointment){
        try{
            try{
                LocalDate startDate = selectedAppointment.getStartDateTime().toLocalDateTime().toLocalDate();
                LocalDate endDate = selectedAppointment.getEndDateTime().toLocalDateTime().toLocalDate();
            }catch(NullPointerException e){
                ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert invalidInput = new Alert(Alert.AlertType.WARNING, "There are no dates selected.", clickOkay);
                invalidInput.showAndWait();
                return;
            }

            ZonedDateTime startDT = selectedAppointment.getStartDateTime().toInstant().atZone(ZoneOffset.UTC);
            ZonedDateTime endDT = selectedAppointment.getEndDateTime().toInstant().atZone(ZoneOffset.UTC);

            ZonedDateTime localStartDT = startDT.withZoneSameInstant(userDAO.getTimeZone());
            ZonedDateTime localEndDT = endDT.withZoneSameInstant(userDAO.getTimeZone());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String startTime = localStartDT.format(formatter);
            String endTime = localEndDT.format(formatter);

            updateAppointmentID.setText(selectedAppointment.getAppointmentID().toString());
            updateAppointmentTitle.setText(selectedAppointment.getTitle());
            updateAppointmentType.setText(selectedAppointment.getType());
            updateAppointmentDescription.setText(selectedAppointment.getDescription());
            updateAppointmentStartDate.setValue(selectedAppointment.getStartDateTime().toLocalDateTime().toLocalDate());
            updateAppointmentLocation.setText(selectedAppointment.getLocation());
            updateAppointmentEndDate.setValue(selectedAppointment.getEndDateTime().toLocalDateTime().toLocalDate());
            //for combo boxes, populate them first than set values
            updateAppointmentStartTime.setItems(getAppointmentTimes());
            updateAppointmentStartTime.getSelectionModel().select(startTime);

            updateAppointmentEndTime.setItems(getAppointmentTimes());
            updateAppointmentEndTime.getSelectionModel().select(endTime);

            updateAppointmentCustomerID.setItems(appointmentDAO.getCustomerIDs());
            updateAppointmentCustomerID.getSelectionModel().select(selectedAppointment.getCustomerID());

            updateAppointmentContact.setItems(contactDAO.getContactNames());
            updateAppointmentContact.getSelectionModel().select(selectedAppointment.getContactName());

            updateAppointmentUserID.setItems(userDAO.getUserIDs());
            updateAppointmentUserID.getSelectionModel().select(selectedAppointment.getUserID());

    }catch (SQLException e){
            e.printStackTrace();
        }
}

    /**
     * Method gets all of the appt times for initialization and population
     */
    public ObservableList<String> getAppointmentTimes(){
        ObservableList<String> apptTimes = FXCollections.observableArrayList();
        LocalTime startAppointment = LocalTime.MIN.plusHours(8);
        LocalTime endAppointment = LocalTime.MAX.minusHours(1).minusMinutes(45);
        if (!startAppointment.equals(0) || !endAppointment.equals(0)){
            while(startAppointment.isBefore(endAppointment)){
                apptTimes.add(String.valueOf(startAppointment));
                startAppointment = startAppointment.plusMinutes(15);
            }
        }
        return apptTimes;
    }
}