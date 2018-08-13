package com.zxsoft.fanfanfamily.base.controller;

import com.zxsoft.fanfanfamily.base.domain.BaseEntity;
import com.zxsoft.fanfanfamily.base.domain.vo.AvatorLoadFactor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

public interface BaseRestController<T extends BaseEntity> {

    ResponseEntity<T> getById(String id);
    ResponseEntity<Page<T>> queryPage(Pageable pageable);
    ResponseEntity<List<T>> queryAll();

    ResponseEntity<T> addEntity(T t);
    ResponseEntity<T> addEntity(String parsingEntity);
    ResponseEntity<T> modifyEntity(T t);
    ResponseEntity<T> modifyEntity(String parsingEntity);
    ResponseEntity deleteEntity(String jsonId);
    ResponseEntity deleteBatch(String jsonIds);

    ResponseEntity<Path> uploadAvatar(String id,String fileName,String postfix, byte[] bytes);//以byte[]方式上传头像、图标
    ResponseEntity<Path> loadAvatar(String id,AvatorLoadFactor factor);
}
