package ru.markov.application.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    private String username;
    private String password;
    private boolean enabled;

    public void setUsername(String username) {this.username = username;}
    public void setPassword(String password) {this.password = password;}
    public void setEnabled(boolean enabled) {this.enabled = enabled;}
    public String getUsername() {return this.username;}
    public String getPassword() {return this.password;}
    public boolean isEnabled() {return this.enabled;}
}