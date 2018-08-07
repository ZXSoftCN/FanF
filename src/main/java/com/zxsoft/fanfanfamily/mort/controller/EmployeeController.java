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

    @PostMapping(value = "/employee/add")
    public Employee addEmployee(@RequestBody Employee Employee){
        return employeeService.save(Employee);
    }

    @GetMapping("/employee/get/{employeeId}")
    public ResponseEntity<Employee> queryEmployee(@PathVariable(required = false) Employee employeeId) {

        ResponseEntity<Employee> itemR;
        Optional<Employee> rltEmployee = employeeService.getById(employeeId.getId());
//        Optional<Employee> rltEmployee = EmployeeService.getById(EmployeeId);
        if (rltEmployee.isPresent()) {
            itemR = ResponseEntity.ok(rltEmployee.get());
        }else{
            itemR = ResponseEntity.status(200).body(null);
        }
        return itemR;
    }

    @GetMapping("/Employee/get")
    public ResponseEntity<Page<Employee>> queryEmployeeList(@RequestParam(value = "page",required = false,defaultValue = "0") int page,
                                                        @RequestParam(value = "size",required = false,defaultValue = "5") int size,
                                                        @RequestParam(value = "sort",required = false,defaultValue = "code") String sort) {
        ResponseEntity<Page<Employee>> itemR;
        Sort itemSort = Sort.by(Sort.Direction.ASC,sort);
        Pageable pageable = PageRequest.of(page,size, itemSort);
        Page<Employee> pcollEmployee = employeeService.findAll(pageable);
        if (pcollEmployee.getSize() > 0) {
            itemR = ResponseEntity.ok(pcollEmployee);
        }else{
            itemR = ResponseEntity.status(200).body(null);
        }
        return itemR;
    }

    @PostMapping(value = "/employee/updateAvatar",consumes = "multipart/form-data")
    public ResponseEntity<String> uploadAvatar(@RequestParam(value = "employeeId",required = true) Employee employee,
                                                 @RequestParam(value = "fileName",required = false,defaultValue = "Empty") String fileName,
                                                 @RequestParam(value = "postfix",required = true) String postfix,
                                                 @RequestBody(required = true) byte[] bytes){
        ResponseEntity<String> itemR ;
        Path item =  employeeService.uploadAvatarExtend(employee,fileName,postfix,bytes);

        if (item != null) {
            itemR = ResponseEntity.ok(item.toString());
        }else{
            itemR = ResponseEntity.status(200).body("");
        }
        return itemR;
    }

    @PostMapping(value = "/employee/loadAvatar")
    public ResponseEntity<String> loadAvatar(@RequestParam(value = "employeeId",required = true) Employee employee,
                                                @RequestParam(value = "width",required = false,defaultValue = "0") int width,
                                                @RequestParam(value = "height",required = false,defaultValue = "0") int height,
                                                @RequestParam(value = "scaling",required = false,defaultValue = "1") double scaling){
        ResponseEntity<String> itemR ;
        Path item;
        if (width == 0 && height == 0) {
            item = employeeService.loadAvatar(employee);
        }else{
            item = employeeService.loadAvatar(employee,width,height,scaling);
        }

        if (item != null) {
            itemR = ResponseEntity.ok(item.toString());
        }else{
            itemR = ResponseEntity.status(200).body("");
        }
        return itemR;
    }

}
