package it.gtug.gadc.trelloid.model;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Notification implements Serializable {
    /**
     * serialVersionUID generated for Class
     */
    private static final long serialVersionUID = -8960017235140472918L;
    private String id;
    private Boolean unread;
    private String type;
    private String date;
    private String idMemberCreator;
    private NotificationData data;
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
    public void setData(NotificationData data) {
        this.data = data;
    }
    public NotificationData getData() {
        return data;
    }
    public void setIdMemberCreator(String idMemberCreator) {
        this.idMemberCreator = idMemberCreator;
    }
    public String getIdMemberCreator() {
        return idMemberCreator;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getDate() {
        return date;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
    public void setUnread(Boolean unread) {
        this.unread = unread;
    }
    public Boolean getUnread() {
        return unread;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
}
