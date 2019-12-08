package SunSideMiningStation;

import java.util.Random;

public class RobotSPD extends Robot {
    private Random _random = new Random();
    private boolean _glitched = false;

    boolean isGlitched() {
        return _glitched;
    }
    boolean isBusy() { return _location != Location.AT_BASE; }

    public boolean gatherSelenium(int requiredSeleniumNumber) throws InterruptedException, IllegalArgumentException {
        if (requiredSeleniumNumber > MercuryBase.maxGatherSelenium || requiredSeleniumNumber < 0)
            throw new IllegalArgumentException("Can't gather this amount of selenium");

        this.move(Location.NEAR_SELENIUM_POOL);

        if (_random.nextDouble() < 0.1) {
            _glitched = true;

            return false;
        }

        Thread.sleep(100 * requiredSeleniumNumber);

        this.move(Location.AT_BASE);

        return true;
    }

    public void saveHuman(Staff staff) throws InterruptedException {
        _glitched = false;

        carryHuman(staff);

        this.move(Location.AT_BASE);

        this.releaseHuman(staff);
    }
}
