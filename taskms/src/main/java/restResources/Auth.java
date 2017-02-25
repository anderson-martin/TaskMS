package restResources;

import service.TMSService;
import service.TMSServiceImpl;
import service.exchange.userGroup.UserView;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by rohan on 2/18/17.
 */
@Path("/auth")
public class Auth {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public UserView getAuthenticationInfo(@HeaderParam("Authorization") String key) {
        return TMSServiceImpl.getSingleInstance().authenticate(new TMSService.Credential(key));
    }

}
