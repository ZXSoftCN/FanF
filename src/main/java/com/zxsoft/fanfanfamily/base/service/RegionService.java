package com.zxsoft.fanfanfamily.base.service;

import com.zxsoft.fanfanfamily.base.domain.Region;
import com.zxsoft.fanfanfamily.base.domain.RegionResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


public interface RegionService extends BaseService<Region> {

    RegionResource addResource(MultipartFile file, String regionId);
    RegionResource addResource(byte[] bytes, String originFileName, String postfix, String regionId);
    void addResource(Region region, MultipartFile file);
    RegionResource modifyResource(RegionResource regionResource);
    RegionResource modifyResource(RegionResource regionResource, MultipartFile file);
    RegionResource modifyResource(RegionResource regionResource, byte[] bytes, String originFileName, String postfix);
    void delete(Region region);
    void deleteResource(RegionResource regionResource);


    Page<Region> queryAllByIdIsNotNull(Pageable pageable);
    Page<Region> queryRegionsByIdIsNotNull(Pageable page);
    Optional<Region> queryFirstByCode(String code);
    Optional<Region> findFirstByCode(String code);
}
