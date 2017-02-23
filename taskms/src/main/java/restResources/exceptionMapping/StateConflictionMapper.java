package restResources.exceptionMapping;

import service.exception.StateConflict;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by rohan on 2/23/17.
 */
public class StateConflictionMapper implements ExceptionMapper<StateConflict> {
    @Override
    public Response toResponse(StateConflict exception)
    {
        return Response.status(Response.Status.CONFLICT).entity(exception.getMessage()).build();
    }
}