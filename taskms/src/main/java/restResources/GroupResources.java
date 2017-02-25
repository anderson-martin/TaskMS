package restResources;

import objectModels.basicViews.GroupBasicView;
import service.TMSService;
import service.TMSServiceImpl;
import service.exchange.userGroup.DeactivationEffect;
import service.exchange.userGroup.GroupRegister;
import service.TMSService.Credential;
import service.exchange.userGroup.GroupUpdater;
import service.exchange.userGroup.GroupView;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by rohan on 2/18/17.
 */
@Path("/group")
public class GroupResources {
    private static final TMSService service = TMSServiceImpl.getSingleInstance();

    // get group information
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<GroupBasicView> getAllGroups(@HeaderParam("Authorization") String key) {
        return service.getAllGroups(new Credential(key));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public GroupBasicView registerGroup(@HeaderParam("Authorization") String key, GroupRegister groupRegister) {
        return service.registerGroup(new Credential(key), groupRegister);
    }
//    consume:
//    {
//        "name": "cashiers"
//    }
//
//    produces:
//    {
//        "id": 69,
//            "name": "cashiers"
//    }

    @Path("/{groupId}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public DeactivationEffect deactivateGroup(
            @HeaderParam("Authorization") String key, @PathParam("groupId") long groupId) {
        return service.deactivateGroup(new Credential(key), groupId);
    }

// produces
//    {
//        "users": [
//        {
//            "id": 69,
//                "userName": "bobchen",
//                "firstName": "Bob",
//                "lastName": "Chen"
//        }
//  ],
//        "superGroup": {
//        "id": 69,
//                "name": "cashiers"
//    },
//        "subGroups": [
//        {
//            "id": 69,
//                "name": "cashiers"
//        }
//  ],
//        "id": 69,
//            "name": "cashiers"
//    }

    @Path("/{groupId}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public GroupView updateGroup(@HeaderParam("Authorization") String key,
                                 @PathParam("groupId") long groupId, GroupUpdater groupUpdater){
        return service.updateGroup(new Credential(key), groupId, groupUpdater);
    }

    @Path("/{groupId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public GroupView getGroupInfo(@HeaderParam("Authorization") String key, @PathParam("groupId") long groupId) {
        return TMSServiceImpl.getSingleInstance().getGroupInfo(new Credential(key), groupId);
    }

//    consume:
//{
//  "name": "cashiers",
//  "users": [
//    0
//  ],
//  "managerGroup": 0,
//  "subordinateGroup": 0
//}
//
//produce:
//    {
//  "users": [
//    {
//      "id": 69,
//      "userName": "bobchen",
//      "firstName": "Bob",
//      "lastName": "Chen"
//    }
//  ],
//  "superGroup": {
//    "id": 69,
//    "name": "cashiers"
//  },
//  "subGroups": [
//    {
//      "id": 69,
//      "name": "cashiers"
//    }
//  ],
//  "id": 69,
//  "name": "cashiers"
//}
}
