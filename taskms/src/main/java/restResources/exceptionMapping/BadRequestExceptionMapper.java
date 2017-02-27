package restResources.exceptionMapping;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {

    public Response toResponse(BadRequestException ex) {
        return Response.status(Response.Status.BAD_REQUEST).
                entity(ex.getMessage()).
                type("text/plain").
                build();
    }
}
