package com.example.userservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class RegisteredUserCredentials {
    @Id
    @GeneratedValue
    private Long id;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] password;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] passwordSalt;

}
