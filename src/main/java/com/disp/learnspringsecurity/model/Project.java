package com.disp.learnspringsecurity.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Entity
@Data
@Getter
@Setter
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
//    private Long ownerId;
    @ManyToOne
    @JoinColumn(name = "owner_user")
    private MyUser ownerUser; // Связь с пользователем

    @ManyToMany
    @JoinTable(
            name = "project_members",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<MyUser> members = new HashSet<>();

    public Set<MyUser> getMembers() {
        return members;
    }

    public void setMembers(Set<MyUser> participants) {
        this.members = participants;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MyUser getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(MyUser ownerUser) {
        this.ownerUser = ownerUser;
    }

}
