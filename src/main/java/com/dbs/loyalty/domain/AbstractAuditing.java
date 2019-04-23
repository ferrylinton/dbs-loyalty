package com.dbs.loyalty.domain;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MappedSuperclass
public abstract class AbstractAuditing implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @Column(name = "created_by", length = 50, nullable = false, updatable = false)
    private String createdBy;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate;
    
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

}

