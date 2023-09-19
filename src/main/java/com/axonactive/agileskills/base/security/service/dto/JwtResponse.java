package com.axonactive.agileskills.base.security.service.dto;

import com.axonactive.agileskills.base.entity.StatusEnum;
import com.axonactive.agileskills.user.entity.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class JwtResponse {
    private String token;
    private String email;

    private RoleEnum role;

    private String type = "Bearer";
    private StatusEnum status;

    public JwtResponse(String token, String email, RoleEnum role, StatusEnum status) {
        this.token = token;
        this.email = email;
        this.role = role;
        this.status = status;
    }
}
