package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDTO;
import com.itheima.reggie.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDTO dishDTO);

    void updateWithFlavor(DishDTO dishDto);

    void updateStatusByIds(Integer status, List<Long> ids);

}
