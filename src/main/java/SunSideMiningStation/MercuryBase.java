package SunSideMiningStation;

import SunSideMiningStation.Models.BaseStatusModel;

import java.util.ArrayList;
import java.util.List;

public class MercuryBase {
    private BaseStatusModel _status;
    private static MercuryBase _instance;
    private List<RobotSPD> _spds;

    private MercuryBase(BaseStatusModel status, List<RobotSPD> spds) {
        _status = status;
        _spds = spds;
    }

    public static synchronized MercuryBase getInstance(){
        if(_instance == null){
            // TODO: Initialize it with values from some permanent storage
            List<RobotSPD> spds = new ArrayList<RobotSPD>();
            spds.add(new RobotSPD());
            BaseStatusModel status = new BaseStatusModel(0, 10, 5, 5, 0);
            _instance = new MercuryBase(status, spds);
        }
        return _instance;
    }

    public RobotSPD getAvailableSPD() {
        for (RobotSPD spd: _spds) {
            if (!spd.isBusy()) {
                return spd;
            }
        }
        return null;
    }

    public BaseStatusModel getStatus() {
        return _status;
    }

    void addSelenium(int seleniumNumber) {
        _status.setSeleniumNumber(_status.getSeleniumNumber() + seleniumNumber);
    }

    public void provideEnergy(String location, int requiredEnergy){

    }

    public void createBattery(int batteryNumber){

    }

    public void repairBattery(int batteryNumber){

    }

    public void checkBaseStatus(){

    }

    private boolean CheckBattery(){
        return false;
    }

    private boolean checkWeather(){
        return false;
    }
}
