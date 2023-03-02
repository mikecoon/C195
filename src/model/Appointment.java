package model;

import DAO.contactDAO;

import java.sql.SQLException;
import java.sql.Timestamp;

public class Appointment {
    private Integer appointmentID;
    private String title;
    private String type;
    private String description;
    private String location;
    private Timestamp startDateTime;
    private Timestamp endDateTime;
    private Integer contactID;
    private String contactName;
    private Integer customerID;
    private Integer userID;
    private Timestamp createDate;
    private Timestamp lastUpdateDateTime;
    private String lastUpdateBy;

    public Appointment(Integer appointmentID_, String title_, String type_, String description_, String location_,
                       Timestamp startDateTime_, Timestamp endDateTime_, Integer contactID_,
                       Integer customerID_, Integer userID_, Timestamp createDate_,
                       Timestamp lastUpdateDateTime_, String lastUpdateBy_){
        appointmentID = appointmentID_;
        title = title_;
        type = type_;
        description = description_;
        location = location_;
        startDateTime = startDateTime_;
        endDateTime = endDateTime_;
        contactID = contactID_;
        customerID = customerID_;
        userID = userID_;
        createDate = createDate_;
        lastUpdateDateTime = lastUpdateDateTime_;
        lastUpdateBy = lastUpdateBy_;
        try{
            contactName = contactDAO.givenIDgetName(contactID);
        }catch (SQLException e){
            System.out.println(e);
        }

    }
    //Appointment class getters

    public Integer getAppointmentID(){
        return appointmentID;
    }
    public String getTitle(){
        return title;
    }
    public String getType(){
        return type;
    }
    public String getDescription() {
        return description;
    }
    public String getLocation() {
        return location;
    }
    public Timestamp getStartDateTime() {
        return startDateTime;
    }
    public Timestamp getEndDateTime() {
        return endDateTime;
    }
    public Integer getContactID() {
        return contactID;
    }
    public String getContactName() {
        return contactName;
    }
    public Integer getCustomerID() {
        return customerID;
    }
    public Integer getUserID() {
        return userID;
    }
    public Timestamp getCreateDate() {
        return createDate;
    }
    public Timestamp getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }
    public String getLastUpdateBy() {
        return lastUpdateBy;
    }
}
