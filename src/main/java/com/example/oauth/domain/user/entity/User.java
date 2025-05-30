package com.example.oauth.domain.user.entity;

import com.example.oauth.common.entity.BaseEntity;
import com.example.oauth.common.enums.OAuth;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column
    private String password;

    @Column
    private String oauthId;

    @Enumerated(EnumType.STRING)
    @Column
    private OAuth oauthProvider;

}
