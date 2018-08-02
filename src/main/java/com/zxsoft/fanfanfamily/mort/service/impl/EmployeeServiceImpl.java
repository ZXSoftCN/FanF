package com.zxsoft.fanfanfamily.mort.service.impl;

import com.zxsoft.fanfanfamily.base.domain.Organization;
import com.zxsoft.fanfanfamily.base.domain.mort.Employee;
import com.zxsoft.fanfanfamily.base.domain.mort.Employee;
import com.zxsoft.fanfanfamily.base.service.impl.BaseServiceImpl;
import com.zxsoft.fanfanfamily.mort.repository.EmployeeDao;
import com.zxsoft.fanfanfamily.mort.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class EmployeeServiceImpl extends BaseServiceImpl<Employee> implements EmployeeService {
    private final String resPathName = "employee";
    @Autowired
    private EmployeeDao employeeDao;
    
    //<editor-fold desc="私有方法">
    private void modifyIcon(Employee employee, Path path) {
        try {
            String strOld = employee.getIconUrl();
            if (employee.getIconUrl().startsWith("file:/")) {
                strOld = employee.getIconUrl().replaceFirst("file:/", "");
            }
            Path pathOld = Paths.get(strOld);
            Files.deleteIfExists(pathOld);

            employee.setIconUrl(path.toString());
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
    public Path uploadAvatarExtend(Employee employee, String fileName, String postfix, byte[] bytes) {
        return null;
    }

    @Override
    public Path loadAvatar(Employee employee) {
        String strUrl = employee.getIconUrl();
        return loadAvatarInner(strUrl);
    }

    @Override
    public Path loadAvatar(Employee employee, int width, int height, double scaling) {
        String strUrl = employee.getIconUrl();
        return loadAvatarInner(strUrl,width,height,scaling);
    }

    @Override
    public Path uploadAvatarExtend(Employee employee, MultipartFile file) {
        Path itemNew = uploadAvatar(file);
        if (itemNew == null) {
            return null;
        }
        //将Path路径保存至Region的IconUrl属性
        modifyIcon(employee,itemNew);

        return itemNew;
    }
}