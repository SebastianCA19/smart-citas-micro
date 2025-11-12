package model;

public class Procedure {
    private int id;
    private String procedimiento;

    public Procedure(int id, String procedimiento) {
        this.id = id;
        this.procedimiento = procedimiento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
    }
}