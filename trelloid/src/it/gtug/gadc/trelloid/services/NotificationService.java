package it.gtug.gadc.trelloid.services;

import it.gtug.gadc.trelloid.model.Notification;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * RestEasy client interface to Notification API  
 * @see https://trello.com/docs/api/notification/index.html
 * 
 */
@Path("/1/notifications")
public interface NotificationService {

    /**
     * Get all notification data for only one notification.
     * For list of notifications see {@link it.gtug.gadc.trelloid.services.MemberService #listNotifications(String, String)}.
     * 
     * @param notificationid
     * @param key
     * @return a Notification Object
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{notificationid}")
    Notification getNotification(@PathParam("notificationid") String notificationid, @QueryParam("key") String key);
    
}
