package com.example.jmix.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum OnboardingStatus implements EnumClass<String> {

    NOT_STARTED("10"),
    IN_PROGRESS("20"),
    COMPLETED("30");

    private final String id;

    OnboardingStatus(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static OnboardingStatus fromId(String id) {
        for (OnboardingStatus at : OnboardingStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}