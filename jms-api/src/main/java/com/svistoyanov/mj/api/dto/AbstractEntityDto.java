package com.svistoyanov.mj.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public abstract class AbstractEntityDto extends AbstractDto
{
    @JsonProperty("id")
    private UUID id;

    protected AbstractEntityDto()
    {
    }

    protected AbstractEntityDto(UUID id)
    {
        this.id = id;
    }

    public UUID getId()
    {
        return id;
    }

    public void setId(UUID id)
    {
        this.id = id;
    }
}
