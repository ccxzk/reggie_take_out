package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.dto.DishDTO;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper,Dish> implements DishService {

    @Resource
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品，同时保存对应的口味数据
     * @param dishDTO
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        //保存菜品的基本信息到菜品表dish
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        this.save(dish);

        //获取菜品id
        Long dishId = dish.getId();

        //批量插入菜品口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors = flavors.stream().map(item -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDTO dishDto) {
        //更新菜品信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto,dish);
        this.updateById(dish);

        //获取菜品id
        Long dishId = dish.getId();

        //清理当前菜品对应口味数据
        dishFlavorService.remove(new LambdaQueryWrapper<DishFlavor>().eq(DishFlavor::getDishId,dishId));

        //添加当前提交过来的口味数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map(item -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public void updateStatusByIds(Integer status, List<Long> ids) {
        // 构造更新条件
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Dish::getId, ids);

        // 构造要更新的数据
        Dish dish = new Dish();
        dish.setStatus(status);

        this.update(dish, wrapper);
    }


}
