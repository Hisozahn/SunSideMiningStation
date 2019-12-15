package SunSideMiningStation.Models;

public class OrderInfo {
    private int id;
    private int energy;
    private String status;

    public OrderInfo(int id, int energy, String status) {
        this.id = id;
        this.energy = energy;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
