package com.axonactive.agileskills.user.entity;

import com.axonactive.agileskills.base.entity.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "agile_skills_user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String name;

    @Column(unique = true, nullable = false)
    @Email
    private String email;

    @Column(nullable = false)
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z]).{6,}$")
    private String password;

    @Column
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Column
    @Enumerated(EnumType.STRING)
    private RoleEnum role;
}
