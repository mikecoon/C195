package model;

/**User class*/
public class User {

    public int userID;
    public String userName;
    public String userPassword;

    /**Constructor for User class*/
    public User(int userID, String userName, String userPassword) {
        this.userID = userID;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    /**
     * @return userID
     */
    public int getUserID() {

        return userID;
    }

    /**
     * @return userName
     */
    public String getUserName() {

        return userName;
    }

    /**
     * @return userPassword
     */
    public String getUserPassword() {

        return userPassword;
    }
}