package com.itheima.reggie.dto;

import lombok.Data;

@Data
public class EmployeePageQueryDTO {
    String name;

    Long page;

    Long pageSize;

}
