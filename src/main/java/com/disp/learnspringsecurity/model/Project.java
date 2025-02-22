package com.disp.learnspringsecurity.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


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
        @JoinColumn(name = "ownerUser")
        private MyUser ownerUser; // Связь с пользователем

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

//    public Long getOwnerId() {
//        return ownerId;
//    }
//
//    public void setOwnerId(Long ownerId) {
//        this.ownerId = ownerId;
//    }

    public MyUser getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(MyUser ownerUser) {
        this.ownerUser = ownerUser;
    }

}
