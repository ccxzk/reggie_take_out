package com.itheima.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dish implements Serializable {
    private static final long serialVersionUID = 1L;

    Long id;

    String name;

    Long categoryId;

    Double price;

    String image;

    String code;

    String description;

    int status;

    int sort;

    @TableField(fill = FieldFill.INSERT)
    LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    Long updateUser;
}
