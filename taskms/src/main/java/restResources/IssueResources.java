package restResources;

import service.TMSService;
import service.TMSServiceImpl;
import service.exchange.msg.IssueCreator;
import service.exchange.msg.IssueUpdater;
import service.exchange.msg.IssueView;
import service.TMSService.Credential;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Set;

/**
 * Created by rohan on 2/18/17.
 */

@Path("/issues")
public class IssueResources {
    public static final TMSService service = TMSServiceImpl.getSingleInstance();
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Set<IssueView> getIssues(@HeaderParam("Authorization") String key) {
        return service.getIssues(new Credential(key));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public IssueView createNewIssue(@HeaderParam("Authorization") String key, IssueCreator issueCreator) {
        return service.createIssue(new Credential(key), issueCreator);
    }

    @Path("/{issueId}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public IssueView deleteIssue(@HeaderParam("Authorization") String key, @PathParam("issueId") long issueId){
        return service.deleteIssue(new Credential(key), issueId);
    }

    @Path("/{issueId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public IssueView getIssueById(@HeaderParam("Authorization") String key, @PathParam("issueId") long issueId) {
        return service.getIssue(new Credential(key), issueId);
    }

    @Path("/{issueId}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public IssueView updateTask(
            @HeaderParam("Authorization") String key, @PathParam("issueId") long issueId, IssueUpdater issueUpdater){
        return service.updateIssue(new Credential(key), issueId, issueUpdater );
    }
}
