package SunSideMiningStation.Models;

import java.util.Queue;
import java.util.LinkedList;

public class BaseStatusModel {
    private int _seleniumNumber;
    private int _energyNumber;
    private int _batteryNumber;
    private int _workingBatteryNumber;
    private int _requiredEnergyNumber;
    private int _spdsFree;
   
    private int batterySimulTime = 500;
    private int laserSimulTime = 2000;
    private int baseTickTime = 1000;
    private boolean laserIsBusy = false;

    class EnergyRequest {
        public int energy;
        public String location;
        public EnergyRequest(int energy, String location){
            this.energy = energy;
            this.location = location;
        }
    };

    private Queue<EnergyRequest> _queue;

    public BaseStatusModel(){}

    public BaseStatusModel(int seleniumNumber,
                           int energyNumber,
                           int batteryNumber,
                           int workingBatteryNumber,
                           int requiredEnergyNumber){
        _seleniumNumber = seleniumNumber;
        _energyNumber = energyNumber;
        _batteryNumber = batteryNumber;
        _workingBatteryNumber = workingBatteryNumber;
        _requiredEnergyNumber = requiredEnergyNumber;
        _queue = new LinkedList<EnergyRequest>();

        Thread thr = new Thread( new Runnable() 
        {
            public void run() {
                while(true) {
                    try {
                        batterySimulStub( );
                    } catch( InterruptedException e ) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        });
        thr.start( );

        Thread thr1 = new Thread( new Runnable() 
        {
            public void run() {
                while(true) {
                    try {
                        baseLoopTick( );
                    } catch( InterruptedException e ) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        });
        thr1.start( );
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

    public void addEnergyRequest(int energy, String location) {
        if(energy > 0) {
            _queue.add(new EnergyRequest(energy,location));
            _requiredEnergyNumber += energy;
        }
    }

    private void checkWeather() {
        // stub
    }

    private void adjustTrajectory(String location) {
        // stub
    }

    private void fireLaser(int energy, String location) {
        if(laserIsBusy) return;
        System.out.println("Firing " + String.valueOf(energy) + " at " + location);

        checkWeather();
        adjustTrajectory(location);
        _energyNumber -= energy;
        _requiredEnergyNumber -= energy;
        laserIsBusy = true;
        Thread thr = new Thread( new Runnable() 
        {
            public void run() {
                try {
                    Thread.sleep(laserSimulTime);
                } catch(InterruptedException e) {
                    System.out.println(e.getMessage( ));
                }
                laserIsBusy = false;
            }
        });
        thr.start();

    }

    private void executeRequest() {
        if(_queue.isEmpty()) return;
        EnergyRequest enreq = _queue.poll( );
        fireLaser(enreq.energy, enreq.location);
    }

    private void baseLoopTick( ) throws InterruptedException {
        if(_queue.isEmpty() == false && laserIsBusy == false) {
            EnergyRequest enreq = _queue.peek( );
            if( enreq.energy <= _energyNumber ) {
                executeRequest( );
            }
        }
        System.out.println("Avail energy: " + String.valueOf(_energyNumber) + 
                            " Req energy: " + String.valueOf(_requiredEnergyNumber) + 
                            " Q empty: " + String.valueOf(_queue.isEmpty( )) +
                            " Laser busy:" + String.valueOf(laserIsBusy));
        Thread.sleep(baseTickTime);
    }

    public void batterySimulStub( ) throws InterruptedException {
        _energyNumber += _batteryNumber;
        Thread.sleep(batterySimulTime);
    }

}

