package it.gtug.gadc.trelloid.services;

import it.gtug.gadc.trelloid.model.CardContainer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;


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
    List<CardContainer> findListsForBoard(@PathParam("boardid") String boardid, @QueryParam("key") String key);
}
