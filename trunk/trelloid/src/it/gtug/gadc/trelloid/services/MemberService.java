package it.gtug.gadc.trelloid.services;

/**
 * RestEasy client interface to Member API  
 * @see https://trello.com/docs/api/member/index.html
 * Provides the following info for each member:
 *  - actions
 *  - boards
 *  - cards
 *  - notifications
 *  - organizations
 *  - bio
 *  - fullName
 *  - initials
 *  
 * Note: If you specify me as the memberid, the calls will respond as 
 * if you had supplied the username associated with the supplied token.
 */
import it.gtug.gadc.trelloid.model.Board;
import it.gtug.gadc.trelloid.model.Member;
import it.gtug.gadc.trelloid.model.Notification;
import it.gtug.gadc.trelloid.model.Organization;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/1/members")
public interface MemberService {
    /**
     * @see https://trello.com/docs/api/member/index.html#get-1-members-member-id-or-username
     * @param memberid If you specify me as the username, this call will respond as if you had supplied the username associated with the supplied token
     * @param key trello auth key
     * @return a Member object
     */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{memberid}")
	Member findMembers(@PathParam("memberid") String memberid,
			@QueryParam("key") String key);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/me")
	Member findMe(@QueryParam("key") String key,
			@QueryParam("token") String token);

	/**
	 * List of ALL historic notifications unfiltered!
	 * @see https://trello.com/docs/api/member/index.html#get-1-members-member-id-or-username-notifications
	 * @param memberid
	 * @param key
	 * @return list of ALL notifications unfiltered!
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{memberid}/notifications")
    List<Notification> listNotifications(@PathParam("memberid") String memberid,
            @QueryParam("key") String key);
    
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{memberid}/boards")
	List<Board> listMemberBoardsPubs(@PathParam("memberid") String memberid,
			@QueryParam("key") String key);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{memberid}/boards")
	List<Board> listMemberBoards(@PathParam("memberid") String memberid,
	            @QueryParam("key") String key,@QueryParam("token") String token);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/my/boards")
	ArrayList<Board> findBoardsWichHeIsMember(@QueryParam("key") String key,
			@QueryParam("token") String token);
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/my/boards/public")
    List<Board> findPublicBoardsWichHeIsMember(@QueryParam("key") String key,
            @QueryParam("token") String token);
    
	 @GET
	 @Produces(MediaType.APPLICATION_JSON)
	 @Path("/{memberid}/organizations")
	 List<Organization> listOrganizations(@PathParam("memberid") String boardid, @QueryParam("key") String key);
	 
	
}
