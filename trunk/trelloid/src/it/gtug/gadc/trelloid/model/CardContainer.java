package it.gtug.gadc.trelloid.model;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Questa classe rappresenta cio' che nel dominio applicativo si chiama LISTA
 * (con all'interno le CARDS)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardContainer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idBoard;
	private String id;
	private String name;

	private List<Card> cards;

	public CardContainer() {

	}

	public CardContainer(String name) {
		super();
		this.name = name;
	}

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

	public String getIdBoard() {
		return idBoard;
	}

	public void setIdBoard(String idBoard) {
		this.idBoard = idBoard;
	}
}
