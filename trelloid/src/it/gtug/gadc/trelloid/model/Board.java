package it.gtug.gadc.trelloid.model;

import java.util.List;

public class Board {
	
	private String id;
	private String name;

	private List<CardContainer> containers;

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
