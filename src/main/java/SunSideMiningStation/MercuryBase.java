package SunSideMiningStation;

import SunSideMiningStation.Models.BaseStatusModel;

public class MercuryBase {
    private BaseStatusModel _status;
    private static MercuryBase _instance;

    private MercuryBase(BaseStatusModel status) {
        _status = status;
    }

    public static synchronized MercuryBase getInstance(){
        if(_instance == null){
            // TODO: Initialize it with values from some permanent storage
            BaseStatusModel status = new BaseStatusModel(0, 10, 5, 5, 0, 1);
            _instance = new MercuryBase(status);
        }
        return _instance;
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
