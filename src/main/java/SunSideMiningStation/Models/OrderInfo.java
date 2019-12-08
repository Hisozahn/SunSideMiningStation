package SunSideMiningStation.Models;

public class OrderInfo {
    private int id;
    private int energy;

    public OrderInfo(int id, int energy) {
        this.id = id;
        this.energy = energy;
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
}
