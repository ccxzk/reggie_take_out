package com.itheima.reggie.dto;

import com.itheima.reggie.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDTO {

    Long id;

    String name;

    Double price;

    Long categoryId;

    String image;

    String description;

    private List<DishFlavor> flavors = new ArrayList<>();
}
