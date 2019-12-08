package SunSideMiningStation;

import java.util.Random;

public class RobotSPD extends Robot {
    private Random _random = new Random();
    private boolean _glitched = false;

    boolean isGlitched() {
        return _glitched;
    }
    boolean isBusy() { return _location != Location.AT_BASE; }

    public void gatherSelenium(MercuryBase base, int requiredSeleniumNumber){
        if (_random.nextDouble() < 0.1) {
            _location = Location.NEAR_SELENIUM_POOL;
            _glitched = true;
        } else {
            base.addSelenium(requiredSeleniumNumber);
        }
    }

    public void saveHuman(Staff staff) {
        _glitched = false;

        carryHuman(staff);

        this.move(Location.AT_BASE);

        this.releaseHuman(staff);
    }
}
