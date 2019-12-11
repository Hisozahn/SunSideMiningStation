package SunSideMiningStation.Services;

import SunSideMiningStation.*;
import SunSideMiningStation.Models.BaseStatusModel;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/rest/mercuryBase")
@Produces({MediaType.APPLICATION_JSON})
public class MercuryBaseService {

    @Path("seleniumOrder")
    @POST
    public JsonResponse MakeSeleniumOrder(@QueryParam("requiredSeleniumNumber") int requiredSeleniumNumber){
        MercuryBase mBase = MercuryBase.getInstance();
        RobotSPD spd = mBase.getAvailableSPD();
        if (spd == null) {
            return new JsonResponse(JsonResponse.StatusCode.INTERNAL_SERVER_ERROR, "No SPDs available");
        }
        if (requiredSeleniumNumber < 0 || requiredSeleniumNumber > MercuryBase.maxGatherSelenium) {
            return new JsonResponse(JsonResponse.StatusCode.INTERNAL_SERVER_ERROR, "Invalid selenium amount requested");
        }
        if (!mBase.addGatherSeleniumOrder(new SeleniumOrder(spd, requiredSeleniumNumber)))
            return new JsonResponse(JsonResponse.StatusCode.INTERNAL_SERVER_ERROR, "Operation is already in progress");

        return new JsonResponse(JsonResponse.StatusCode.OK);
    }

    @GET
    public BaseStatusModel GetBaseStatus(){
        MercuryBase mBase = MercuryBase.getInstance();
        return mBase.getStatus();
    }

    @Path("createBatteryOrder")
    @POST
    public JsonResponse MakeCreateBatteryOrder()
    {
        MercuryBase mBase = MercuryBase.getInstance();
        try {
            mBase.createBattery();
        } catch (IllegalStateException e) {
            return new JsonResponse(JsonResponse.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return new JsonResponse(JsonResponse.StatusCode.OK);
    }

    @Path("repairBatteryOrder")
    @POST
    public JsonResponse MakeRepairBatteryOrder(@QueryParam("BatteryNumber") int batteryNumber)
    {
        MercuryBase mBase = MercuryBase.getInstance();
        try {
            mBase.repairBattery(batteryNumber);
        } catch (IndexOutOfBoundsException e) {
            return new JsonResponse(JsonResponse.StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return new JsonResponse(JsonResponse.StatusCode.OK);
    }

    @Path("saveRobotOrder")
    @POST
    public JsonResponse MakeSaveRobotOrder(@QueryParam("SPDIndex") int spdIndex)
    {
        MercuryBase mBase = MercuryBase.getInstance();
        RobotSPD spd = mBase.getSPDByIndex(spdIndex);
        Staff staff = mBase.getAvailableStaff();
        if (spd == null) {
            return new JsonResponse(JsonResponse.StatusCode.INTERNAL_SERVER_ERROR, "Invalid SPD index");
        }
        OldRobot old = mBase.getAvailableOldRobot();
        if (old == null) {
            return new JsonResponse(JsonResponse.StatusCode.INTERNAL_SERVER_ERROR, "No Old Robots available");
        }
        if (staff == null) {
            return new JsonResponse(JsonResponse.StatusCode.INTERNAL_SERVER_ERROR, "No staff available");
        }

        if (!mBase.addSaveRobotOrder(new SaveRobotOrder(spd, staff, old)))
            return new JsonResponse(JsonResponse.StatusCode.INTERNAL_SERVER_ERROR, "Operation is already in progress");

        return new JsonResponse(JsonResponse.StatusCode.OK);
    }

}
