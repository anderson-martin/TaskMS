package restResources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by rohan on 2/18/17.
 */

@Path("/issues")
public class IssueResources {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getIssues() {}

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void createNewIssue() {}

    @Path("/{issueId}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteIssue(){}

    @Path("/{issueId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getIssueById() {}

    @Path("/{taskId}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void updateTask(){}
}
