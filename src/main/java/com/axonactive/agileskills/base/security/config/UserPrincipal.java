package com.axonactive.agileskills.base.security.config;

import com.axonactive.agileskills.user.entity.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.Principal;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserPrincipal implements Principal {
    private String email;
    private RoleEnum role;

    @Override
    public String getName() {
        return email;
    }
}
