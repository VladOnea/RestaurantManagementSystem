package com.inn.restaurant.model;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import java.io.Serializable;

@NamedQuery(name="User.findByEmailId",query="select u from User u where u.email=:email")

@NamedQuery(name="User.getAllUser",query="select new com.inn.restaurant.wrapper.UserWrapper(u.id,u.name,u.email,u.telephoneNumber,u.status) from User u where u.role='user'")

@NamedQuery(name = "User.updateStatus", query = "update User u set u.status=:status where u.id=:id")

@NamedQuery(name = "User.getAllAdmin", query = "select u.email from User u where u.role='admin'")

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name="user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name="telephoneNumber")
    private String telephoneNumber;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="status")
    private String status;

    @Column(name="role")
    private String role;
}