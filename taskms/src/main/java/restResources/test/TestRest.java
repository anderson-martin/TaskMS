package restResources.test;

import service.exception.StateConflict;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by rohan on 2/8/17.
 */
@Path("/sayHello")
public class TestRest {

    @Path("/json")
    @GET
    @Produces("application/json")
    public TestRestSerializedClass testJson() {
        TestRestSerializedClass vit = new TestRestSerializedClass(10,"vittu perkele", "cai dam","02660");
        vit.setStatus(TestRestSerializedClass.STATUS.FAILED);
//        vit.setAddress(new Vit.Address("asd","03321"));
        return vit;
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public TestRestSerializedClass testJsonConsume(TestRestSerializedClass vit) {
        System.out.println(vit);
        return vit;
    }

    @GET
    @Produces("application/json")
    public String seyHello() {
        return "vittu";
    }

    @Path("/auth")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getHeaderAuth(@HeaderParam("Authorization") String key) {
        return "Hei , you've given us key: <" + key + ">";
    }

    @Path("/content")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getHeaderContentType(@HeaderParam("Content-Type") String type) {
        return "Hei , you're provided us with content type: <" + type + ">";
    }

    @GET
    @Path("/retrieve/{uuid}")
    public Response retrieveSomething(@PathParam("uuid") String uuid) {

        /*
        resource for hint:
        https://jersey.java.net/documentation/latest/representations.html


         */
        if(uuid == null || uuid.trim().length() == 0) {
            return Response.serverError().entity("UUID cannot be blank").build();
        }
        long num = Integer.parseInt(uuid.trim());
        // 404
        if(num == 1) {
            return Response.status(Response.Status.NOT_FOUND).entity("Entity not found for UUID: " + uuid).build();
        }
        // 403 forbidden
        if(num == 2) {
            return Response.status(Response.Status.FORBIDDEN).entity("vittu perkele").build();
        }

        if(num == 4) {
            throw new ForbiddenException("You fucked upPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
        }

        if(num == 5) {
            throw new WebApplicationException(404);
        }


        if(num == 6) {
            throwMeSomeNiceException();
        }

        return Response.ok(((Long) num).toString(), MediaType.APPLICATION_JSON).build();
    }

    public static void throwMeSomeNiceException() {
        throw new StateConflict("you are fucking noob");
    }
}
