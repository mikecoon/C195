package model;

public class Report {

    private String name;
    private int total;

    public Report(String name, int total){
        this.name = name;
        this.total = total;

    }

    public String getName() {
        return name;
    }

    public int getTotal() {
        return total;
    }
}
