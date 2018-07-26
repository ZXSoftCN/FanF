package com.zxsoft.fanfanfamily.base.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public interface BaseService<T> {
//    void initPath();
    Path getOriginPath();
    Path getPath();
    JpaRepository<T,String> getBaseDao();
    Optional<T> getById(String id);
    Path storeFile(MultipartFile file);
    List<T> findAll();
    Page<T> findAll(Pageable pageable);
    T save(T t);
    void Delete(T t);
    void DeleteAll(List<T> t);
}
