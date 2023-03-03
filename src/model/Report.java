package model;

/**Class for reports*/
public class Report {

    private String name;
    private int total;

    /**Report constructor*/
    public Report(String name, int total){
        this.name = name;
        this.total = total;

    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @return total
     */
    public int getTotal() {
        return total;
    }
}
