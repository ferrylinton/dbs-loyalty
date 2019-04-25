package com.dbs.loyalty.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MappedSuperclass
public abstract class AbstractTask extends AbstractAuditing {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
	@Column(name = "pending", nullable = true)
	private Boolean pending;

}

