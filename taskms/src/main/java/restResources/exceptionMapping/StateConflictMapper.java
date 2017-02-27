package restResources.exceptionMapping;

import service.exception.StateConflict;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class StateConflictMapper implements ExceptionMapper<StateConflict> {
    @Override
    public Response toResponse(StateConflict exception)
    {
        return Response.status(Response.Status.CONFLICT).entity(exception.getMessage()).build();
    }
}