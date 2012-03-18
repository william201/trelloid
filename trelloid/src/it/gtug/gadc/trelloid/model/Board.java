package it.gtug.gadc.trelloid.model;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Board implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1123L;
	private String id;
	private String name;
	private String desc;
	private boolean closed;
	private String idOrganization;
	private boolean pinned;
	private String url;
	private BoardPrefs prefs;

	private List<CardContainer> containers;

	
	
	public Board(String id, String name, String desc) {
        this.id=id;
        this.name=name;
        this.desc=desc;
    }

	public Board() {
        this("vuoto","vuoto","default");
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
	
	public boolean isPublic() {
		
		if(this.getPrefs()!=null&&this.getPrefs().getPermissionLevel().equals("public")){
			return true;
		}else{
			return false;
		}
	}
	public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public String getIdOrganization() {
		return idOrganization;
	}

	public void setIdOrganization(String idOrganization) {
		this.idOrganization = idOrganization;
	}

	public boolean isPinned() {
		return pinned;
	}

	public void setPinned(boolean pinned) {
		this.pinned = pinned;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public BoardPrefs getPrefs() {
		return prefs;
	}

	public void setPrefs(BoardPrefs prefs) {
		this.prefs = prefs;
	}
}
