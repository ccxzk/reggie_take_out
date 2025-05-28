package com.itheima.reggie.dto;

import lombok.Data;

@Data
public class DishDTO {
    String name;

    Double price;

    Long categoryId;

    String image;

    String description;
}
