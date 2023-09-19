package com.axonactive.agileskills.position.service;

import com.axonactive.agileskills.base.utility.BaseCache;
import com.axonactive.agileskills.position.entity.PositionEntity;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SearchPositionCache extends BaseCache<PositionEntity> {
    public SearchPositionCache() {
        super();
    }
}
