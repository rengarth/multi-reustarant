package com.education.restaurantservice.dto.menu;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CategoryDTO {
    private Long id;
    private String name;
    private Long parentId;

    @JsonProperty("isDeleted")
    private Boolean isDeleted;

    @JsonIgnore
    private CategoryDTO parent;
}
