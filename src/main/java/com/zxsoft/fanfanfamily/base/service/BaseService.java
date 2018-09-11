package com.zxsoft.fanfanfamily.base.service;

import com.zxsoft.fanfanfamily.base.domain.BaseEntity;
import com.zxsoft.fanfanfamily.base.domain.Region;
import com.zxsoft.fanfanfamily.base.domain.vo.AvatorLoadFactor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public interface BaseService<T> {
//    void initPath();
    Path getOriginPath();
    Path getPath();
    Path getAvatarPath();
    JpaRepository<T,String> getBaseDao();

    //region 用于BaseController对应基础方法
    Optional<T> getById(String id);
    Optional<T> getByKey(String key);//可通过code或name来查询
    List<T> findAll();
    Page<T> findAll(Pageable pageable);
    T add(T t);
    T modify(T t);
    T save(T t);
    List<T> saveBatch(List<T> collT);
    Boolean delete(String id);
    Boolean deleteBatch(List<String> ids);
    String uploadAvatarExtend(T t, String fileName, String postfix, byte[] bytes);

    //endregion

    String getEntityName();
    AtomicInteger getSortNoMax();
    void initSortNoMax();
    int getNewSortNo();
    AtomicInteger getCodeNumMax();
    void initCodeNumMax();
    String getNewCode();


    Path storeFile(MultipartFile file);

    String uploadAvatar(MultipartFile file);//以MultipartFile方式上传头像、图标
    String uploadAvatar(String fileName,String postfix,byte[] bytes);//以字节数组上传头像、图标
    String uploadAvatar(String fileName,byte[] bytes);//以字节数组上传头像、图标
    String uploadAvatarExtend(T t, MultipartFile file);

    Resource downloadResource(String url);

    Path loadAvatar(T t);//获取T的图标、头像、缩略图
    Path loadAvatar(T t, int width, int height, double scaling);//按尺寸，清晰度获取
    Path loadAvatar(T t, AvatorLoadFactor factor);//按尺寸，清晰度获取
}
