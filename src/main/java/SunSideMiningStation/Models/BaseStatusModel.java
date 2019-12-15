package SunSideMiningStation.Models;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

public class BaseStatusModel {
    private int _seleniumNumber;
    private int _energyNumber;
    private int _batteryNumber;
    private int _workingBatteryNumber;
    private int _requiredEnergyNumber;
    private boolean _energyInsufficient;
    private int _spdsFree;
    private String _energyRequestCSV = "requests.csv";
    private final Object csvMonitor = new Object();
    private int _lastEnergyRequestId;
   
    private int batterySimulTime = 500;
    private int laserSimulTime = 2000;
    private int baseTickTime = 1000;
    private boolean laserIsBusy = false;

    class EnergyRequest {
        public static final String STATUS_DONE = "done";
        public static final String STATUS_IN_PROGRESS = "in progress";
        public int id;
        public int energy;
        public String location;
        public String status;
        public EnergyRequest(int id, int energy, String location, String status){
            this.id = id;
            this.energy = energy;
            this.location = location;
            this.status = status;
        }
    };

    private Queue<EnergyRequest> readCSV() {

        File file = new File(_energyRequestCSV);

        try {
            file.createNewFile();

            BufferedReader csvReader = new BufferedReader(new FileReader(file));
            String row;
            LinkedList<EnergyRequest> queue = new LinkedList<EnergyRequest>();
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                int energy;
                int id;

                if (data.length != 4)
                    throw new IOException("Invalid csv file (format might have changed, delete requests.csv)");

                id = Integer.parseInt(data[0]);
                energy = Integer.parseInt(data[2]);

                queue.add(new EnergyRequest(id, energy, data[1], data[3]));
            }
            csvReader.close();

            return queue;

        } catch (IOException e) {
            System.err.println("Fatal exception: csv file io error: " + e.getMessage());
            System.exit(1);

            return null;
        }
    }

    private void writeCSV(Queue<EnergyRequest> queue) {
        try {
            FileWriter csvWriter = new FileWriter(_energyRequestCSV);

            for (EnergyRequest e: queue) {
                csvWriter.append(e.id + "," + e.location + "," + e.energy + "," + e.status + "\n");
            }

            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            System.err.println("Fatal exception: csv file io error: " + e.getMessage());
            System.exit(1);
        }
    }

    public BaseStatusModel(){}

    public BaseStatusModel(int seleniumNumber,
                           int energyNumber,
                           int batteryNumber,
                           int workingBatteryNumber) {
        synchronized (csvMonitor) {
            Queue<EnergyRequest> queue = readCSV();
            _requiredEnergyNumber = 0;
            _lastEnergyRequestId = 0;
            for (EnergyRequest e: queue) {
                if (!e.status.equals(EnergyRequest.STATUS_DONE))
                    _requiredEnergyNumber += e.energy;
                _lastEnergyRequestId = e.id;
            }
        }
        _seleniumNumber = seleniumNumber;
        _energyNumber = energyNumber;
        _batteryNumber = batteryNumber;
        _workingBatteryNumber = workingBatteryNumber;
        _energyInsufficient = _requiredEnergyNumber > energyNumber;

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

    public List<OrderInfo> getEnergyRequests(String location) {
        List<OrderInfo> requests = new LinkedList<OrderInfo>();

        synchronized (csvMonitor) {
            Queue<EnergyRequest> queue = readCSV();
            for (EnergyRequest e: queue) {
                if (e.location.equals(location))
                    requests.add(new OrderInfo(e.id, e.energy, e.status));
            }
        }

        return requests;
    }

    public boolean cancelRequest(String location, int id) {
        boolean canceled = false;
        synchronized (csvMonitor) {
            Queue<EnergyRequest> queue = readCSV();
            Iterator<EnergyRequest> iter = queue.iterator();

            while (iter.hasNext()){
                EnergyRequest e;
                e = iter.next();
                if(e.location.equals(location) && e.id == id && !e.status.equals(EnergyRequest.STATUS_DONE)){
                    iter.remove();
                    canceled = true;
                    _requiredEnergyNumber -= e.energy;
                    break;
                }
            }
            writeCSV(queue);
        }

        return canceled;
    }

    public boolean isEnergyInsufficient() { return _energyInsufficient; }

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
        _energyInsufficient = _requiredEnergyNumber > energyNumber;
    }

    public void setBatteryNumber(int batteryNumber){
        _batteryNumber = batteryNumber;
    }

    public void setWorkingBatteryNumber(int workingBatteryNumber){
        _workingBatteryNumber = workingBatteryNumber;
    }

    public void setRequiredEnergyNumber(int requiredEnergyNumber){
        _requiredEnergyNumber = requiredEnergyNumber;
        _energyInsufficient = requiredEnergyNumber > _energyNumber;
    }

    public void setSpdsFree(int spdsFree) {
        _spdsFree = spdsFree;
    }

    public int addEnergyRequest(int energy, String location) {
        if(energy > 0) {
            synchronized (csvMonitor) {
                Queue<EnergyRequest> queue = readCSV();
                queue.add(new EnergyRequest(++_lastEnergyRequestId, energy,location, EnergyRequest.STATUS_IN_PROGRESS));
                writeCSV(queue);
            }
            _requiredEnergyNumber += energy;

            return _lastEnergyRequestId;
        }

        return -1;
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
        setEnergyNumber(_energyNumber - energy);
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

    private void executeRequestUnsafe(EnergyRequest enreq) {
        fireLaser(enreq.energy, enreq.location);
        enreq.status = EnergyRequest.STATUS_DONE;
    }

    private void baseLoopTick( ) throws InterruptedException {
        synchronized (csvMonitor) {
            Queue<EnergyRequest> queue = readCSV();

            if (queue.isEmpty() == false && laserIsBusy == false) {
                EnergyRequest enreq = null;
                for (EnergyRequest e: queue) {
                    if (!e.status.equals(EnergyRequest.STATUS_DONE)) {
                        enreq = e;
                        break;
                    }
                }
                if (enreq != null && enreq.energy <= _energyNumber) {
                    executeRequestUnsafe(enreq);
                }
            }

            writeCSV(queue);
        }
        System.out.println("Avail energy: " + String.valueOf(_energyNumber) + 
                            " Req energy: " + String.valueOf(_requiredEnergyNumber) +
                            " Laser busy:" + String.valueOf(laserIsBusy));
        Thread.sleep(baseTickTime);
    }

    public void batterySimulStub( ) throws InterruptedException {
        setEnergyNumber(_energyNumber + _batteryNumber);
        Thread.sleep(batterySimulTime);
    }

}

