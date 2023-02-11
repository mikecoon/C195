package model;

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
    private String createBy;
    private Timestamp lastUpdateDateTime;
    private String lastUpdateBy;

    public Appointment(Integer appointmentID, String title, String type, String description, String location,
                       Timestamp startDateTime, Timestamp endDateTime, Integer contactID, String contactName,
                       Integer customerID, Integer userID, Timestamp createDate, String createBy,
                       Timestamp lastUpdateDateTime, String lastUpdateBy){
        appointmentID = appointmentID;
        title = title;
        type = type;
        description = description;
        location = location;
        startDateTime = startDateTime;
        endDateTime = endDateTime;
        contactID = contactID;
        contactName = contactName;
        customerID = customerID;
        userID = userID;
        createDate = createDate;
        createBy = createBy;
        lastUpdateDateTime = lastUpdateDateTime;
        lastUpdateBy = lastUpdateBy;

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
    public String getCreateBy() {
        return createBy;
    }
    public Timestamp getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }
    public String getLastUpdateBy() {
        return lastUpdateBy;
    }
}
