package ru.markov.application.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_roles")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userRoleId;

    @ManyToOne
    @JoinColumn(name = "username")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    public Long getUserRoleId() {return userRoleId;}
    public void setUserRoleId(Long userRoleId) {this.userRoleId = userRoleId;}
    public UserEntity getUser() {return user;}
    public void setUser(UserEntity user) {this.user = user;}
    public RoleEntity getRole() {return role;}
    public void setRole(RoleEntity role) {this.role = role;}

}