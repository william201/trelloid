package it.gtug.gadc.trelloid.model;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Board implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;

	private List<CardContainer> containers;

	public Board() {
		
	}
	
	public Board(String id, String name) {
		this.id=id;
		this.name=name;
	}

	public List<CardContainer> getContainers() {
		return containers;
	}

	public void setContainers(List<CardContainer> containers) {
		this.containers = containers;
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

	@Override
	public String toString(){
		return name;
	}
}
