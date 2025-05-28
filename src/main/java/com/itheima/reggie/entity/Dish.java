package com.itheima.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Dish {
    Long id;

    String name;

    Long categoryId;

    Double price;

    String image;

    String code;

    String description;

    @TableField(fill = FieldFill.INSERT)
    int status;

    int sort;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    Long createUser;

    @TableField(fill = FieldFill.UPDATE)
    Long updateUser;
}
