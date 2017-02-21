package restResources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by rohan on 2/18/17.
 */
@Path("/tasks")
public class TaskResources {
    // all task associated with user
    // task.recipientGroup = his subordinate group
    // && task where he is in recipient list
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getTasks() {}
//    {
//        "tasks": [
//        {
//            "id": 69,
//                "status": "DONE",
//                "title": "Have fun",
//                "sender": {
//            "id": 69,
//                    "userName": "bobchen",
//                    "firstName": "Bob",
//                    "lastName": "Chen"
//        },
//            "senderGroup": {
//            "id": 69,
//                    "name": "cashiers"
//        },
//            "targetUsers": [
//            {
//                "id": 69,
//                    "userName": "bobchen",
//                    "firstName": "Bob",
//                    "lastName": "Chen"
//            }
//      ],
//            "targetGroup": {
//            "id": 69,
//                    "name": "cashiers"
//        },
//            "deadline": "1994-11-05T08:15:30+02:00"
//        }
//  ]
//    }

    // create new task
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void createNewTask() {}
    // consume
//    {
//        "description": "Some long and boring description of things to do, that gonna annoy any human bein on Earth",
//        "title": "Have fun",
//        "senderId": 4 ,
//        "senderGroupId": 10,
//        "targetUserIds": [1,2,4],
//        "targetGroupId": 4,
//        "deadline": "1994-11-05T08:15:30+02:00"
//    }
//
    // produces
//    {
//        "id": 69,
//            "status": "DONE",
//            "title": "Have fun",
//            "sender": {
//        "id": 69,
//                "userName": "bobchen",
//                "firstName": "Bob",
//                "lastName": "Chen"
//    },
//        "senderGroup": {
//        "id": 69,
//                "name": "cashiers"
//    },
//        "targetUsers": [
//        {
//            "id": 69,
//                "userName": "bobchen",
//                "firstName": "Bob",
//                "lastName": "Chen"
//        }
//  ],
//        "targetGroup": {
//        "id": 69,
//                "name": "cashiers"
//    },
//        "deadline": "1994-11-05T08:15:30+02:00"
//    }

    @Path("/{task_id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteTask(){}

    @Path("/{task_id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getTaskById() {}

    @Path("/{taskId}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void updateTask(){}
}
