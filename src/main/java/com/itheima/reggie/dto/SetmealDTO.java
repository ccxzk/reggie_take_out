package com.itheima.reggie.dto;

import com.itheima.reggie.entity.SetmealDish;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SetmealDTO {
    Long id;

    String name;

    Long categoryId;

    Double  price;

    String image;

    String description;

    List<SetmealDish> setmealDishes;
}
