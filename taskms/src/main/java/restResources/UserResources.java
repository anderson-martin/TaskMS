package restResources;

import objectModels.basicViews.UserBasicView;
import service.TMSService;
import service.TMSServiceImpl;
import service.TMSService.Credential;
import service.exchange.userGroup.DeactivationEffect;
import service.exchange.userGroup.UserRegister;
import service.exchange.userGroup.UserUpdater;
import service.exchange.userGroup.UserView;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by rohan on 2/18/17.
 */
@Path("/users")
public class UserResources {
    // get the list of user for HR only
    private static final TMSService service = TMSServiceImpl.getSingleInstance();
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserBasicView> getAllUsers(@HeaderParam("Authorization") String key) {
        return service.getAllUsers(new Credential(key));
    }
//    {
//        "userItems": [
//        {
//            "id": 69,
//                "userName": "bobchen",
//                "firstName": "Bob",
//                "lastName": "Chen"
//        }
//  ]
//    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserBasicView registerUser(@HeaderParam("Authorization") String key, UserRegister userRegister) {
        return service.registerUser(new Credential(key), userRegister);
    }
//    consume {
//        "contactDetails": {
//        "phoneNumber": "1234567",
//                "streetAddress": "Somestreet 6 C 66",
//                "city": "Pompeii",
//                "postalCode": "02230",
//                "email": "example@example.com"
//    },
//        "status": "ACTIVE",
//            "userName": "bobchen",
//            "firstName": "Bob",
//            "lastName": "Chen"
//    }
//   produce {
//    "id": 69,
//        "userName": "bobchen",
//        "firstName": "Bob",
//        "lastName": "Chen"
//}\

    @Path("/{userId}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public DeactivationEffect deactivateUser(
            @HeaderParam("Authorization") String key, @PathParam("userId") long userId) {
        return service.deactivateUser(new Credential(key), userId);
    }
//    {
//        "contactDetails": {
//                "phoneNumber": "1234567",
//                "streetAddress": "Somestreet 6 C 66",
//                "city": "Pompeii",
//                "postalCode": "02230",
//                "email": "example@example.com"
//    },
//        "status": "HR_MANAGER",
//            "userName": "bobchen",
//            "firstName": "Bob",
//            "lastName": "Chen",
//            "id": 69,
//        "managerGroups": [
//        {
//            "id": 69,
//                "name": "cashiers"
//        }
//  ],
//        "groups": [
//        {
//            "id": 69,
//                "name": "cashiers"
//        }
//  ],
//        "subordinateGroups": [
//        {
//            "id": 69,
//                "name": "cashiers"
//        }
//  ]
//    }

    // get user deep view
    @Path("/{userId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public UserView getUserInfo(@HeaderParam("Authorization") String key, @PathParam("userId") long userId) {
        return service.getUserInfo(new Credential(key), userId);
    }
//    {
//        "contactDetails": {
//        "phoneNumber": "1234567",
//                "streetAddress": "Somestreet 6 C 66",
//                "city": "Pompeii",
//                "postalCode": "02230",
//                "email": "example@example.com"
//    },
//        "status": "HR_MANAGER",
//            "userName": "bobchen",
//            "firstName": "Bob",
//            "lastName": "Chen",
//            "id": 69,
//        "groups": [
//        {
//            "id": 69,
//                "name": "cashiers"
//        }
//  ],
//    }


    // update user
    @Path("/{userId}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserView updateUser(@HeaderParam("Authorization") String key, @PathParam("userId") long userId, UserUpdater updater) {
        return service.updateUser(new Credential(key), userId, updater);
    }
    // consume
//    {
//        "firstName": "Bob",
//            "lastName": "Chen",
//            "groups": [
//        0
//  ],
//        "status": "ACTIVE",
//            "contactDetails": {
//        "phoneNumber": "1234567",
//                "streetAddress": "Somestreet 6 C 66",
//                "city": "Pompeii",
//                "postalCode": "02230",
//                "email": "example@example.com"
//    }
//    }
//    produces
// {
//        "contactDetails": {
//        "phoneNumber": "1234567",
//                "streetAddress": "Somestreet 6 C 66",
//                "city": "Pompeii",
//                "postalCode": "02230",
//                "email": "example@example.com"
//    },
//        "status": "HR_MANAGER",
//            "userName": "bobchen",
//            "firstName": "Bob",
//            "lastName": "Chen",
//            "id": 69,
//            "managerGroups": [
//        {
//            "id": 69,
//                "name": "cashiers"
//        }
//  ],
//        "groups": [
//        {
//            "id": 69,
//                "name": "cashiers"
//        }
//  ],
//        "subordinateGroups": [
//        {
//            "id": 69,
//                "name": "cashiers"
//        }
//  ]
//    }
}
