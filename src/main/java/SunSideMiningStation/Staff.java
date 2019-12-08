package SunSideMiningStation;

public class Staff {
    private Location _location = Location.AT_BASE;
    private Robot _carriedBy = null;

    public Location getLocation() {
        return _location;
    }

    public Robot getCarriedBy() {
        return _carriedBy;
    }

    public void setCarriedBy(Robot _carriedBy) {
        this._carriedBy = _carriedBy;
    }

    public void setLocation(Location location) {
        _location = location;
    }

    public void saveSPD(RobotSPD spd, OldRobot old) throws IllegalStateException, InterruptedException {
        if (!spd.isGlitched())
            throw new IllegalStateException("SPD does not need to be saved");

        if (!old.releaseHuman(this))
            throw new IllegalStateException("Old robot cannot release me!");

        spd.saveHuman(this);
        old.move(Location.AT_BASE);
    }
}
