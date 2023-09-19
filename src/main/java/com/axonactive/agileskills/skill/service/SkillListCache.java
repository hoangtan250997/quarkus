package com.axonactive.agileskills.skill.service;

import com.axonactive.agileskills.base.utility.BaseCache;
import com.axonactive.agileskills.skill.entity.SkillEntity;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SkillListCache extends BaseCache<SkillEntity> {

    public static final int CACHE_SIZE = 2;
    public static final int EXPIRED_DAYS = 7;

    public SkillListCache() {
        super(CACHE_SIZE, EXPIRED_DAYS);
    }
}
