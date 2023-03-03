package model;

/** Class for first level divisions*/
public class firstLevelDivision {
    private String divisionName;
    private int divID;
    public int countryID;

    /**First level division constructor*/
    public firstLevelDivision(String divisionName, int divID, int countryID){
        this.divisionName = divisionName;
        this.divID = divID;
        this.countryID = countryID;
    }

    /**
     * @return  countryID
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * @return divID
     */
    public int getDivID() {
        return divID;
    }

    /**
     * @return divisionName
     */
    public String getDivisionName() {
        return divisionName;
    }
}
