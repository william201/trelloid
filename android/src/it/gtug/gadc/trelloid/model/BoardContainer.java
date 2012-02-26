package it.gtug.gadc.trelloid.model;

import it.gtug.gadc.trelloid.model.Board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Board List di un utente GET /1/members/[member_id or username]/boards¶
 * 
 * @see https
 *      ://trello.com/docs/api/member/index.html#get-1-members-member-id-or-
 *      username-boards
 * @author kwame
 * 
 */
public class BoardContainer {
	public static final String apiCall = "/1/members/{0}/boards?key={1}&token={2}";
	private HashMap<String, Board> mapOfBoards = new HashMap<String, Board>();

	public BoardContainer(int length) {
		mapOfBoards = new HashMap<String, Board>(length);
	}

	public Board board(String id) {
		return mapOfBoards.get(id);
	}

	public void addBoard(String id, String name) {
		Board petite = new Board(id, name);
		mapOfBoards.put(id, petite);

	}

	public String[] getArray() {
		List<String> listStringBoard = new ArrayList<String>();
		for (Board board : mapOfBoards.values()) {
			listStringBoard.add(board.toString());
		}
		return (String[]) listStringBoard.toArray(new String[listStringBoard
				.size()]);
	}

}
