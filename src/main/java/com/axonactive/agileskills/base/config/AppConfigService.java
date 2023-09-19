package com.axonactive.agileskills.base.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ResourceBundle;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppConfigService {
    private static final ResourceBundle rb = ResourceBundle.getBundle("jwt");
    private static final Integer TIME_TO_LIVE = Integer.valueOf(rb.getString("jwt.time-to-live"));

    public static String getSecretKey() {
        return rb.getString("jwt.secret.key");
    }

    public static String getIssuer() {
        return rb.getString("jwt.issuer");
    }

    public static Integer getTimeToLive() {
        return TIME_TO_LIVE;
    }
}
