package it.gtug.gadc.trelloid.services;

import it.gtug.gadc.trelloid.model.Card;
import it.gtug.gadc.trelloid.model.Comment;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/1/cards")
public interface CardService {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{cardid}")
	Card getCard(@PathParam("cardid") String cardId, @QueryParam("key") String key);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{cardid}/actions")
    List<Comment> getComments(@PathParam("cardid") String cardId, @QueryParam("key") String key, @QueryParam("filter") String filter);
}
