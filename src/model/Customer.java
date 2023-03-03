package model;

/**Class for Customers*/
public class Customer {
    private Integer id;
    private String name;
    private String address;
    private String phoneNumber;
    private String postalCode;
    private String division;
    private String divisionID;
    private String country;

    /**Customer constructor*/
    public Customer(Integer id, String name, String address, String phoneNumber, String postalCode, String division, String divisionID, String country){
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.postalCode = postalCode;
        this.division = division;
        this.divisionID = divisionID;
        this.country = country;

    }

    /**
     * @return id
     */
    public Integer getId(){
        return id;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @return postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @return divisionID
     */
    public String getDivisionID() {
        return divisionID;
    }

    /**
     * @return division
     */
    public String getDivision() {
        return division;
    }

    /**
     * @return country
     */
    public String getCountry() {
        return country;
    }

    /**
     * sets the division
     */
    public void setDivision(String division){
        this.division = division;
    }

    /**
     * sets the country
     */
    public void setCountry(String country) {
        this.country = country;
    }
}
