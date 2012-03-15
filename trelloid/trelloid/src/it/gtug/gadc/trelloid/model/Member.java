package it.gtug.gadc.trelloid.model;

public class Member {

	private String id;
	private String fullName;
	private String initials;
	private String username;
	private String bio;
	private String url;
	private String avatarHash;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;

	}

	public String getAvatarHash() {
		return avatarHash;
	}

	public void setAvatarHash(String avatarHash) {
		this.avatarHash = avatarHash;
	}

}