package model;

import DAO.contactDAO;

import java.sql.SQLException;
import java.sql.Timestamp;

/**Class for appoinment*/
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

    /**Appointment constructor*/
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
    /**
     * @return appointmentID
     */
    public Integer getAppointmentID(){
        return appointmentID;
    }
    /**
     * @return title
     */
    public String getTitle(){
        return title;
    }
    /**
     * @return type
     */
    public String getType(){
        return type;
    }
    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @return location
     */
    public String getLocation() {
        return location;
    }
    /**
     * @return startDateTime
     */
    public Timestamp getStartDateTime() {
        return startDateTime;
    }
    /**
     * @return endDateTime
     */
    public Timestamp getEndDateTime() {
        return endDateTime;
    }
    /**
     * @return contactID
     */
    public Integer getContactID() {
        return contactID;
    }
    /**
     * @return contactName
     */
    public String getContactName() {
        return contactName;
    }
    /**
     * @return customerID
     */
    public Integer getCustomerID() {
        return customerID;
    }
    /**
     * @return userID
     */
    public Integer getUserID() {
        return userID;
    }
    /**
     * @return createDate
     */
    public Timestamp getCreateDate() {
        return createDate;
    }
    /**
     * @return lastUpdateTime
     */
    public Timestamp getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }
    /**
     * @return lastUpdateBy
     */
    public String getLastUpdateBy() {
        return lastUpdateBy;
    }
}
