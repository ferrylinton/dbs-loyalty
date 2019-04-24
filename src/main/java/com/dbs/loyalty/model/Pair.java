package com.dbs.loyalty.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@ApiModel(value="SimpleData", description = "Simple data")
public class Pair<K, V> {

	@ApiModelProperty(value = "Data's key", example = "message", required = true)
	@NonNull
	private K key;
	
	@ApiModelProperty(value = "Data's value", example = "success", required = true)
	@NonNull
	private V value;

}
