package it.gtug.gadc.trelloid.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Questa classe rappresenta cio' che nel dominio applicativo si chiama LISTA
 * (con all'interno le CARDS)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardContainer {

	private String idParentBoard;
	private String id;
	private String name;

	private List<Card> cards;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	public String getIdParentBoard() {
		return idParentBoard;
	}

	public void setIdParentBoard(String idParentBoard) {
		this.idParentBoard = idParentBoard;
	}
}
