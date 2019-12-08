package SunSideMiningStation;

public class SaveRobotOrder {
    private RobotSPD _spd;
    private Staff _staff;
    private OldRobot _old;

    public SaveRobotOrder(RobotSPD _spd, Staff _staff, OldRobot _old) {
        this._spd = _spd;
        this._staff = _staff;
        this._old = _old;
    }

    public RobotSPD getSpd() {
        return _spd;
    }

    public Staff getStaff() {
        return _staff;
    }

    public OldRobot getOld() {
        return _old;
    }

    @Override
    public boolean equals(Object obj) {
        SaveRobotOrder order = (SaveRobotOrder)obj;

        return order._spd == _spd || order._old == _old || order._staff == _staff;
    }
}
