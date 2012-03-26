package it.gtug.gadc.trelloid.model;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationData implements Serializable{
    /**
     * serialVersionUID generated for Class
     */
    private static final long serialVersionUID = -6082461656168762998L;
    private Board board;
    private Card card;
    private String text;
    /*   
    {"id":"4f40c2b9a5ae9a4759017114",
        "unread":false,
        "type":"commentCard",
        "date":"2012-02-19T09:36:57.872Z",
    
        "data":{
          "board":{
            "id":"4f3f6245e4b1f2a0023665c8",
            "name":"TrelloAndroid"
            },
          "card":{
             "id":"4f3fa1c7b23069041241fd87",
             "name":"Elenco delle board"
           },
          "text":"Per generare un token orario https://trello.com/1/appKey/generate"
        },
        
        "idMemberCreator":"4f3e3224248f35f87b0c591e"
     },
     */
    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }
    public void setCard(Card card) {
        this.card = card;
    }
    public Card getCard() {
        return card;
    }
    public void setBoard(Board board) {
        this.board = board;
    }
    public Board getBoard() {
        return board;
    }
}
