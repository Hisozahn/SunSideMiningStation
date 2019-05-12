package SunSideMiningStation.Services;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import SunSideMiningStation.MercuryBase;

@Path("/miningStation")
@Produces({MediaType.APPLICATION_JSON})
public class MiningStationService {

    @POST
    @Path("{location}")
    public void MakeEnergyOrder(@PathParam("location") String location,
                                @QueryParam("requiredEnergy") int requiredEnergy){
        MercuryBase mBase = MercuryBase.getInstance();
        mBase.provideEnergy(location, requiredEnergy);
    }
}
