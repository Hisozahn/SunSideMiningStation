package SunSideMiningStation.Services;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.net.URI;

@Provider
public class BadURIExceptionMapper implements ExceptionMapper<NotFoundException> {

    public Response toResponse(NotFoundException exception){
        return Response.temporaryRedirect(URI.create("/resource/Customer.html")).build();
    }
}