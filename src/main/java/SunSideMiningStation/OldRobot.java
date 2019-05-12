package SunSideMiningStation;

public class OldRobot {
    public void rescueSPD(RobotSPD spd){
        if (!spd.isBusy())
            return;
        spd.Save();
    }
}
