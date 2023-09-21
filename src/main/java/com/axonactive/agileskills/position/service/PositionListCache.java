package com.axonactive.agileskills.position.service;

import com.axonactive.agileskills.base.utility.BaseCache;
import com.axonactive.agileskills.position.entity.PositionEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PositionListCache extends BaseCache<PositionEntity> {

    public static final int CACHE_SIZE = 2;
    public static final int EXPIRED_DAYS = 7;

    public PositionListCache() {
        super(CACHE_SIZE, EXPIRED_DAYS);
    }
}
