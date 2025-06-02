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
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishFlavorService;
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
    private DishFlavorService dishFlavorService;

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
        lambdaQueryWrapper.orderByDesc(Dish::getCreateTime); // 添加排序条件

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

        dishService.saveWithFlavor(dishDTO);

        return R.success("新增菜品成功");
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R getById(@PathVariable Long id){
        Dish dish = dishService.getById(id);

        LambdaQueryWrapper<DishFlavor> queryWrapper = Wrappers.<DishFlavor>lambdaQuery()
                .eq(DishFlavor::getDishId,id);


        DishDTO dishDTO = new DishDTO();
        dishDTO.setFlavors(dishFlavorService.list(queryWrapper));
        BeanUtils.copyProperties(dish,dishDTO);

        return R.success(dishDTO);
    }

    /**
     * 修改菜品
     * @param dishDTO
     * @return
     */
    @PutMapping
    public R update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品信息：{}", dishDTO);

        dishService.updateWithFlavor(dishDTO);

        return R.success("菜品信息修改成功");
    }

    /**
     * 批量删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public R delete(@RequestParam List<Long> ids){

        //先删除菜品口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = Wrappers.<DishFlavor>lambdaQuery()
                .in(DishFlavor::getDishId, ids); // 修改为 in 条件

        dishFlavorService.remove(queryWrapper);

        // 再删除菜品表数据
        dishService.removeByIds(ids);
        return R.success("菜品删除成功");
    }

    /**
     * 批量起售停售
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R startOrStop(@PathVariable Integer status,
                         @RequestParam List<Long> ids){
        dishService.updateStatusByIds(status, ids);
        return R.success("状态修改成功");
    }

    /**
     * 获取菜品列表
     * @param categoryId
     * @param name
     * @return
     */
    @GetMapping("/list")
    public R<List<Dish>> list(@RequestParam(required = false) Long categoryId,
                              @RequestParam(required = false) String name
    ){
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = Wrappers.<Dish>lambdaQuery()
                .like(StringUtils.isNotEmpty(name),  Dish::getName, name)
                .eq(categoryId != null, Dish::getCategoryId, categoryId);

        List<Dish> list = dishService.list(lambdaQueryWrapper);
        return R.success(list);
    }







}
