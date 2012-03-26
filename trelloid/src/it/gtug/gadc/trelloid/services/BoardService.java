package it.gtug.gadc.trelloid.services;

import it.gtug.gadc.trelloid.model.Board;
import it.gtug.gadc.trelloid.model.CardContainer;
import it.gtug.gadc.trelloid.model.Checklist;
import it.gtug.gadc.trelloid.model.Member;
import it.gtug.gadc.trelloid.model.Organization;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;


/*
 * 
 * https://api.trello.com/1/board/4f3f6245e4b1f2a0023665c8/lists?key=5ab5ad43320989a74b677ab82a349db2
 * 
 */

@Path("/1/boards")
public interface BoardService {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{boardid}/lists")
    List<CardContainer> findListsForBoard(@PathParam("boardid") String boardid, @QueryParam("key") String key, @QueryParam("token") String token);
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{boardid}/lists")
    List<CardContainer> findListsForBoardPubs(@PathParam("boardid") String boardid, @QueryParam("key") String key);
    //TODO: da cambiare in List<ListSimple>
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{boardid}")
    Board getBoard(@PathParam("boardid") String boardid, @QueryParam("key") String key, @QueryParam("token") String token);
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{boardid}/checklists")
    List<Checklist> listChecklists(@PathParam("boardid") String boardid, @QueryParam("key") String key);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{boardid}/members")
    List<Member> listMembers(@PathParam("boardid") String boardid, @QueryParam("key") String key);
 
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{boardid}/organization")
    Organization getOrganization(@PathParam("boardid") String boardid, @QueryParam("key") String key);
 
    
}


