package com.itheima.reggie.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class EmployeeDTO {
    Long id;

    String name;

    String username;

    String phone;

    String sex;

    String idNumber;

    Integer status;

}
