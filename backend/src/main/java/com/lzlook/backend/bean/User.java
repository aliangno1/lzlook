package com.lzlook.backend.bean;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Table(name = "user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String avatar;

    private String phone;

    private String email;

    private String password;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}