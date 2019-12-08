package SunSideMiningStation;

import SunSideMiningStation.Models.BaseStatusModel;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MercuryBase {
    private BaseStatusModel _status;
    private static MercuryBase _instance;
    private List<RobotSPD> _spds;
    private List<OldRobot> _olds;
    private List<Staff> _staff;
    private static final int BATTERY_COST_IN_SELENIUM = 5;
    private ConcurrentLinkedQueue<SaveRobotOrder> _saveRobotQueue;
    private ConcurrentLinkedQueue<SeleniumOrder> _seleniumOrderQueue;
    private final int tickTime = 1000;
    public static final int maxGatherSelenium = 10;

    private MercuryBase(BaseStatusModel status, List<RobotSPD> spds, List<OldRobot> olds, List<Staff> staff) {
        _status = status;
        _spds = spds;
        _olds = olds;
        _staff = staff;
        _saveRobotQueue = new ConcurrentLinkedQueue<SaveRobotOrder>();
        _seleniumOrderQueue = new ConcurrentLinkedQueue<SeleniumOrder>();

        Thread thr = new Thread( new Runnable()
        {
            public void run() {
                while(true) {
                    try {
                        saveSimul( );
                        orderSimul();
                        Thread.sleep(tickTime);
                    } catch( Exception e ) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        });
        thr.start( );
    }

    public static synchronized MercuryBase getInstance(){
        if(_instance == null){
            // TODO: Initialize it with values from some permanent storage
            List<RobotSPD> spds = new ArrayList<RobotSPD>();
            spds.add(new RobotSPD());
            List<OldRobot> olds = new ArrayList<OldRobot>();
            olds.add(new OldRobot());
            List<Staff> staff = new ArrayList<Staff>();
            staff.add(new Staff());
            BaseStatusModel status = new BaseStatusModel(0, 10, 5, 5, 0);
            _instance = new MercuryBase(status, spds, olds, staff);
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

    public RobotSPD getSPDByIndex(int index) {
        try {
            return _spds.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public OldRobot getAvailableOldRobot() {
        if (_olds.isEmpty()) {
            return null;
        }
        return _olds.get(0);
    }

    public Staff getAvailableStaff() {
        if (_staff.isEmpty()) {
            return null;
        }
        return _staff.get(0);
    }

    public BaseStatusModel getStatus() {
        return _status;
    }

    void addSelenium(int seleniumNumber) {
        _status.setSeleniumNumber(_status.getSeleniumNumber() + seleniumNumber);
    }

    public void provideEnergy(String location, int requiredEnergy){
        System.out.println("Request " + String.valueOf(requiredEnergy) + " energy to " + location);
        System.out.println("Available energy: " + _status.getEnergyNumber());
        System.out.println("Required energy: " + _status.getRequiredEnergyNumber());
        _status.addEnergyRequest(requiredEnergy, location);
    }

    public void createBattery(){
        int seleniumNumber = _status.getSeleniumNumber();

        if (seleniumNumber < BATTERY_COST_IN_SELENIUM) {
            throw new IllegalStateException("Amount of selenium at the base is insufficient");
        }
        _status.setSeleniumNumber(seleniumNumber - BATTERY_COST_IN_SELENIUM);
        _status.setBatteryNumber(_status.getBatteryNumber() + 1);
        _status.setWorkingBatteryNumber(_status.getWorkingBatteryNumber() + 1);
    }

    public void repairBattery(int batteryNumber){
        if (batteryNumber >= _status.getBatteryNumber()) {
            throw new ArrayIndexOutOfBoundsException("Invalid battery number");
        }
        // Batteries now are always functional
    }

    public void checkBaseStatus(){

    }

    private boolean CheckBattery(){
        return false;
    }

    private boolean checkWeather(){
        return false;
    }

    public boolean addSaveRobotOrder(SaveRobotOrder order) {
        if (_saveRobotQueue.contains(order))
            return false;
        _saveRobotQueue.add(order);

        return true;
    }
    public boolean addGatherSeleniumOrder(SeleniumOrder order) {
        if (_seleniumOrderQueue.contains(order))
            return false;
        _seleniumOrderQueue.add(order);

        return true;
    }


    public void saveSimul() throws InterruptedException {
        SaveRobotOrder order;

        try {
            order = _saveRobotQueue.remove();
        } catch (NoSuchElementException e) {
            return;
        }

        order.getOld().moveToSPD(order.getSpd(), order.getStaff());
        order.getStaff().saveSPD(order.getSpd(), order.getOld());

        System.out.println("SPD saved");
    }

    public void orderSimul() throws InterruptedException {
        SeleniumOrder order;

        try {
            order = _seleniumOrderQueue.remove();
        } catch (NoSuchElementException e) {
            return;
        }

        if (!order.getSpd().gatherSelenium(order.getSeleniumNum())) {
            System.out.println("SPD did not finish gathering");
            return;
        }

        this.addSelenium(order.getSeleniumNum());
        System.out.println("SPD gathered " + order.getSeleniumNum() + " selenium");
    }
}
