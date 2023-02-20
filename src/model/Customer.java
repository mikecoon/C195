package model;

public class Customer {
    private Integer id;
    private String name;
    private String address;
    private String phoneNumber;
    private String postalCode;

    public Customer(Integer id, String name, String address, String phoneNumber, String postalCode){
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.postalCode = postalCode;

    }

    public Integer getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

}
