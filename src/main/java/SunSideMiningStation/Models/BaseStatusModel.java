package SunSideMiningStation.Models;

import java.io.*;
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

                if (data.length != 2)
                    throw new IOException("Invalid csv file");

                energy = Integer.parseInt(data[1]);

                queue.add(new EnergyRequest(energy, data[0]));
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
                csvWriter.append(e.location + "," + e.energy + "\n");
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
            for (EnergyRequest e: queue) {
                _requiredEnergyNumber += e.energy;
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

    public void addEnergyRequest(int energy, String location) {
        if(energy > 0) {
            synchronized (csvMonitor) {
                Queue<EnergyRequest> queue = readCSV();
                queue.add(new EnergyRequest(energy,location));
                writeCSV(queue);
            }
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

    private void executeRequestUnsafe(Queue<EnergyRequest> queue) {
        if(queue.isEmpty()) return;
        EnergyRequest enreq = queue.poll( );
        fireLaser(enreq.energy, enreq.location);
    }

    private void baseLoopTick( ) throws InterruptedException {
        synchronized (csvMonitor) {
            Queue<EnergyRequest> queue = readCSV();

            if (queue.isEmpty() == false && laserIsBusy == false) {
                EnergyRequest enreq = queue.peek();
                if (enreq.energy <= _energyNumber) {
                    executeRequestUnsafe(queue);
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

