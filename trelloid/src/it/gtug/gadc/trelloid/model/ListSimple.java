package it.gtug.gadc.trelloid.model;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ListSimple implements Serializable {
    /**
     * serialVersionUID generated for Class
     */
    private static final long serialVersionUID = 7531246538147424278L;
    private String id;
    private String name;
    private List<Card> cards;
    
    /*
    {
        "id": "4eea4ffc91e31d174600004a",
        "name": "To Do Soon",
        "cards": [{
            "id": "4eea503791e31d1746000080",
            "name": "Finish my awesome application"
        }]
    }     
    */
    
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
    public List<Card> getCards() {
        return cards;
    }
 

}
