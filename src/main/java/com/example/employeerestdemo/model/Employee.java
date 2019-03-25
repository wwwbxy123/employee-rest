package com.example.employeerestdemo.model;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by guang on 3:52 PM 10/6/18.
 */
// POJO = Plain old java Obj, POJO 也是一类java class
// JPA = Java Persistent API  , persistent 是DB 来做的，JPA就是帮 java 到 sql做的转换，用来DB打交道用的， spring 把JPA整合进去了

@Entity
@Table(name = "employees")

//这个 class 是一个bean， bean是某一种java class，
    // spring MVC 的 DispatcherServerlet 也是一个bean，
public class Employee {   //运行的时候 create table employees 是通过 @Entity @Table(name = "employees") 帮我们实现的
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // specify key是怎么生成的
    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull  //通过 java annotation 的方式 JPA这个框架让我们对数据库本身的调整，
    @Min(1)
    @Max(100)
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
