package com.crevan.telrostesttask;

import org.springframework.util.Assert;

public interface HasId {

    Integer getId();

    void setId(final Integer id);

    String getEmail();

    default boolean isNew() {
        return getId() == null;
    }

    //doesn't work for hibernate lazy proxy
    default int id() {
        Assert.notNull(getId(), "Entity must have ID");
        return getId();
    }
}
