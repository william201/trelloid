package it.gtug.gadc.trelloid.model;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
/**
 * E' una classe con le preferences di una board
 * @author malo
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoardPrefs implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean selfJoin;
	private String invitations;
	private String comments;
	private String voting;
	private String permissionLevel;
	public boolean isSelfJoin() {
		return selfJoin;
	}
	public void setSelfJoin(boolean selfJoin) {
		this.selfJoin = selfJoin;
	}
	public String getInvitations() {
		return invitations;
	}
	public void setInvitations(String invitations) {
		this.invitations = invitations;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getVoting() {
		return voting;
	}
	public void setVoting(String voting) {
		this.voting = voting;
	}
	public String getPermissionLevel() {
		return permissionLevel;
	}
	public void setPermissionLevel(String permissionLevel) {
		this.permissionLevel = permissionLevel;
	}
	
	
}
