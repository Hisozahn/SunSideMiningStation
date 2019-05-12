package SunSideMiningStation.Services;

import SunSideMiningStation.MercuryBase;
import SunSideMiningStation.Models.BaseStatusModel;
import SunSideMiningStation.RobotSPD;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/mercuryBase")
@Produces({MediaType.APPLICATION_JSON})
public class MercuryBaseService {

    @Path("seleniumOrder")
    @POST
    public void MakeSeleniumOrder(@QueryParam("requiredSeleniumNumber") int requiredSeleniumNumber){
        RobotSPD spd = new RobotSPD();
        spd.gatherSelenium(requiredSeleniumNumber);
    }

    @GET
    public BaseStatusModel GetBaseStatus(){
        return new BaseStatusModel();
    }

    @Path("createBatteryOrder")
    @POST
    public void MakeCreateBatteryOrder(@QueryParam("BatteryNumber") int batteryNumber)
    {
        MercuryBase mBase = new MercuryBase();
        mBase.createBattery(batteryNumber);
    }

    @Path("repairBatteryOrder")
    @POST
    public void MakeRepairBatteryOrder(@QueryParam("BatteryNumber") int batteryNumber)
    {
        MercuryBase mBase = new MercuryBase();
        mBase.repairBattery(batteryNumber);
    }

    @Path("saveRobotOrder")
    @POST
    public void MakeSaveRobotOrder()
    {

    }

}