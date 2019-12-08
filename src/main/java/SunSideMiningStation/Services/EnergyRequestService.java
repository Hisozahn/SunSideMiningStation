package SunSideMiningStation.Services;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import SunSideMiningStation.MercuryBase;
import SunSideMiningStation.Models.OrderInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Path("/energy")
@Produces({MediaType.APPLICATION_JSON})
public class EnergyRequestService {
    private static Map<String, String> passes = new HashMap<String, String>();
    private static Map<UUID, String> tokens = new HashMap<UUID, String>();

    static {
        passes.put("Earth", "forklift154");
        passes.put("Mars", "cookie34star67");
    }

    private static String  getLocation(String token) {
        try {
            return tokens.get(UUID.fromString(token));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Path("login")
    @POST
    public JsonResponse Login(@FormParam("location") String location,
                              @FormParam("password") String password){
        String pass = passes.get(location);
        if (!password.equals(pass))
            return new JsonResponse(JsonResponse.StatusCode.INTERNAL_SERVER_ERROR, "Invalid password/login pair");

        UUID id = UUID.randomUUID();
        tokens.put(id, location);

        return new JsonResponse(JsonResponse.StatusCode.OK, "" + id.toString());
    }
    @Path("request")
    @POST
    public JsonResponse MakeEnergyOrder(@FormParam("token") String token,
                                @FormParam("requiredEnergy") int requiredEnergy) {
        String location = getLocation(token);
        if (location == null)
            return new JsonResponse(JsonResponse.StatusCode.INTERNAL_SERVER_ERROR, "Invalid auth token");
        if (requiredEnergy <1 || requiredEnergy >10000)
            return new JsonResponse(JsonResponse.StatusCode.INTERNAL_SERVER_ERROR, "Invalid energy request");

        MercuryBase mBase = MercuryBase.getInstance();
        mBase.provideEnergy(location, requiredEnergy);
        return new JsonResponse(JsonResponse.StatusCode.OK);
    }
    @Path("check_requests")
    @POST
    public Object CheckEnergyOrders(@FormParam("token") String token){
        String location = getLocation(token);
        if (location == null)
            return new JsonResponse(JsonResponse.StatusCode.INTERNAL_SERVER_ERROR, "Invalid auth token");

        String str = "";
        MercuryBase mBase = MercuryBase.getInstance();
        List<OrderInfo> requested = mBase.checkRequests(location);

        return requested;
    }

    @Path("cancel_request")
    @POST
    public JsonResponse CancelEnergyOrder(@FormParam("token") String token,
                                          @FormParam("requestId") int requestId){
        String location = getLocation(token);
        if (location == null)
            return new JsonResponse(JsonResponse.StatusCode.INTERNAL_SERVER_ERROR, "Invalid auth token");

        MercuryBase mBase = MercuryBase.getInstance();
        if (mBase.cancelRequest(location, requestId))
            return new JsonResponse(JsonResponse.StatusCode.OK);

        return new JsonResponse(JsonResponse.StatusCode.INTERNAL_SERVER_ERROR, "Failed to cancel request");
    }
}
