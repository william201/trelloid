package it.gtug.gadc.trelloid.model;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Data implements Serializable {
	/**
     * serialVersionUID generated for Class
     */
    private static final long serialVersionUID = 5404746793293879318L;
    private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
