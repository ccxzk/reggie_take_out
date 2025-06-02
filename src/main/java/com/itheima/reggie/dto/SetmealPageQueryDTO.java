package com.itheima.reggie.dto;

import lombok.Data;

@Data
public class SetmealPageQueryDTO {
    private Integer page;

    private Integer pageSize;

    private String name;
}
