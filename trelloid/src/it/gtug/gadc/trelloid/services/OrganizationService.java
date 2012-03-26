package it.gtug.gadc.trelloid.services;

import it.gtug.gadc.trelloid.model.Organization;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;


/**
 * RestEasy client interface to Organizations API  
 * @see https://trello.com/docs/api/organization/index.html
 * 
 */
@Path("/1/organizations")
public interface OrganizationService {
    /**
     * Get the data of an organization.
     * For a list of organizations see {@link it.gtug.gadc.trelloid.services.MemberService #listOrganizations(String, String)}
     * @param notificationid
     * @param key
     * @return an Organization object
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{organizationid}")
    Organization getOrganization(@PathParam("organizationid") String notificationid, @QueryParam("key") String key);

}
