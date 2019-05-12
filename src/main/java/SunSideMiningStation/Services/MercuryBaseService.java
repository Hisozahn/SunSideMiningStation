package SunSideMiningStation.Services;

import SunSideMiningStation.MercuryBase;
import SunSideMiningStation.Models.BaseStatusModel;
import SunSideMiningStation.RobotSPD;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/mercuryBase")
@Produces({MediaType.APPLICATION_JSON})
public class MercuryBaseService {

    @POST
    public void MakeSeleniumOrder(@QueryParam("requiredSeleniumNumber") int requiredSeleniumNumber){
        RobotSPD spd = new RobotSPD();
        spd.gatherSelenium(requiredSeleniumNumber);
    }

    @GET
    public BaseStatusModel GetBaseStatus(){
        return new BaseStatusModel();
    }

    @POST
    public void MakeCreateBatteryOrder(@QueryParam("BatteryNumber") int batteryNumber)
    {
        MercuryBase mBase = new MercuryBase();
        mBase.createBattery(batteryNumber);
    }

    @POST
    public void MakeRepairBatteryOrder(@QueryParam("BatteryNumber") int batteryNumber)
    {
        MercuryBase mBase = new MercuryBase();
        mBase.repairBattery(batteryNumber);
    }

    @POST
    public void MakeSaveRobotOrder()
    {

    }

}
