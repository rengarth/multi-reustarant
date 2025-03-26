package com.education.kafkadto.dto.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DishDTO {
    private Long id;
    private String name;
    private String description;
    private Integer pricePerOne;

    @JsonProperty("isDeleted")
    private Boolean isDeleted;

    @JsonProperty("isAvailable")
    private Boolean isAvailable;

    private String categoryName;
}
