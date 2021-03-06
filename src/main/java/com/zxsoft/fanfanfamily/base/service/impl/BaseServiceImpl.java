package com.zxsoft.fanfanfamily.base.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zxsoft.fanfanfamily.base.domain.EntityIncrease;
import com.zxsoft.fanfanfamily.base.domain.Menu;
import com.zxsoft.fanfanfamily.base.domain.vo.AvatorLoadFactor;
import com.zxsoft.fanfanfamily.base.repository.EntityIncreaseDao;
import com.zxsoft.fanfanfamily.base.service.BaseService;
import com.zxsoft.fanfanfamily.base.service.StorageException;
import com.zxsoft.fanfanfamily.common.EntityManagerUtil;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public abstract class BaseServiceImpl<T> implements BaseService<T> {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    private Path origUploadPath;
    protected Path rootUploadPath;
    protected Path avatarUploadPath;
    protected final String avatar = "avatar";
    //初始化流水号
    private final int INIT_NUM = 0;

    @Autowired
    protected AppPropertiesConfig appPropertiesConfig;
    @Autowired
    protected WebApplicationContext applicationContext;
    @Autowired
    private EntityIncreaseDao entityIncreaseDao;
    @Autowired
    protected EntityManagerUtil entityManagerUtil;

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
    public String getEntityName() {
        return null;
    }

    @Override
    public AtomicInteger getSortNoMax() {

        return null;
    }

    @Override
    public void initSortNoMax() {
        try{
            if (StringUtils.isBlank(this.getEntityName()) && getSortNoMax() == null) {
                return ;
            }
            Optional<Integer> sortNoMax = entityIncreaseDao.getSortNoMax(this.getEntityName());
//            if(!sortNoMax.isPresent() || sortNoMax.get()<INIT_NUM){
//                sortNoMax = Optional.of(INIT_NUM);
//
//            }
            if(logger.isDebugEnabled()){
                logger.debug(String.format("初始化%s序号最大值为：%d",getEntityName(),sortNoMax.orElse(INIT_NUM)));
            }
            getSortNoMax().set(sortNoMax.orElse(INIT_NUM));
        }catch(Exception e){
            logger.error(String.format("初始化获取%s序号最大值异常:%s",getEntityName(),e.getMessage()));
        }
    }

    /**
     * @Author  javaloveiphone
     * @Description :获取最新分组编号
     * @return
     * int
     * 注：此方法并没有使用synchronized进行同步，因为共享的编号自增操作是原子操作，线程安全的
     */
    @Override
    public int getNewSortNo() {
        //线程安全的原子操作，所以此方法无需同步
        int newSortNo = getSortNoMax().incrementAndGet();
        return newSortNo;
    }

    @Override
    public AtomicInteger getCodeNumMax() {
        return null;
    }

    @Override
    public void initCodeNumMax() {
        try{
            if (StringUtils.isBlank(this.getEntityName()) && getCodeNumMax() == null) {
                return ;
            }
            Optional<Integer> codeNumMax = entityIncreaseDao.getCodeNumMax(this.getEntityName());
            if(logger.isDebugEnabled()){
                logger.debug(String.format("初始化%s编号最大值为：%d",getEntityName(),codeNumMax.orElse(INIT_NUM)));
            }
            getCodeNumMax().set(codeNumMax.orElse(INIT_NUM));
        }catch(Exception e){
            logger.error(String.format("初始化获取%s编号最大值异常:%s",getEntityName(),e.getMessage()));
        }
    }

    @Override
    public String getNewCode() {
        String newCode = null;
        Optional<EntityIncrease> itemIncrease = entityIncreaseDao.findFirstByEntityNameIgnoreCase(getEntityName());
        if (itemIncrease.isPresent()) {
            String datePart = null;
            if (!StringUtils.isBlank(itemIncrease.get().getDateFormat())) {
                DateTimeFormatter df = DateTimeFormatter.ofPattern(itemIncrease.get().getDateFormat());
                datePart = DateTime.now().toString(itemIncrease.get().getDateFormat());
            }
            String prefixPart = itemIncrease.get().getPrefix();
            int codeNumLength = itemIncrease.get().getCodeNumLength();
            String separate = itemIncrease.get().getSeparate();
            String maxNumPlus = "1" + StringUtils.repeat("0",codeNumLength);
            Long maxNumLong = Long.decode(maxNumPlus) + getCodeNumMax().incrementAndGet();
            String numPart = maxNumLong.toString().substring(1);//去除首位字符1
            List<String> lstJoin = new ArrayList<>();
            if (!StringUtils.isEmpty(prefixPart)) {
                lstJoin.add(prefixPart);
            }
            if (!StringUtils.isEmpty(datePart)) {
                lstJoin.add(datePart);
            }
            lstJoin.add(numPart);
            newCode = StringUtils.join(lstJoin.iterator(),separate);
        }
        return newCode;
    }

    public Path getDefaultAvatar() {
        String strClasses = applicationContext.getClassLoader().getResource("").getPath().substring(1);
        return Paths.get(String.format("%s/%s",strClasses,appPropertiesConfig.getDefaultAvatar()));
    }

    protected String getClassesPath(){
        return applicationContext.getClassLoader().getResource("").getPath().substring(1);
    }

    @Override
    public Path getOriginPath(){return origUploadPath;}

    //构造函数执行时applicalitionProperties还未装配完成。
    //装配动作是在构造完成之后进行
    protected void initPath() {

        this.origUploadPath = Paths.get(String.format("%s/%s",getClassesPath(),appPropertiesConfig.getUploadPath()));
        this.rootUploadPath = Paths.get(String.format("%s/%s",getClassesPath(),appPropertiesConfig.getUploadPath()));
        this.avatarUploadPath = Paths.get(String.format("%s/%s",getClassesPath(),appPropertiesConfig.getUploadPath()));
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
    public String getBannedFile() {
        return "";
    }

    @Override
    public abstract JpaRepository<T,String> getBaseDao();

    @Override
    public Optional<T> getById(String id) {
        return getBaseDao().findById(id);
    }

    /**
     * 默认使用id。下行实体根据自身关键字属性进行实现，如按code或name查询。
     * @param key
     * @return
     */
    @Override
    public Optional<T> getByKey(String key) {
        return getBaseDao().findById(key);
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
    @Transactional
    public List<T> saveBatch(List<T> collT) {

        return getBaseDao().saveAll(collT);
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
    public String uploadAvatar( MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            String postfix =  StringUtils.substringAfter(file.getOriginalFilename(),".");

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
            Files.copy(file.getInputStream(), itemNew,StandardCopyOption.REPLACE_EXISTING);
            String strOppoPath = StringUtils.replace(itemNew.toString().replace("\\","/"),getClassesPath(),"");
            return strOppoPath;
//            return itemNew;

        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public String uploadAvatar(String fileName, String postfix, byte[] bytes) {

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

            return itemNew.toString();
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
    public String uploadAvatar(String fileName, byte[] bytes) {
        String pos = StringUtils.substringAfterLast(fileName,".");
        return uploadAvatar(fileName,pos,bytes);
    }

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
    public Path loadAvatar(T t) {
        return loadAvatar(t,null);
    }

    @Override
    public Path loadAvatar(T t, int width, int height, double scaling){
        AvatorLoadFactor factor = new AvatorLoadFactor();
        factor.setWidth(width);
        factor.setHeight(height);
        factor.setScaling(scaling);
        return loadAvatar(t,factor);
    }

    @Override
    public abstract Path loadAvatar(T t, AvatorLoadFactor factor) ;

    private Path loadAvatarInner(String url) {

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

    protected Path loadAvatarInner(String url,AvatorLoadFactor factor) {
        if (factor == null) {
            return loadAvatarInner(url);
        }
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
                            StringUtils.substringBefore(fileName,"."),factor.getWidth(),factor.getHeight(),
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
                                .size(factor.getWidth(),factor.getHeight())
                                .outputQuality(factor.getScaling())
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

    /**
     * 删除实体的附件。
     * @param url
     */
    @Override
    public void deleteInnertFile(String url) {
        if (url != null && !url.isEmpty() && !isBannedUrl(url)) {
            if (url.startsWith("file:/")) {
                url = url.replaceFirst("file:/", "");
            }
            Path pathOld = Paths.get(StringUtils.join(getClassesPath(),url));
            try{
                Files.deleteIfExists(pathOld);
            }catch (IOException ex){
                logger.error(String.format("%s 删除头像图标【%s】失败:%s.%s",
                        this.getClass().getName(),pathOld.toAbsolutePath(),ex.getMessage(),System.lineSeparator()));//System.lineSeparator()换行符
            }
        }
    }

    protected boolean isBannedUrl(String url) {
        return appPropertiesConfig.getBannedFiles().contains(url);
    }
}
