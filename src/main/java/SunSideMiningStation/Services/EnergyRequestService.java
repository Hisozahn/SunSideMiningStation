package SunSideMiningStation.Services;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import SunSideMiningStation.MercuryBase;

@Path("/energy")
@Produces({MediaType.APPLICATION_JSON})
public class EnergyRequestService {

    @POST
    public JsonResponse MakeEnergyOrder(@FormParam("location") String location,
                                @FormParam("requiredEnergy") int requiredEnergy){
        System.out.println("Loc: " + location + "; ener " + requiredEnergy);
        MercuryBase mBase = MercuryBase.getInstance();
        mBase.provideEnergy(location, requiredEnergy);
        return new JsonResponse(JsonResponse.StatusCode.OK);
    }
}
