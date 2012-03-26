package it.gtug.gadc.trelloid.model;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Checklist implements Serializable {
    /**
     * serialVersionUID generated for Class
     */
    private static final long serialVersionUID = 3761576119549486628L;
    private String id;
    private String name;
    private List<Card> cards;
    private List<Checkitem> checkitems;
    
/*
{
    "id": "4eea6ae1a5da7f5a49000092",
    "name": "API Checklist",
    "cards": [{
        "id": "4eea522c91e31d174600027e",
        "name": "Figure out how to read a user's board list"
    }],
    "checkitems": [{
        "id": "4eea6aeda5da7f5a490000b9",
        "name": "See if there is a call",
        "type": "check"
    },
     {
        "id": "4eea6af1a5da7f5a490000cc",
        "name": "Figure out how to use the call",
        "type": "check"
    }, 
    {
        "id": "4eea6af4a5da7f5a490000e1",
        "name": "Add it to the code",
        "type": "check"
    }]
}
*/
    
    public void setCheckitems(List<Checkitem> checkitems) {
        this.checkitems = checkitems;
    }
    public List<Checkitem> getCheckitems() {
        return checkitems;
    }
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
    public List<Card> getCards() {
        return cards;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
  
}
