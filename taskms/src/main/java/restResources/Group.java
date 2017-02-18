package restResources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by rohan on 2/18/17.
 */
@Path("/group")
public class Group {
    // get group information
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getGroupInformation() {
        throw new UnsupportedOperationException();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String creatGroup() {
        throw new UnsupportedOperationException();
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
    public void deactivateGroup() {}

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
    @Produces(MediaType.APPLICATION_JSON)
    public void updateGroup(){}
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
