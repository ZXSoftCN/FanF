package com.zxsoft.fanfanfamily.base.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zxsoft.fanfanfamily.base.service.BaseService;
import com.zxsoft.fanfanfamily.base.service.StorageException;
import com.zxsoft.fanfanfamily.config.AppPropertiesConfig;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public abstract class BaseServiceImpl<T> implements BaseService<T> {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    private Path origUploadPath;
    protected Path rootUploadPath;
    protected Path avatarUploadPath;
    protected final String avatar = "avatar";

    @Autowired
    protected AppPropertiesConfig appPropertiesConfig;
    @Autowired
    protected WebApplicationContext applicationContext;


    //    @Autowired
//    public BaseServiceImpl(ApplicalitionProperties applicalitionProperties){
//        this.rootUploadPath = Paths.get(applicalitionProperties.getUploadPath());
//    }
//
//    public BaseServiceImpl(){
////        initPath();
//
//    }
    public Path getDefaultAvatar() {
        return Paths.get(appPropertiesConfig.getDefaultAvatar());
    }

    @Override
    public Path getOriginPath(){return origUploadPath;}

    //构造函数执行时applicalitionProperties还未装配完成。
    //装配动作是在构造完成之后进行
    protected void initPath() {
        this.origUploadPath = Paths.get(appPropertiesConfig.getUploadPath());
        this.rootUploadPath = Paths.get(appPropertiesConfig.getUploadPath());
        this.avatarUploadPath = Paths.get(appPropertiesConfig.getUploadPath());
    }

    @Override
    public Path getPath() {
        if (this.rootUploadPath == null) {
            initPath();//此处注意：会出现子类通过super.getPath()调用时，initPath()再执行是子类的方法，而造成循环。
        }
        try {
            if (!Files.exists(this.rootUploadPath)) {
                Files.createDirectories(this.rootUploadPath);
            }
        } catch (IOException ex) {
            logger.error(String.format("创建资源文件夹%s失败，异常：%s",
                    this.rootUploadPath.toString(),ex.getMessage()));
        }

        return this.rootUploadPath ;
    }

    @Override
    public Path getAvatarPath(){
        if (this.avatarUploadPath == null) {
            initPath();//此处注意：会出现子类通过super.getPath()调用时，initPath()再执行是子类的方法，而造成循环。
        }
        try {
            if (!Files.exists(this.avatarUploadPath)) {
                Files.createDirectories(this.avatarUploadPath);
            }
        } catch (IOException ex) {
            logger.error(String.format("创建图标头像文件夹%s失败，异常：%s",
                    this.avatarUploadPath.toString(),ex.getMessage()));
        }
        return this.avatarUploadPath ;
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
    @Transactional
    public T add(T t) {
        return getBaseDao().save(t);
    }

    @Override
    @Transactional
    public T modify(T t) {
        return getBaseDao().save(t);
    }

    @Override
    public Boolean delete(String id) {
        Boolean rlt = false;
        Optional<T> t = getBaseDao().findById(id);
        if (t.isPresent()) {
            getBaseDao().delete(t.get());
            rlt  = true;
        }
        return rlt;
    }

    @Override
    public Boolean deleteBatch(List<String> ids) {
        Boolean rlt = false;
        List<T> lst = getBaseDao().findAllById(ids);
        if (!lst.isEmpty()) {
            getBaseDao().deleteAll(lst);
            rlt = true;
        }
        return rlt;
    }

    @Override
    public Path uploadAvatar( MultipartFile file) {
        return null;
    }

    @Override
    public Path uploadAvatar(String fileName, String postfix, byte[] bytes) {

        try{
//            File destFile = new File(destPath.toString());
//            OutputStream stream = new FileOutputStream(destFile,false);
//            org.apache.commons.io.IOUtils.write(bytes,stream);
            //按日期+随机数生成新文件名
            String currDate = DateTime.now().toString(appPropertiesConfig.getAppShortDateFormat());
            String newFileName = String.format("%s%d.%s",currDate,
                    appPropertiesConfig.getRandomInt(),postfix);
            Path itemNew = getAvatarPath().resolve(newFileName);
            while (Files.exists(itemNew)) {
                newFileName = String.format("%s%d.%s",currDate,
                        appPropertiesConfig.getRandomInt(),postfix);
                itemNew = getAvatarPath().resolve(newFileName);
            }
            OpenOption[] options =
                    new OpenOption[] {StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING};
            Files.createFile(itemNew);
            Files.write(itemNew,bytes,options);

            return itemNew;
        }catch (IOException ex){
            logger.error(String.format("%s Failed to store file:%s.%s",
                    this.getClass().getName(),ex.getMessage(), System.lineSeparator()));//System.lineSeparator()换行符
        }
        return null;
    }

    /*
    *解析文件名获取后缀名
     */
    @Override
    public Path uploadAvatar(String fileName, byte[] bytes) {
        String pos = StringUtils.substringAfterLast(fileName,".");
        return uploadAvatar(fileName,pos,bytes);
    }

    @Override
    public abstract Path uploadAvatarExtend(T t, String fileName, String postfix, byte[] bytes) ;

    @Override
    public Resource downloadResource(String url) {
        if (!StringUtils.isEmpty(url) ) {
            Path path = Paths.get(url);
            if (Files.exists(path)) {
                Resource resource = applicationContext.getResource(url);
                return resource;
            } else {
                logger.warn(String.format("所要下载的文件%s不存在",url));
            }
        }
        return null;
    }

    @Override
    public abstract Path loadAvatar(T t);

    @Override
    public abstract Path loadAvatar(T t, int width, int height, double scaling) ;

    protected Path loadAvatarInner(String url) {

        if (!StringUtils.isEmpty(url) ) {
            Path path = Paths.get(url);
            if (Files.exists(path)) {
                return path;
            } else {
                logger.warn(String.format("加载%s【%s】的图标被删除，使用默认头像。",getClass().getName(),url));
            }
        }
//        logger.debug(String.format("加载%s，使用默认头像。",getClass().getName()));
        return getDefaultAvatar();
    }

    protected Path loadAvatarInner(String url,int width, int height, double scaling) {

        if (!StringUtils.isEmpty(url) ) {
            Path path = Paths.get(url);
            if (Files.exists(path)) {
                String fileName = path.getFileName().toString();

                String currentDirectory = String.format("%s\\%s",
                        path.getParent().toString(),StringUtils.substringBefore(fileName,"."));//toAbsolutePath().

                Path upperPath = Paths.get(currentDirectory);
                try {
                    if (!Files.exists(upperPath)) {
                        upperPath = Files.createDirectories(upperPath);
                    }
                    String newFileName = String.format("%s_%d_%d.%s",
                            StringUtils.substringBefore(fileName,"."),width,height,
                            StringUtils.substringAfterLast(fileName,"."));
                    Path newItem = upperPath.resolve(newFileName);
                    File newFile;
                    if (Files.exists(newItem) && newItem.toFile().length() > 0) {
                        newFile = newItem.toFile();
                    }else{
                        if (Files.exists(newItem)) {
                            Files.deleteIfExists(newItem);//异常：0字节文件
                        }
                        newFile = Files.createFile(newItem).toFile();
                        Thumbnails.of(path.toUri().toURL())
                                .size(width, height)
                                .outputQuality(scaling)
                                .useOriginalFormat()
                                .toFile(newFile);
                    }
                    return newFile.toPath();
                }catch (MalformedURLException ex) {
                    logger.error(String.format("读取文件%s失败，异常：%s" ,fileName, ex.getMessage()));
                } catch (IOException ex) {
                    logger.error(String.format("创建%s文件夹失败，异常：%s",upperPath.toString(),ex.getMessage()));
                }
            } else {
                logger.warn(String.format("加载%s【%s】的图标被删除，使用默认头像。",getClass().getName(),url));
            }
        }
//        logger.debug(String.format("加载%s，使用默认头像。",getClass().getName()));
        return getDefaultAvatar();
    }
}
