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
    private Long oauthId;

    @Enumerated(EnumType.STRING)
    @Column
    private OAuth oauthProvider;

    @Column
    private String oauthRefreshToken;

    private User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    private User(String email, Long oauthId, OAuth oauthProvider, String oauthRefreshToken) {
        this.email = email;
        this.oauthId = oauthId;
        this.oauthProvider = oauthProvider;
        this.oauthRefreshToken = oauthRefreshToken;
    }

    public static User create(String email, String password) {
        return new User(email, password);
    }

    public static User createWithOAuth(String email, Long oauthId, OAuth oauthProvider, String oauthRefreshToken) {
        return new User(email, oauthId, oauthProvider, oauthRefreshToken);
    }

    public void updateRefreshToken(String refreshToken) {
        this.oauthRefreshToken = refreshToken;
    }
}
