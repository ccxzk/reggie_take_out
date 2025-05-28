package com.itheima.reggie.dto;

import lombok.Data;

@Data
public class DishPageQueryDTO {
    String name;

    Long page;

    Long pageSize;
}
