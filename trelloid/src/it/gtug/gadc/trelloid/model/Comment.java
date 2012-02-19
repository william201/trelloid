package it.gtug.gadc.trelloid.model;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment {
	private String id;
	private String name;
	private String idMemberCreator;

	private Map<Object, Object> data = new HashMap<Object, Object>();

	public String getText() {
		return (String) data.get("text");
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

	public String getIdMemberCreator() {
		return idMemberCreator;
	}

	public void setIdMemberCreator(String idMemberCreator) {
		this.idMemberCreator = idMemberCreator;
	}

	@Override
	public String toString() {
		return getText();
	}

	public Map<Object, Object> getData() {
		return data;
	}

	public void setData(Map<Object, Object> data) {
		this.data = data;
	}
}
