package com.pos.hyper.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotNull
    String name;

    String address;

    @NotNull
    @Column(unique = true)
    String phone;


    @Column(unique = true)
    String email;

    public int getId() {
        return id;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public @NotNull String getPhone() {
        return phone;
    }

    public void setPhone(@NotNull String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

}
