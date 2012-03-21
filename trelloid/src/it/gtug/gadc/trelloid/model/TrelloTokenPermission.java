package it.gtug.gadc.trelloid.model;

import java.io.Serializable;

public class TrelloTokenPermission implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idModel;
    private String modelType;
    private boolean read;
    private boolean write;
	public String getIdModel() {
		return idModel;
	}
	public void setIdModel(String idModel) {
		this.idModel = idModel;
	}
	public String getModelType() {
		return modelType;
	}
	public void setModelType(String modelType) {
		this.modelType = modelType;
	}
	public boolean isRead() {
		return read;
	}
	public void setRead(boolean read) {
		this.read = read;
	}
	public boolean isWrite() {
		return write;
	}
	public void setWrite(boolean write) {
		this.write = write;
	}
    
    
}
