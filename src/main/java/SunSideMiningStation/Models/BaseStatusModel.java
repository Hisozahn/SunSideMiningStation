package SunSideMiningStation.Models;

public class BaseStatusModel {
    private int _seleniumNumber;
    private int _energyNumber;
    private int _batteryNumber;
    private int _workingBatteryNumber;
    private int _requiredEnergyNumber;
    private int _spdsFree;

    public BaseStatusModel(){}

    public BaseStatusModel(int seleniumNumber,
                           int energyNumber,
                           int batteryNumber,
                           int workingBatteryNumber,
                           int requiredEnergyNumber,
                           int spdsFree){
        _seleniumNumber = seleniumNumber;
        _energyNumber = energyNumber;
        _batteryNumber = batteryNumber;
        _workingBatteryNumber = workingBatteryNumber;
        _requiredEnergyNumber = requiredEnergyNumber;
        _spdsFree = spdsFree;
    }

    public int getSeleniumNumber(){
        return _seleniumNumber;
    }

    public int getEnergyNumber(){
        return _energyNumber;
    }

    public int getBatteryNumber(){
        return _batteryNumber;
    }

    public int getWorkingBatteryNumber(){
        return _workingBatteryNumber;
    }

    public int getRequiredEnergyNumber(){
        return _requiredEnergyNumber;
    }

    public int getSpdsFree() {
        return _spdsFree;
    }

    public void setSeleniumNumber(int seleniumNumber){
        _seleniumNumber = seleniumNumber;
    }

    public void setEnergyNumber(int energyNumber){
        _energyNumber = energyNumber;
    }

    public void setBatteryNumber(int batteryNumber){
        _batteryNumber = batteryNumber;
    }

    public void setWorkingBatteryNumber(int workingBatteryNumber){
        _workingBatteryNumber = workingBatteryNumber;
    }

    public void setRequiredEnergyNumber(int requiredEnergyNumber){
        _requiredEnergyNumber = requiredEnergyNumber;
    }

    public void setSpdsFree(int spdsFree) {
        _spdsFree = spdsFree;
    }

}

