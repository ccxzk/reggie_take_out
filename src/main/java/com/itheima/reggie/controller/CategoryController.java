package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.CategoryDTO;
import com.itheima.reggie.dto.CategoryPageQueryDTO;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("category")
@Slf4j
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param categoryDTO
     * @return
     */
    @PostMapping
    public R save(@RequestBody CategoryDTO categoryDTO){
        log.info("新增分类");

        //创建分类对象，并拷贝数据
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public R page(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分页查询分类");

        //构造分页构造器
        Page page = new Page(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());

        //构造条件构造器
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        //添加排序条件
        lambdaQueryWrapper.orderByAsc(Category::getSort);

        //分页查询
        categoryService.page(page,lambdaQueryWrapper);

        return R.success(page);
    }

    /**
     * 修改分类
     * @param categoryDTO
     * @return
     */
    @PutMapping
    public R update(@RequestBody CategoryDTO categoryDTO){
        log.info("修改分类");

        //创建分类对象，并拷贝数据
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        categoryService.updateById(category);
        return R.success("修改分类成功");
    }

    /**
     * 删除分类
     * @param ids
     * @return
     */
    @DeleteMapping
    public R delete(@RequestParam Long ids){
        log.info("删除分类");

        categoryService.removeById(ids);
        return R.success("删除分类成功");
    }

    /**
     * 根据id查询分类
     * @param type
     * @return
     */
    @GetMapping("/list")
    public R list(@RequestParam int type){
        //构造条件构造器
        QueryWrapper<Category> queryWrapper = Wrappers.<Category>query()
                .eq("type",type);

        //根据分类类型查询
        queryWrapper.eq("type",type);

        //查询
        List<Category> categories = categoryService.list(queryWrapper);

        return R.success(categories);
    }


}
