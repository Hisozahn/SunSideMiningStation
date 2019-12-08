package SunSideMiningStation;

public class SeleniumOrder {
    private RobotSPD _spd;
    private int _seleniumNum;

    public SeleniumOrder(RobotSPD _spd, int seleniumNum) {
        this._spd = _spd;
        this._seleniumNum = seleniumNum;
    }

    public RobotSPD getSpd() {
        return _spd;
    }

    public int getSeleniumNum() {
        return _seleniumNum;
    }

    @Override
    public boolean equals(Object obj) {
        SeleniumOrder order = (SeleniumOrder)obj;

        return order._spd == _spd;
    }
}
