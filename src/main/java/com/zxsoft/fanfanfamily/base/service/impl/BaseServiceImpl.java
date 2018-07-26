package com.zxsoft.fanfanfamily.base.service.impl;

import com.zxsoft.fanfanfamily.base.service.BaseService;
import com.zxsoft.fanfanfamily.base.service.StorageException;
import com.zxsoft.fanfanfamily.config.AppPropertiesConfig;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public abstract class BaseServiceImpl<T> implements BaseService<T> {
    private Path origUploadPath;
    protected Path rootUploadPath;
    @Autowired
    private AppPropertiesConfig appPropertiesConfig;


//    @Autowired
//    public BaseServiceImpl(ApplicalitionProperties applicalitionProperties){
//        this.rootUploadPath = Paths.get(applicalitionProperties.getUploadPath());
//    }
//
//    public BaseServiceImpl(){
////        initPath();
//
//    }

    @Override
    public Path getOriginPath(){return origUploadPath;};

    //构造函数执行时applicalitionProperties还未装配完成。
    //装配动作是在构造完成之后进行
    protected void initPath() {
        this.origUploadPath = Paths.get(appPropertiesConfig.getUploadPath());
        this.rootUploadPath = Paths.get(appPropertiesConfig.getUploadPath());
    }

    @Override
    public Path getPath() {
        if (this.rootUploadPath == null) {
            initPath();//此处注意：会出现子类通过super.getPath()调用时，initPath()再执行是子类的方法，而造成循环。
        }
        return this.rootUploadPath ;
    }

    @Override
    public abstract JpaRepository<T,String> getBaseDao();

    @Override
    public Optional<T> getById(String id) {
        return getBaseDao().findById(id);
    }

    @Override
    public Path storeFile(MultipartFile file) {
        Path filePath;
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            String postfix =  StringUtils.substringAfter(file.getOriginalFilename(),".");

            String newFileName = String.format("%s.%s",appPropertiesConfig.getRandomString(),postfix);
            filePath = this.rootUploadPath.resolve(newFileName);
            Files.copy(file.getInputStream(), filePath,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
        return  filePath;
    }

    @Override
    public T save(T t) {
        CrudRepository<T,String> dao = getBaseDao();
        return dao.save(t);
    }

    @Override
    public List<T> findAll() {
        Iterator<T> coll = getBaseDao().findAll().iterator();
        return  IteratorUtils.toList(coll);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        Page<T> coll = getBaseDao().findAll(pageable);
        return coll;
    }

    @Override
    public void Delete(T t) {
        getBaseDao().delete(t);
    }

    @Override
    public void DeleteAll(List<T> t) {
        if (!t.isEmpty()) {
            getBaseDao().deleteAll(t);
        }

    }
}
