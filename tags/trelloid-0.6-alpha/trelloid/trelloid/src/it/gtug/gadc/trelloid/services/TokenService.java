package it.gtug.gadc.trelloid.services;

import it.gtug.gadc.trelloid.model.TokenData;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("1/tokens")
public interface TokenService {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{token}/member")
    TokenData getTokenMemberInfo(@PathParam("token") String token,@QueryParam("key")String key);

}
