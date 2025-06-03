package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.constant.MessageConstant;
import com.itheima.reggie.dto.SetmealDTO;
import com.itheima.reggie.dto.SetmealPageQueryDTO;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import com.itheima.reggie.vo.SetmealVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Resource
    private SetmealService setmealService;

    @Resource
    private SetmealDishService setmealDishService;

    @Resource
    private CategoryService categoryService;

    /**
     *  套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
     public R<Page> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        //构造分页构造器
        Page<Setmeal> pageInfo = new Page(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize()); // 套餐合集
        Page<SetmealVO> page = new Page(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize()); // 包含分类名称的合集

        //构造条件构造器
        LambdaQueryWrapper<Setmeal>  lambdaQueryWrapper = Wrappers.<Setmeal>lambdaQuery()
                .like(StringUtils.isNotEmpty(setmealPageQueryDTO.getName()), Setmeal::getName,setmealPageQueryDTO.getName()) //  添加过滤条件
                .orderByAsc(Setmeal::getUpdateTime); // 添加排序条件

        //分页查询
        setmealService.page(pageInfo,lambdaQueryWrapper);
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealVO> setmealVOList = records.stream().map(item -> {
            SetmealVO setmealVO = new SetmealVO();

            Category category = categoryService.getById(item.getCategoryId());
            if (category != null) {
                setmealVO.setCategoryName(category.getName());
            }

            BeanUtils.copyProperties(item,setmealVO);

            return  setmealVO;

        }).collect(Collectors.toList());

        page.setRecords(setmealVOList);

        //将结果返回
        return R.success(page);
    }

    /**
     *  新增套餐
     * @param setmealDTO
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDTO setmealDTO) {

        setmealService.saveWithDish(setmealDTO);
        return R.success("新增套餐成功");
    }

    /**
     * 根据id查询套餐信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDTO> getById(@PathVariable Long id) {
        Setmeal setmeal = setmealService.getById(id);

        LambdaQueryWrapper <SetmealDish> queryWrapper = Wrappers.<SetmealDish>lambdaQuery()
                .eq(SetmealDish::getSetmealId,id);

        SetmealDTO setmealDTO = SetmealDTO.builder()
                .setmealDishes(setmealDishService.list(queryWrapper))
                .build();
        BeanUtils.copyProperties(setmeal,setmealDTO);

        return R.success(setmealDTO);
    }

    /**
     *  修改套餐
     * @param setmealDTO
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDTO setmealDTO) {
        setmealService.updateWithDish(setmealDTO);

        return R.success("修改套餐成功");
    }

    /**
     *  批量起售停售
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> startOrStop(@PathVariable Integer status, @RequestParam List<Long> ids) {
        setmealService.updateStatusByIds(status, ids);

        return  R.success("状态修改成功");
    }

    /**
     *  批量删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        setmealService.removeByIds(ids);

        return R.success("删除成功");
    }

    /**
     *   根据id、status查询套餐
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list( Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> queryWrapper = Wrappers.<Setmeal>lambdaQuery()
                .eq(Setmeal::getCategoryId,setmeal.getCategoryId())
                .eq(Setmeal::getStatus,1)
                .orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }

}
