package com.BigBangChat.BBC.entities;


import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {

    public UserEntity() {

    }

    public UserEntity(int id, String name, String mail, String password, Role role) {

        this.id = id;
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.role = role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;


    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String mail;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserEntity{" + "id=" + id + ", name='" + name + '\'' + ", mail='" + mail + '\'' + ", password='" + password + '\'' + ", role=" + role + '}';
    }


}
