package restResources;

import service.TMSService;
import service.TMSServiceImpl;
import service.exchange.msg.TaskCreator;
import service.exchange.msg.TaskUpdater;
import service.exchange.msg.TaskView;
import service.TMSService.Credential;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by rohan on 2/18/17.
 */
@Path("/tasks")
public class TaskResources {
    public static final TMSService service = TMSServiceImpl.getSingleInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaskView> getTasks(@HeaderParam("Authorization") String key) {
        return service.getTasks(new Credential(key));
    }
    // create new task
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TaskView createTask(@HeaderParam("Authorization") String key, TaskCreator taskCreator) {
        return service.createTask(new Credential(key), taskCreator);
    }

    @Path("/{taskId}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public TaskView deleteTask(@HeaderParam("Authorization") String key, @PathParam("taskId") long taskId) {
        return service.deleteTask(new Credential(key), taskId);
    }

    @Path("/{taskId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public TaskView getTaskById(@HeaderParam("Authorization") String key, @PathParam("taskId") long taskId) {
        return service.getTask(new Credential(key), taskId);
    }

    @Path("/{taskId}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TaskView updateTask(
            @HeaderParam("Authorization") String key, @PathParam("taskId") long taskId, TaskUpdater taskUpdater){
        return service.updateTask(new Credential(key),taskId, taskUpdater);
    }
}
