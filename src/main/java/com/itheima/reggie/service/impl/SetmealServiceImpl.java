package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.constant.MessageConstant;
import com.itheima.reggie.dto.SetmealDTO;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Resource
    private SetmealDishService setmealDishService;
    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    @Transactional
    public void saveWithDish(SetmealDTO setmealDto) {
        Setmeal setmeal = Setmeal.builder()
                .status(MessageConstant.ENABLED) // 设置套餐状态为启用
                .build();

        //拷贝前端传来的数据
        BeanUtils.copyProperties(setmealDto,setmeal);

        //保存套餐的基本信息，操作setmeal，执行insert操作
        this.save(setmeal);

        //插入套餐对应菜品
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmeal.getId());
            return item;
        }).collect(Collectors.toList());

        //保存套餐和菜品的关联信息，操作setmeal_dish,执行insert操作
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public void updateWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = Setmeal.builder()
                .status(MessageConstant.ENABLED) // 设置套餐状态为启用
                .build();

        //拷贝前端传来的数据
        BeanUtils.copyProperties(setmealDTO,setmeal);

        this.updateById(setmeal);

        //先删除套餐对应的菜品
        setmealDishService.remove(new LambdaQueryWrapper<SetmealDish>().eq(SetmealDish::getSetmealId,setmealDTO.getId()));

        //插入套餐对应菜品
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmeal.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public void updateStatusByIds(Integer status, List<Long> ids) {
        LambdaQueryWrapper <Setmeal> queryWrapper = Wrappers.<Setmeal>lambdaQuery()
                .in(Setmeal::getId,ids);

        Setmeal setmeal = Setmeal.builder()
                .status(status)
                .build();

        this.update(setmeal,queryWrapper);
    }

}
