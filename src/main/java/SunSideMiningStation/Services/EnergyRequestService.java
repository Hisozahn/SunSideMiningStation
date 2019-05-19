package SunSideMiningStation.Services;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import SunSideMiningStation.MercuryBase;

@Path("/energy")
@Produces({MediaType.APPLICATION_JSON})
public class EnergyRequestService {

    @POST
    public JsonResponse MakeEnergyOrder(@QueryParam("location") String location,
                                @QueryParam("requiredEnergy") int requiredEnergy){
        MercuryBase mBase = MercuryBase.getInstance();
        mBase.provideEnergy(location, requiredEnergy);
        return new JsonResponse(JsonResponse.StatusCode.OK);
    }
}
