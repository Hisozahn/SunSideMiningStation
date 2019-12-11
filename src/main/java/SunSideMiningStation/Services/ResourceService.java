package SunSideMiningStation.Services;

import SunSideMiningStation.MercuryBase;
import SunSideMiningStation.Models.OrderInfo;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Path("/resource")
@Produces({MediaType.TEXT_HTML, "text/css", "image/jpeg"})
public class ResourceService {

    @Path("/{subResources:[a-zA-Z0-9]+\\.(html|css|jpg)}")
    @GET
    public InputStream GetResource(@PathParam("subResources") String subResources) {
        InputStream s = getClass().getResourceAsStream("/" + subResources);
        if (s == null) {
            return new InputStream() {
                @Override
                public int read() throws IOException {
                    return -1;
                }
            };
        }
        return s;
    }
}
