package it.gtug.gadc.trelloid.model;

import java.util.List;

public class Organization {
    
    private String id;
    private String name;
    private String desc;
    private String url;
    private String displayName;
    private List<Member> members;
    /*
    {
        "id": "4ee7e59ae582acdec8000291",
        "name": "publicorg",
        "desc": "This is a test organization",
        "members": [{
            "id": "4ee7df3ce582acdec80000b2",
            "username": "alicetester",
            "fullName": "Alice Tester"
        }, {
            "id": "4ee7df74e582acdec80000b6",
            "username": "davidtester",
            "fullName": "David Tester"
        }, {
            "id": "4ee7e2e1e582acdec8000112",
            "username": "edtester",
            "fullName": "Ed Tester"
        }]
    }
    */
    public void setMembers(List<Member> members) {
        this.members = members;
    }
    public List<Member> getMembers() {
        return members;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getDesc() {
        return desc;
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
    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }

}
