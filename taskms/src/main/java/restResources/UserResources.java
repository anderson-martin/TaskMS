package restResources;

import objectModels.userGroup.ContactDetail;
import objectModels.userGroup.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by rohan on 2/18/17.
 */
@Path("/users")
public class UserResources {
    // get the list of user for HR only
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getAllUsers() {}
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
    public void registerUser() {}
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
    public UserResources deactivateUser() { return null;}
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
    public UserResources getUserInfo(@HeaderParam("Authorization") String key, @PathParam("userId") long id) { return null;}
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

    public class UpdateUser {
        // HR -> update all
        // user -> update his info except staus
        private String firstName;
        private String lastName;
        private List<Long> groups;
        private User.STATUS status;
        private ContactDetail contactDetails;
    }

    // update user
    @Path("/{userId}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void updateUser(@HeaderParam("Authorization") String key, UpdateUser updateUser) {}
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
