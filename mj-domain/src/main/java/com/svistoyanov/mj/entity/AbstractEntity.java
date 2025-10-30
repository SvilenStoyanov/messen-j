package com.svistoyanov.mj.entity;

import java.util.UUID;

public abstract class AbstractEntity {
    protected UUID id;

    private int version;

    protected AbstractEntity() {
    }

    protected AbstractEntity(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
