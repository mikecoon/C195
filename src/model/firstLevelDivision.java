package model;

public class firstLevelDivision {
    private String divisionName;
    private int divID;
    public int countryID;

    public firstLevelDivision(String divisionName, int divID, int countryID){
        this.divisionName = divisionName;
        this.divID = divID;
        this.countryID = countryID;
    }

    public int getCountryID() {
        return countryID;
    }

    public int getDivID() {
        return divID;
    }

    public String getDivisionName() {
        return divisionName;
    }
}
