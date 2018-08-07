package com.zxsoft.fanfanfamily.base.controller;

import com.zxsoft.fanfanfamily.base.domain.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BaseRestController<T extends BaseEntity> {

    ResponseEntity<T> getById(String id);
    ResponseEntity<Page<T>> queryPage(Pageable pageable);
    ResponseEntity<List<T>> queryAll();

    ResponseEntity<T> addEntity(T t);
    ResponseEntity<T> addEntity(String parsingEntity);
    ResponseEntity<T> moidifyEntity(T t);
    ResponseEntity<T> modifyEntity(String parsingEntity);
    ResponseEntity deleteEntity(String jsonId);
    ResponseEntity deleteBatch(String jsonIds);
}
