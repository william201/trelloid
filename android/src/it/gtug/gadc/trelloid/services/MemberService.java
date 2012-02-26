package it.gtug.gadc.trelloid.services;

import it.gtug.gadc.trelloid.model.Board;
import it.gtug.gadc.trelloid.model.Member;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/1/members")
public interface MemberService {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{memberid}")
	Member findMembers(@PathParam("memberid") String memberid,
                       @QueryParam("key") String key);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{memberid}/boards")
    List<Board> listMemberBoards(@PathParam("memberid") String memberid,
                                 @QueryParam("key") String key);
}
