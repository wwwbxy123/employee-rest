package com.example.employeerestdemo.controller;

/*
Spring MVC --> Controller --> Model // annotation Autowire
Spring (DI) Ioc Container
*/

// 题目
//        POST /employee
//        GET /employee/{empId}
//        PUT /employee/{empId}
//
//        Cache.put(key, value)
//        Object Cache.get(key); -- if it doesnt exist, returns null
//
//        Employee EmployeeDao.getEmployeeById(empId);
//        Integer EmployeeDao.save(Employee);


// Tomcare 是java的一个server，python, c++ 都有自己的server

import com.example.employeerestdemo.model.Cache;
import com.example.employeerestdemo.model.Employee;
import com.example.employeerestdemo.model.EmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;



//@Controller   //把我们写的这个EmployeeRestController 交给spring管理
@RestController
//@RequestMapping("api/v1")  //用公共变量去优化重复的变量
public class EmployeeRestController {

    @Autowired
    private Cache cache;

    @Autowired
    private EmployeeDao employeeDao;

    /* POST */
    // HTTP request body --> java Bean Spring MVC 帮我们做了 desirilized 的过程  --> convert to HTTP Response Body in Json
    //@RequestMapping(value = "api/v1/employees", method = RequestMethod.POST)   //rest api 的精华
    @PostMapping("employees")  // 和上面注视掉的那一行效果是一样的，
    //@ResponseBody //返回body
    public Employee createEmployee(@Valid @RequestBody Employee input){    // json convert to java obj, 是spring MVC 的filter帮我们完成额
        //优化，加了@Valid spring mvc会帮我们做input valid

        Long genId = employeeDao.save(input);
        Employee result = employeeDao.getEmployeeById(genId);
        cache.put(genId, result);
        return result;

    }

    /* GET */
    @GetMapping("employees/{id}")

    public Employee getEmployeeById(@PathVariable Long id) {   // 当java method 参数的名字如果和variable的名字一样的话，就可以不加 @PathVariable（value = "id"）
        //return employeeDao.getEmployeeById(id);
        Object cachedEmp = (Employee) cache.get(id);  // cacheEmp是个general的type，不一定是Employee
        if (cachedEmp != null) {
            return (Employee) cachedEmp;   //只有cachedEmp不为空的情况下才需要做cast，于是可以先 Object cachedEmp
        }
        try {
            Employee res = employeeDao.getEmployeeById(id);
            /*
            if(res == null){
                throw new IllegalArgumentException("id doesn't exist");  // 这个exception throw 不出来，在database里的，于是用 try catch block 吧
            }
            */
            if(res != null){
                cache.put(id, res);
            }
            return res;
        } catch (Exception ex){
            throw ex;
        }
    }

    /* PUT */
    @PutMapping("employees/{empId}")

    public Employee putEmployeeById(@PathVariable Long empId, @Valid @RequestBody Employee newInput){  // 1
        Employee existEmp = employeeDao.getEmployeeById(empId);
        if (existEmp == null){
            throw new IllegalArgumentException("id does not exist");
        }
        existEmp.setFirstName(newInput.getFirstName());
        existEmp.setLastName(newInput.getLastName());
        existEmp.setAge(newInput.getAge());  // 3 setter 和getter的运用

        employeeDao.save(existEmp);  // 4 要把新的东西改回去， jpa决定的，Dao会帮我们更新,把obj存到db里去
        cache.put(empId, existEmp);  // 5 如果不做这个的话，那么get到的永远是旧的那个employ
        return existEmp;
    }

}

