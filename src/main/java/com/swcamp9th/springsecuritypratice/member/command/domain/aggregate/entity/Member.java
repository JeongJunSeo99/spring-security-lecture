package com.swcamp9th.springsecuritypratice.member.command.domain.aggregate.entity;

import com.swcamp9th.springsecuritypratice.security.oauth2.SocialType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_member")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 50)
    private String email;

    @Column
    private String password;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String memberUniqueId; // 회원 가입 시 생기는 고유 번호 (닉네임 개념)

    @Enumerated(EnumType.STRING)
    @Column
    private SocialType socialType;

    @Column
    private String socialId;

    @Column
    private String imageUrl;

}
