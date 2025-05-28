package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.constant.MessageConstant;
import com.itheima.reggie.constant.OperationType;
import com.itheima.reggie.dto.EmployeeDTO;
import com.itheima.reggie.dto.EmployeePageQueryDTO;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request,@RequestBody Employee employee){
        //将前端前来的用户密码明文进行md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //查询用户是否存在
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //用户不存在情况
        if (emp == null){
           return R.error("用户不存在");
        }

        //用户存在情况
        if(emp.getPassword().equals(password)){
            //密码正确
            request.getSession().setAttribute("employee",emp.getId()); //将用户id存入session，以便退出登录
            return R.success(emp);
        }else{
            //密码错误
            return R.error("密码错误");
        }

    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param request
     * @param employeeDTO
     * @return
     */
    @PostMapping
    public R save(HttpServletRequest request,@RequestBody EmployeeDTO employeeDTO){
        //创建员工对象，并拷贝前端传来的数据
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);

        //设置初始密码123456，并进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        //默认启用
        employee.setStatus(MessageConstant.ENABLED);

        //新增员工
        employeeService.save(employee);

        return R.success("新增员工成功");
    }

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public R page(EmployeePageQueryDTO employeePageQueryDTO){
        //构造分页构造器
        Page page = new Page(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        //构造条件构造器
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        //根据名称模糊查询
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(employeePageQueryDTO.getName()),Employee::getName,employeePageQueryDTO.getName());

        //构造排序条件
        lambdaQueryWrapper.orderByAsc(Employee::getCreateTime);

        //分页查询
        employeeService.page(page,lambdaQueryWrapper);

        return R.success(page);
    }

    /**
     * 根据id查询员工（前端回显）
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        //根据id查询员工
        Employee employee = employeeService.getById(id);

        //将结果返回
        if(employee != null){
            return R.success(employee);
        }else{
            return R.error("员工信息查询失败");
        }
    }

    /**
     * 修改员工信息
     * @param request
     * @param employeeDTO
     * @return
     */
    @PutMapping
    public R update(HttpServletRequest request,@RequestBody EmployeeDTO employeeDTO){
        //创建员工对象，并拷贝前端传来的数据
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);

        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }
}
