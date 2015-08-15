package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
public class Committee extends BasicEntity implements Serializable {

    @ManyToMany(mappedBy = "committees" , cascade = {CascadeType.REFRESH,CascadeType.MERGE} , fetch = FetchType.EAGER)
    @JsonView(Views.CommitteeList.class)
    private Set<Contact> members;

    @OneToMany(mappedBy="committee", cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private Set<Event> events;

    @Column
    @JsonView({Views.CommitteeList.class,
            Views.ContactDetails.class,
            Views.ContactCommitteeDetails.class,
            Views.EventList.class})
    private String name;

    public Committee (String id) {
        setId(id);
    }

    public Committee() {
        super();
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }

    public Set<Contact> getMembers() {
        return members;
    }

    public void setMembers(Set<Contact> members) {
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
