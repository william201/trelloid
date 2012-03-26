package it.gtug.gadc.trelloid.services;

import it.gtug.gadc.trelloid.model.Checklist;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;



/**
 * RestEasy client interface to Checklists API  
 * @see https://trello.com/docs/api/checklist/index.html
 * 
 */
@Path("/1/checklists")
public interface ChecklistService {
    /**
     * Retrieve check list data.
     * For a list of Checklists see {@link it.gtug.gadc.trelloid.services.CardService #listChecklists(String, String)}.
     * 
     * @param notificationid
     * @param key
     * @return a Checklist object
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{checklistid}")
    Checklist getChecklist(@PathParam("checklistid") String notificationid, @QueryParam("key") String key);
    
    
}
