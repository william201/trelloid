package it.gtug.gadc.trelloid.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.DataFormatException;

import android.util.Log;

public class TrelloToken implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String TAG = "TrelloToken";
	private  String   idMember;
	private  String dateCreated;
	private  String dateExpires;
	private  TrelloTokenPermission permissions;
	//Example: "2011-12-14T00:46:24.534Z"
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-ggThh:mm:ss.SSS'Z");
	public String getIdMember() {
		return idMember;
	}
	public void setIdMember(String idMember) {
		this.idMember = idMember;
	}
	public String getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getDateExpires() {
		return dateExpires;
	}
	public void setDateExpires(String dateExpires) {
		this.dateExpires = dateExpires;
	}
	public TrelloTokenPermission getPermissions() {
		return permissions;
	}
	public void setPermissions(TrelloTokenPermission permissions) {
		this.permissions = permissions;
	}
	public boolean isValid() {
		Date expire=null;
		try{
			expire=sdf.parse(dateExpires);
		} catch (ParseException e) {
			Log.e(TAG, "Impossibile fare il parse di "+dateExpires);
			throw new RuntimeException("Impossibile fare il parse dell'eccezione");
		}
		if(new Date().before(expire)){
			return true;
		}
		return false;
	}
	


}
