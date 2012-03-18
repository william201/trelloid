package it.gtug.gadc.trelloid.services;

import it.gtug.gadc.trelloid.model.Board;
import it.gtug.gadc.trelloid.model.Member;

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
    
	
}
