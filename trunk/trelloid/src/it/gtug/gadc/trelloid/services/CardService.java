package it.gtug.gadc.trelloid.services;

import it.gtug.gadc.trelloid.model.Card;
import it.gtug.gadc.trelloid.model.Comment;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/1/cards")
public interface CardService {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{cardid}")
	Card getCard(@PathParam("cardid") String cardId, @QueryParam("key") String key, @QueryParam("token") String token);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{cardid}/actions")
	List<Comment> getComments(@PathParam("cardid") String cardId, @QueryParam("key") String key, @QueryParam("filter") String filter,
			@QueryParam("token") String token);

	@POST
	@Path("/{cardid}/actions/comments")
	@Consumes(MediaType.APPLICATION_JSON)
	void addComment(@PathParam("cardid") String cardId, @QueryParam("key") String key, @QueryParam("token") String token, String text);
}
