package it.gtug.gadc.trelloid.services;

import it.gtug.gadc.trelloid.model.Board;
import it.gtug.gadc.trelloid.model.Member;

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
    Member findMembers(@PathParam("memberid") String memberid, @QueryParam("key") String key);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{memberid}/boards")
    List<Board> listMemberBoards(@PathParam("memberid") String memberid, @QueryParam("key") String key);
}
