package org.tcpx.sharine.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Calendar;

@Entity
@Data
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    long id;
    @Column(nullable = false)
    String account;
    @Column(nullable = false)
    String password;
    @Column(nullable = false)
    String tags;
    @Column(name = "display_name",nullable = false)
    String displayName;
    @Column(name = "create_time",nullable = false)
    long createTime;
    @Column(name = "update_time",nullable = false)
    long updateTime;
}
