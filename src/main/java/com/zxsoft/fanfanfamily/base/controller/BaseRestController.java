package com.zxsoft.fanfanfamily.base.controller;

import com.zxsoft.fanfanfamily.base.domain.BaseEntity;
import com.zxsoft.fanfanfamily.base.domain.vo.AvatorLoadFactor;
import com.zxsoft.fanfanfamily.config.converter.FanfAppData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

public interface BaseRestController<T extends BaseEntity> {

    ResponseEntity<T> getById(String id);
    ResponseEntity<T> getByKey(String key);
    ResponseEntity<T> getEntity(String id);
    ResponseEntity<Page<T>> queryPage(Pageable pageable);
    ResponseEntity<List<T>> queryAll();

    ResponseEntity<T> addEntity(T t);
    ResponseEntity<T> addEntity(String parsingEntity);
    ResponseEntity<T> modifyEntity(T t);
    ResponseEntity<T> modifyEntity(String parsingEntity);
    ResponseEntity<List<T>> saveBatch(List<T> lstEntity);
    FanfAppData deleteEntity(String jsonId);
    FanfAppData deleteBatch(String jsonIds);

    ResponseEntity<String> uploadAvatar(String id,String fileName,String postfix, byte[] bytes);//以byte[]方式上传头像、图标
    ResponseEntity<Path> loadAvatar(String id,AvatorLoadFactor factor);
}
