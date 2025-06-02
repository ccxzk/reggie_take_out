package com.itheima.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Setmeal implements Serializable {
    private static final long serialVersionUID = 1L;

    Long id;

    Long categoryId;

    String name;

    Double price;

    String code;

    String description;

    String image;

    int status;

    @TableField(fill = FieldFill.INSERT)
    LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    Long updateUser;
}
