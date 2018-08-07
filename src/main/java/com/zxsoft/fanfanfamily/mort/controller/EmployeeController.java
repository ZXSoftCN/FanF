package com.zxsoft.fanfanfamily.mort.controller;

import com.zxsoft.fanfanfamily.base.controller.BaseRestControllerImpl;
import com.zxsoft.fanfanfamily.base.domain.mort.Employee;
import com.zxsoft.fanfanfamily.base.service.BaseService;
import com.zxsoft.fanfanfamily.mort.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/employee")
public class EmployeeController extends BaseRestControllerImpl<Employee> {

    @Autowired
    private EmployeeService employeeService;

    @Override
    public BaseService getBaseService() {
        return employeeService;
    }

    @Override
    public Class<Employee> getEntityType() {
        return Employee.class;
    }


}
