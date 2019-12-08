package SunSideMiningStation;

public class OldRobot extends Robot {

    public void moveToSPD(RobotSPD spd, Staff staff) throws IllegalStateException, InterruptedException {
        if (!spd.isGlitched())
            return;

        if (!carryHuman(staff))
            throw new IllegalStateException("Can't carry human to pool to save SPD");

        this.move(Location.NEAR_SELENIUM_POOL);
    }
}
