package restResources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by rohan on 2/18/17.
 */
@Path("/users")
public class User {
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
    public void createUser() {}
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
    public void deactivateUser() {}
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

    // get user deep view
    @Path("/{userId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getUserInfo() {}
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

    // update user
    @Path("{userId}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void updateUser() {}
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
