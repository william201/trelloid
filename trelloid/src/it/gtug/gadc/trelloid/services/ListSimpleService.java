package it.gtug.gadc.trelloid.services;

import it.gtug.gadc.trelloid.model.Checklist;
import it.gtug.gadc.trelloid.model.ListSimple;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * RestEasy client interface to List API  
 * @see https://trello.com/docs/api/list/index.html
 * 
 */
@Path("/1/lists")
public interface ListSimpleService {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{listsimpleid}")
    ListSimple getListSimple(@PathParam("listsimpleid") String notificationid, @QueryParam("key") String key);
    
    

}
