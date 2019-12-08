package SunSideMiningStation.Services;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import SunSideMiningStation.MercuryBase;
import SunSideMiningStation.Models.OrderInfo;

import java.util.List;

@Path("/energy")
@Produces({MediaType.APPLICATION_JSON})
public class EnergyRequestService {
    @Path("request")
    @POST
    public JsonResponse MakeEnergyOrder(@FormParam("location") String location,
                                @FormParam("requiredEnergy") int requiredEnergy){
        MercuryBase mBase = MercuryBase.getInstance();
        mBase.provideEnergy(location, requiredEnergy);
        return new JsonResponse(JsonResponse.StatusCode.OK);
    }
    @Path("check_requests")
    @POST
    public List<OrderInfo> CheckEnergyOrders(@FormParam("location") String location){
        String str = "";
        MercuryBase mBase = MercuryBase.getInstance();
        List<OrderInfo> requested = mBase.checkRequests(location);

        return requested;
    }

    @Path("cancel_request")
    @POST
    public JsonResponse CancelEnergyOrder(@FormParam("location") String location,
                                          @FormParam("requestId") int requestId){
        MercuryBase mBase = MercuryBase.getInstance();
        if (mBase.cancelRequest(location, requestId))
            return new JsonResponse(JsonResponse.StatusCode.OK);

        return new JsonResponse(JsonResponse.StatusCode.INTERNAL_SERVER_ERROR, "Failed to cancel request");
    }
}
