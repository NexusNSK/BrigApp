package ru.markov.application.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class RoleEntity {
    @Id
    private Long id;
    private String name;

    public void setId(Long id) {this.id = id;}
    public Long getId() {return id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
}