package com.axonactive.agileskills.base.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppConfigService {
    private static final String SECRET_KEY = "agileskills";
    private static final String ISSUER = "nonIT";

    private static final int TIME_TO_LIVE = 720000000;

    public static String getSecretKey() {
        return SECRET_KEY;
    }

    public static String getIssuer() {
        return ISSUER;
    }

    public static Integer getTimeToLive() {
        return TIME_TO_LIVE;
    }
}
