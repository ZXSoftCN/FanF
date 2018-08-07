package com.zxsoft.fanfanfamily.base.service.impl;

import com.zxsoft.fanfanfamily.base.domain.Region;
import com.zxsoft.fanfanfamily.base.domain.RegionResource;
import com.zxsoft.fanfanfamily.base.domain.vo.AvatorLoadFactor;
import com.zxsoft.fanfanfamily.base.service.RegionService;
import com.zxsoft.fanfanfamily.mort.repository.RegionDao;
import com.zxsoft.fanfanfamily.mort.repository.RegionRescourceDao;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;

@Service
public class RegionServiceImpl extends BaseServiceImpl<Region> implements RegionService {

    private final String resPathName = "region";
//    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RegionDao regionDao;
    @Autowired
    private RegionRescourceDao regionRescourceDao;

//    public RegionServiceImpl(){
//    }

    //<editor-fold desc="私有方法">
    private void modifyIcon(Region region, Path path) {
        try {
            String strOld = region.getLogoUrl();
            if (region.getLogoUrl().startsWith("file:/")) {
                strOld = region.getLogoUrl().replaceFirst("file:/", "");
            }
            Path pathOld = Paths.get(strOld);
            Files.deleteIfExists(pathOld);

            region.setLogoUrl(path.toString());
            regionDao.save(region);
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

//    @Override
//    public Path getPath() {
//        if (rootUploadPath == null) {
//            initPath();
//        }
//        return rootUploadPath;
//    }
//    @Override
//    public Path getAvatarPath() {
//        if (avatarUploadPath == null) {
//            initPath();
//        }
//        return avatarUploadPath;
//    }

    @Override
    public JpaRepository<Region, String> getBaseDao() {
        return regionDao;
    }


    @Override
    public RegionResource addResource(MultipartFile file, String regionId) {

        if (regionId.isEmpty()) {
            return null;
        }
        if (!regionDao.findById(regionId).isPresent()) {
            return  null;
        }
        if (file.isEmpty()) {
            return null;
        }
        Region itemRegion = regionDao.findById(regionId).get();
        String currDate = DateTime.now().toString(appPropertiesConfig.getAppShortDateFormat());
        String postfix =  StringUtils.substringAfter(file.getOriginalFilename(),".");
        String newFileName = String.format("%s%s%d.%s",itemRegion.getCode(),currDate,
                appPropertiesConfig.getRandomLessHundred(),postfix);
        try {
//            file.getBytes();
            if (!Files.exists(getPath())) {
                Files.createDirectories(getPath());
            }
            Path destPath = getPath().resolve(newFileName);
            Files.copy(file.getInputStream(), destPath,
                    StandardCopyOption.REPLACE_EXISTING);
            RegionResource itemNew = new RegionResource();
            itemNew.setOriginFileName(file.getOriginalFilename());
            itemNew.setType(postfix);
            itemNew.setRegion(itemRegion);
            itemNew.setResUrl(destPath.toUri().toURL().toString());
            regionRescourceDao.save(itemNew);
//            Resource itemResource = new UrlResource(destPath.toUri());
            return itemNew;
        } catch (IOException e) {
            logger.error(String.format("%s Failed to store file:%s.","RegionResource",file.getOriginalFilename()));
        }
        return null;
    }

    @Override
    public RegionResource addResource(byte[] bytes,String originFileName,String postfix,String regionId) {

        if (regionId.isEmpty()) {
            return null;
        }
        if (!regionDao.findById(regionId).isPresent()) {
            return  null;
        }
        Region itemRegion = regionDao.findById(regionId).get();
        String currDate = DateTime.now().toString(appPropertiesConfig.getAppShortDateFormat());
        String newFileName = String.format("%s%s%d.%s",itemRegion.getCode(),currDate,
                appPropertiesConfig.getRandomLessHundred(),postfix);
        Path destPath = getPath().resolve(newFileName);
        try{
//            File destFile = new File(destPath.toString());
//            OutputStream stream = new FileOutputStream(destFile,false);
//            org.apache.commons.io.IOUtils.write(bytes,stream);

            OpenOption[] options =
                    new OpenOption[] {StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING};
            if (!Files.exists(getPath())) {
                Files.createDirectories(getPath());
            }
            Files.createFile(destPath);
            Files.write(destPath,bytes,options);
            RegionResource itemNew = new RegionResource();
            itemNew.setOriginFileName(originFileName);
            itemNew.setType(postfix);
            itemNew.setRegion(itemRegion);
            itemNew.setResUrl(destPath.toUri().toURL().toString());
            regionRescourceDao.save(itemNew);
//            Resource itemResource = new UrlResource(destPath.toUri());
            return itemNew;
        }catch (IOException ex){
            return  null;
        }
    }

    @Override
    public void addResource(Region region, MultipartFile file) {
        if (region == null) {
            return;
        }
        Path resPath = storeFile(file);
        RegionResource regResNew = new RegionResource();
        regResNew.setRegion(region);
        regResNew.setResUrl(resPath.toString());
        String postfix = StringUtils.substringAfter(file.getOriginalFilename(),".");
        regResNew.setType(postfix);

        regionRescourceDao.save(regResNew);
    }

    @Override
    public Page<Region> findAll(Pageable pageable) {
        Page<Region> pcollRegion = regionDao.findAll(pageable);
        for (Region item : pcollRegion) {
            if (item.getResources().size() > 0) {
                List<RegionResource> lstRes = IteratorUtils.toList(item.getResources().iterator());
                String url = lstRes.get(0).getResUrl();
            }
        }
        return pcollRegion;
    }

    @Override
    public RegionResource modifyResource(RegionResource regionResource) {
        return null;
    }

    @Override
    public RegionResource modifyResource(RegionResource regionResource,MultipartFile file) {

        try{
            Region itemRegion = regionResource.getRegion();
            //删除原始文件
            Path pathOld = Paths.get(regionResource.getResUrl());
            Files.deleteIfExists(pathOld);

            //记录原始文件信息
            String strOrigin = regionResource.getResUrl();
            Path pathNew = storeFile(file);

            String postfix = StringUtils.substringAfter(file.getOriginalFilename(), ".");
            String currDate = DateTime.now().toString(appPropertiesConfig.getAppShortDateFormat());
            String newFileName = String.format("%s%s%d.%s",itemRegion.getCode(),currDate,
                    appPropertiesConfig.getRandomLessHundred(),postfix);
            Path destPath = getPath().resolve(newFileName);
            Files.copy(file.getInputStream(), destPath,
                    StandardCopyOption.REPLACE_EXISTING);

            regionResource.setType(postfix);
            regionResource.setOriginFileName(file.getOriginalFilename());
            regionResource.setResUrl(destPath.toUri().toURL().toString());
            regionRescourceDao.save(regionResource);

            return regionResource;
        } catch (IOException ex) {
            //日志记录
            logger.error(String.format("存储Region文件时错误：%s",ex.getMessage()));
        }
        return null;
    }


    @Override
    public RegionResource modifyResource(RegionResource regionResource, byte[] bytes, String originFileName, String postfix) {

        Region itemRegion = regionResource.getRegion();
        String currDate = DateTime.now().toString(appPropertiesConfig.getAppShortDateFormat());
        String newFileName = String.format("%s%s%d.%s",itemRegion.getCode(),currDate,
                appPropertiesConfig.getRandomLessHundred(),postfix);
        Path destPath = getPath().resolve(newFileName);
        try{
//            File destFile = new File(destPath.toString());
//            OutputStream stream = new FileOutputStream(destFile,false);
//            org.apache.commons.io.IOUtils.write(bytes,stream);

            String strOld = regionResource.getResUrl();
            if (regionResource.getResUrl().startsWith("file:/")) {
                strOld = regionResource.getResUrl().replaceFirst("file:/","");
            }
            Path pathOld = Paths.get(strOld);
            Files.deleteIfExists(pathOld);

            OpenOption[] options =
                    new OpenOption[] {StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING};
            if (!Files.exists(getPath())) {
                Files.createDirectories(getPath());
            }
            Files.createFile(destPath);
            Files.write(destPath,bytes,options);
            regionResource.setOriginFileName(originFileName);
            regionResource.setType(postfix);
            regionResource.setResUrl(destPath.toUri().toURL().toString());
            regionRescourceDao.save(regionResource);
//            Resource itemResource = new UrlResource(destPath.toUri());
            return regionResource;
        }catch (IOException ex){
            logger.error(String.format("%s Failed to store file:%s.%s",
                   this.getClass().getName(),ex.getMessage(), System.lineSeparator()));//System.lineSeparator()换行符
        }

        return null;
    }


    @Override
    public void delete(Region region) {

    }

    @Override
    public void deleteResource(RegionResource regionResource) {

    }

    @Override
    public Path uploadAvatarExtend(Region region, String fileName, String postfix, byte[] bytes) {
        Path itemNew = uploadAvatar(fileName,postfix, bytes);
        if (itemNew == null) {
            return null;
        }
        //将Path路径保存至Region的IconUrl属性
        modifyIcon(region,itemNew);

        return itemNew;
    }

    @Override
    public Path uploadAvatarExtend(Region region, MultipartFile file) {
        Path itemNew = uploadAvatar(file);
        if (itemNew == null) {
            return null;
        }
        //将Path路径保存至Region的IconUrl属性
        modifyIcon(region,itemNew);

        return itemNew;
    }

    @Override
    public Path loadAvatar(Region region, AvatorLoadFactor factor) {
        String strUrl = region.getLogoUrl();
        return loadAvatarInner(strUrl,factor);
    }

    @Override
    public Page<Region> queryAllByIdIsNotNull(Pageable pageable) {
        return regionDao.queryAllByIdIsNotNull(pageable);
    }

    @Override
    public Page<Region> queryRegionsByIdIsNotNull(Pageable page) {
        return regionDao.queryRegionsByIdIsNotNull(page);
    }

    @Override
    public Optional<Region> queryFirstByCode(String code) {
        return regionDao.queryFirstByCode(code);
    }

    @Override
    public Optional<Region> findFirstByCode(String code) {
        return regionDao.findFirstByCode(code);
    }
}
