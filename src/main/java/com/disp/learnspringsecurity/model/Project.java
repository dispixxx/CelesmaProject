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
    private User ownerUser; // Связь с пользователем

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProjectMember> members = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "project_applicants",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> applicants = new HashSet<>();

    public void addMember(User user, ProjectRole role) {
        ProjectMember member = new ProjectMember();
        member.setProject(this);
        member.setUser(user);
        member.setRole(role);
        this.members.add(member);
    }

    public void updateMemberRole(ProjectMember projectMember, ProjectRole role) {
        if (members.contains(projectMember)){
            projectMember.setRole(role);
        }
    }

    public void deleteMember(ProjectMember projectMember) {
        members.remove(projectMember);
    }

    public ProjectRole getMemberRole(User user) {
        for (ProjectMember member : members) {
            if (member.getUser().equals(user)) {
                return member.getRole();
            }
        }
        return null;
    }

    public Set<ProjectMember> getMembers() {
        return members;
    }

    public void setMembers(Set<ProjectMember> members) {
        this.members = members;
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

    public User getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(User ownerUser) {
        this.ownerUser = ownerUser;
    }

    public Set<User> getApplicants() {
        return applicants;
    }

    public void setApplicants(Set<User> applicants) {
        this.applicants = applicants;
    }
}
