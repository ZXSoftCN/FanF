package com.zxsoft.fanfanfamily.mort.service.impl;

import com.zxsoft.fanfanfamily.base.domain.Organization;
import com.zxsoft.fanfanfamily.base.domain.mort.Employee;
import com.zxsoft.fanfanfamily.base.domain.mort.Employee;
import com.zxsoft.fanfanfamily.base.domain.vo.AvatorLoadFactor;
import com.zxsoft.fanfanfamily.base.repository.OrganizationDao;
import com.zxsoft.fanfanfamily.base.service.impl.BaseServiceImpl;
import com.zxsoft.fanfanfamily.mort.repository.EmployeeDao;
import com.zxsoft.fanfanfamily.mort.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class EmployeeServiceImpl extends BaseServiceImpl<Employee> implements EmployeeService {
    private final String resPathName = "employee";
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private OrganizationDao organizationDao;


    //<editor-fold desc="私有方法">
    private void modifyIcon(Employee employee, String path) {
        try {
            String strOld = employee.getIconUrl();
            if (strOld != null && !strOld.isEmpty()) {
                if (employee.getIconUrl().startsWith("file:/")) {
                    strOld = employee.getIconUrl().replaceFirst("file:/", "");
                }
                Path pathOld = Paths.get(strOld);
                Files.deleteIfExists(pathOld);
            }
            employee.setIconUrl(path);
            employeeDao.save(employee);
        }catch (IOException ex){
            logger.error(String.format("%s Failed to store file:%s.%s",
                    this.getClass().getName(),ex.getMessage(), System.lineSeparator()));//System.lineSeparator()换行符
        }
    }

    //</editor-fold>

    @Override
    protected void initPath() {
        super.initPath();
        rootUploadPath = super.getPath().resolve(resPathName);
        avatarUploadPath = super.getPath().resolve(super.avatar);
    }

    @Override
    public JpaRepository<Employee, String> getBaseDao() {
        return employeeDao;
    }

    @Override
    public String uploadAvatarExtend(Employee employee, String fileName, String postfix, byte[] bytes) {
        String itemNew = uploadAvatar(fileName,postfix,bytes);
        if (itemNew == null) {
            return null;
        }
        //将Path路径保存至Region的IconUrl属性
        modifyIcon(employee,itemNew);

        return itemNew;
    }

    @Override
    public Path loadAvatar(Employee employee, AvatorLoadFactor factor) {
        String strUrl = employee.getIconUrl();
        return loadAvatarInner(strUrl,factor);
    }

    @Override
    public String uploadAvatarExtend(Employee employee, MultipartFile file) {
        String itemNew = uploadAvatar(file);
        if (itemNew == null) {
            return null;
        }
        //将Path路径保存至Region的IconUrl属性
        modifyIcon(employee,itemNew);

        return itemNew;
    }

    @Override
    public Employee add(Employee employee) {
        if (employee == null) {
            return null;
        }
        if (employee.getAliasName() == null || employee.getAliasName().isEmpty()) {
            employee.setAliasName(employee.getName());
        }
        if (employee.getOutterOrganizationId() != null && !employee.getOutterOrganizationId().isEmpty()) {
            Optional<Organization> itemOrg = organizationDao.findById(employee.getOutterOrganizationId());
            if (itemOrg.isPresent()) {
                employee.setOrganization(itemOrg.get());
            }
        }

        return super.add(employee);
    }

    @Override
    public Employee modify(Employee employee) {

        if (employee == null) {
            return null;
        }
        Optional<Employee> itemExists = employeeDao.findById(employee.getId());
        //若不存在，则转入新增
        if (!itemExists.isPresent()) {
            add(employee);
        }
        if (employee.getAliasName() == null || employee.getAliasName().isEmpty()) {
            employee.setAliasName(employee.getName());
        }
        if (employee.getOutterOrganizationId() != null && !employee.getOutterOrganizationId().isEmpty()) {
            Optional<Organization> itemOrg = organizationDao.findById(employee.getOutterOrganizationId());
            if (itemOrg.isPresent()) {
                employee.setOrganization(itemOrg.get());
            }
        }

        return super.modify(employee);
    }
}
