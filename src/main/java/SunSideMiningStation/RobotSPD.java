package SunSideMiningStation;

import java.util.Random;

public class RobotSPD {

    private enum Status {
        AT_BASE,
        NEAR_SELENIUM_POOL
    }

    private Status _status = Status.AT_BASE;
    private Random _random = new Random();

    boolean isBusy() {
        return _status != Status.AT_BASE;
    }

    public void gatherSelenium(MercuryBase base, int requiredSeleniumNumber){
        if (_random.nextDouble() < 0.1) {
            _status = Status.NEAR_SELENIUM_POOL;
        } else {
            base.addSelenium(requiredSeleniumNumber);
        }
    }

    public void Save(){
        _status = Status.AT_BASE;
    }
}
