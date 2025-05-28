package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDTO;
import com.itheima.reggie.dto.DishPageQueryDTO;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Resource
    private DishService dishService;

    @Resource
    private CategoryService categoryService;

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public R page(DishPageQueryDTO dishPageQueryDTO){
        log.info("分页查询：{}",dishPageQueryDTO);

        Dish dish = new Dish();

        //构造分页构造器
        Page page = new Page(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());

        //构造条件构造器
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.like(StringUtils.isNotEmpty(dishPageQueryDTO.getName()),Dish::getName,dishPageQueryDTO.getName()); // 添加过滤条件
        lambdaQueryWrapper.orderByAsc(Dish::getCreateTime); // 添加排序条件

        //分页查询
        dishService.page(page,lambdaQueryWrapper);

        //将结果返回
        return R.success(page);
    }



    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    public R save(@RequestBody DishDTO dishDTO){
        log.info("菜品信息：{}",dishDTO);

        //创建菜品对象，并拷贝前端传来的数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);

        dishService.save(dish);

        return R.success("新增菜品成功");
    }





}
