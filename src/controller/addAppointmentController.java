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

/** Class to add an appointment to the database*/
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
    @FXML ComboBox<String> addAppointmentContact;
    @FXML ComboBox<Integer> addAppointmentUserID; //might not need
    @FXML Button addAppointmentSaveButton;
    @FXML Button addAppointmentCancelButton;

    /**
     * Initializes the addControllerViews combo boxes
     * lambda function to populate the customerID combobox without having to have a for loop to go over all the customerIDs
     * @throws SQLException
     */
    public void initialize() throws SQLException {
        addAppointmentContact.setItems(contactDAO.getContactNames());
        addAppointmentStartTime.setItems(getAppointmentTimes());
        addAppointmentEndTime.setItems(getAppointmentTimes());
        addAppointmentUserID.setItems(userDAO.getUserIDs());

        ObservableList<Integer> customerIDList = appointmentDAO.getCustomerIDs();
        ObservableList<Integer> allCustomers = FXCollections.observableArrayList();

        //lambda function
        customerIDList.forEach(customerID -> allCustomers.add(customerID));

        addAppointmentCustomerID.setItems(customerIDList);

    }

    /**
     * Saves the appointment to the database
     * validates user input and alerts if any errors
     * @throws SQLException
     * @throws IOException
     * @param event
     */
    public void addAppointmentSaveButton(ActionEvent event) throws IOException, SQLException {
        try{
            //field variables
            String title = addAppointmentTitle.getText();
            String type = addAppointmentType.getText();
            String location = addAppointmentLocation.getText();
            String description = addAppointmentDescription.getText();
            Integer customerID = addAppointmentCustomerID.getValue();
            Integer userID = addAppointmentUserID.getValue();
            String contactName = addAppointmentContact.getValue();


            System.out.println("Got here1");
            //input validation
            if(!addAppointmentTitle.getText().isBlank() && !addAppointmentType.getText().isBlank() && !addAppointmentDescription.getText().isBlank() &&
                    addAppointmentStartDate.getValue() != null && !addAppointmentLocation.getText().isBlank() && addAppointmentEndDate.getValue() != null &&
                    addAppointmentStartTime.getValue() != null && addAppointmentEndTime.getValue() != null && addAppointmentCustomerID.getValue() != null ){
                System.out.println("Got here2");

                //put in try statement
                LocalDate startDate = addAppointmentStartDate.getValue();
                LocalDate endDate = addAppointmentEndDate.getValue();
                DateTimeFormatter dtformat = DateTimeFormatter.ofPattern("HH:mm");
                LocalDateTime startDT = LocalDateTime.of(addAppointmentStartDate.getValue(),
                        LocalTime.parse(addAppointmentStartTime.getValue(), dtformat));
                LocalDateTime endDT = LocalDateTime.of(addAppointmentEndDate.getValue(),
                        LocalTime.parse(addAppointmentEndTime.getValue(), dtformat));

                if(checkDate(startDT,endDT,startDate,endDate)){
                    System.out.println("Date / Time is out of range.");
                    String e = "Error with dates or times entered. Make sure appointment is within business days and hours of operation. Make sure start and end time arent the same and that start time is before end time. Make sure the start and end dates are the same.";
                    ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                    Alert invalidInput = new Alert(Alert.AlertType.WARNING, e, clickOkay);
                    invalidInput.showAndWait();
                    return;
                }
                if(checkOverlap(startDT,endDT,startDate,endDate,customerID)){
                    System.out.println("Customer overlap, error.");
                    String e = "There is an overlap in customer appointments, try again.";
                    ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                    Alert invalidInput = new Alert(Alert.AlertType.WARNING, e, clickOkay);
                    invalidInput.showAndWait();
                    return;
                }

                String currentUser = userDAO.getCurrentUser().getUserName();
                ZonedDateTime zonedStartDT = ZonedDateTime.of(startDT,userDAO.getTimeZone()).withZoneSameInstant(ZoneOffset.UTC);
                ZonedDateTime zonedEndDT = ZonedDateTime.of(endDT,userDAO.getTimeZone()).withZoneSameInstant(ZoneOffset.UTC);

                //put in a try
                String sql = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement ps = JDBC.connection.prepareStatement(sql);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String start = zonedStartDT.format(formatter);
                String end = zonedEndDT.format(formatter);
                String randAppointmentID = String.valueOf((int) (Math.random() * 350));

                ps.setString(1, randAppointmentID);
                ps.setString(2, title);
                ps.setString(3, description);
                ps.setString(4, location);
                ps.setString(5, type);
                ps.setString(6, start);
                ps.setString(7, end);
                ps.setString(8, ZonedDateTime.now(ZoneOffset.UTC).format(formatter));
                ps.setString(9, currentUser);
                ps.setString(10, ZonedDateTime.now(ZoneOffset.UTC).format(formatter));
                ps.setString(11, currentUser);
                ps.setInt(12, customerID);
                ps.setInt(13, userID);
                //implement
                ps.setString(14, contactDAO.getContactByID(contactName));


                ps.execute();
                ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Appointment succesfully created.", clickOkay);
                alert.showAndWait();
                Parent parent = FXMLLoader.load(getClass().getResource("/view/appointments.fxml"));
                Scene scene = new Scene(parent);
                Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
                window.setTitle("Appointments");
                window.setScene(scene);
                window.show();



            }
            else{
                System.out.println("Input, error.");
                String e = "Make sure you enter valid input for all fields, try again.";
                ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert invalidInput = new Alert(Alert.AlertType.WARNING, e, clickOkay);
                invalidInput.showAndWait();
                return;
            }
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }

    /**
     * Method cancels new appointment creation and takes user back to appointmentView
     * @param event
     * @throws Exception
     */
    public void addAppointmentCancelButton(ActionEvent event) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/appointments.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Appointments");
        window.setScene(scene);
        window.show();
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
        System.out.println(customerID);
        for (Appointment appointment : appointments){
            System.out.println(appointment.getAppointmentID());
            LocalDateTime start = appointment.getStartDateTime().toLocalDateTime();
            LocalDateTime end = appointment.getEndDateTime().toLocalDateTime();
            System.out.println(start);
            System.out.println(end);

            if(start.isEqual(startDT) & end.isEqual(endDT)){
                System.out.println("Duplicate appointment, error.");
                String e = "An appointment already exists at this time for this customer, try again.";
                ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert invalidInput = new Alert(Alert.AlertType.WARNING, e, clickOkay);
                invalidInput.showAndWait();
                return true;
            }
            else if ((start.isBefore(startDT) & end.isAfter(endDT) || (start.isBefore(endDT) & start.isAfter(startDT)) || (end.isBefore(endDT) & end.isAfter(startDT)))){

                System.out.println("Customer overlap, error.");
                String e = "There is an overlap in customer appointments, try again.";
                ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
                Alert invalidInput = new Alert(Alert.AlertType.WARNING, e, clickOkay);
                invalidInput.showAndWait();
                System.out.println("True: COL");
                return true;
            }
        }
        System.out.println("False: COL");
        return false;

    }

    /**
     * Method checks the date to make sure it is between business hours.
     * @param startDT start date time
     * @param endDT end date tine
     * @param startDate start date of appt
     * @param endDate end date of appt
     */
    public Boolean checkDate(LocalDateTime startDT, LocalDateTime endDT, LocalDate startDate, LocalDate endDate ){
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

        //weekend appointments should be included.
        /*
        if(startDayInt < weekBegin || startDayInt > weekEnd || endDayInt < weekBegin || endDayInt > weekEnd){
            return true;
        }
         */

        //validate time within business hours
        if(zonedDTstart.isBefore(hoursStart) | zonedDTstart.isAfter(hoursEnd) | zonedDTend.isBefore(hoursStart)
                | zonedDTend.isAfter(hoursEnd) | zonedDTstart.isAfter(hoursEnd)){
            return true;
        }
        //validate start before end
        if(startDT.isAfter(endDT)){
            return true;
        }
        //validate start not equal to end
        if(startDT.isEqual(endDT)){
            return true;
        }
        if(startDayInt != endDayInt){
            return true;
        }

        return false;
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
