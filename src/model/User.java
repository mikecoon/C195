package model;

public class User {

    public int userID;
    public String userName;
    public String userPassword;

    public User(int userID, String userName, String userPassword) {
        this.userID = userID;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    //return UserID
    public int getUserID() {

        return userID;
    }

    //return userName
    public String getUserName() {

        return userName;
    }

    //return user password
    public String getUserPassword() {

        return userPassword;
    }
}