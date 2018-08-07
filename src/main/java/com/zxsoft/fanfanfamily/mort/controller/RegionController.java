package com.zxsoft.fanfanfamily.mort.controller;

import com.zxsoft.fanfanfamily.base.domain.Region;
import com.zxsoft.fanfanfamily.base.domain.RegionResource;
import com.zxsoft.fanfanfamily.base.service.RegionService;
import com.zxsoft.fanfanfamily.base.sys.PageableBody;
import org.hibernate.collection.internal.PersistentSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @PostMapping(value = "/region/add")
    public Region addRegion(@RequestBody Region region){
        return regionService.save(region);
    }

    @GetMapping("/region/get/{regionId}")
    public ResponseEntity<Region> queryRegion(@PathVariable(required = false) Region regionId) {

        ResponseEntity<Region> itemR;
        Optional<Region> rltRegion = regionService.getById(regionId.getId());

        if (rltRegion.isPresent()) {
            itemR = ResponseEntity.ok(rltRegion.get());
        }else{
            itemR = ResponseEntity.status(200).body(null);
        }
        return itemR;
    }

    @GetMapping("/region/getbycode/{code}")
    public ResponseEntity<Region> queryRegion(@PathVariable(required = false,name = "code") String code) {

        ResponseEntity<Region> itemR;
//        Optional<Region> rltRegion = regionService.findFirstByCode(code);//不级联查询。但后续有级联查询，会造成数据加载。
        Optional<Region> rltRegion2 = regionService.queryFirstByCode(code);//启用当前查询会引起上一查询，进行级联查询。
//        Optional<Region> rltRegion = regionService.getById(regionId);

        if (rltRegion2.isPresent()) {
            itemR = ResponseEntity.ok(rltRegion2.get());
        }else{
            itemR = ResponseEntity.status(200).body(null);
        }
        return itemR;
    }

    @PageableBody
    @GetMapping("/region/get")
    public ResponseEntity<Page<Region>> queryRegionList(@RequestParam(value = "page",required = false,defaultValue = "0") int page,
                                                        @RequestParam(value = "size",required = false,defaultValue = "2") int size,
                                                        @RequestParam(value = "sort",required = false,defaultValue = "code") String sort) {
        ResponseEntity<Page<Region>> itemR;
        Sort itemSort = Sort.by(Sort.Direction.ASC,sort);
        Pageable pageable = PageRequest.of(page,size, itemSort);
//        Page<Region> pcollRegion = regionService.findAll(pageable);
        Page<Region> pcollRegion2 = regionService.queryRegionsByIdIsNotNull(pageable);
        if (pcollRegion2.getSize() > 0) {
            itemR = ResponseEntity.ok(pcollRegion2);
        }else{
            itemR = ResponseEntity.status(200).body(null);
        }
        return itemR;
    }

    @PostMapping(value = "/region/updateAvatar",consumes = "multipart/form-data")
    public ResponseEntity<String> uploadAvatar(@RequestParam(value = "regionId",required = true) Region region,
                                                 @RequestParam(value = "fileName",required = false,defaultValue = "Empty") String fileName,
                                                 @RequestParam(value = "postfix",required = true) String postfix,
                                                 @RequestBody(required = true) byte[] bytes){
        ResponseEntity<String> itemR ;
        Path item =  regionService.uploadAvatarExtend(region,fileName,postfix,bytes);

        if (item != null) {
            itemR = ResponseEntity.ok(item.toString());
        }else{
            itemR = ResponseEntity.status(200).body("");
        }
        return itemR;
    }

    @PostMapping(value = "/region/loadAvatar")
    public ResponseEntity<String> loadAvatar(@RequestParam(value = "regionId",required = true) Region region,
                                                @RequestParam(value = "width",required = false,defaultValue = "0") int width,
                                                @RequestParam(value = "height",required = false,defaultValue = "0") int height,
                                                @RequestParam(value = "scaling",required = false,defaultValue = "1") double scaling){
        ResponseEntity<String> itemR ;
        Path item;
        if (width == 0 && height == 0) {
            item = regionService.loadAvatar(region);
        }else{
            item = regionService.loadAvatar(region,width,height,scaling);
        }

        if (item != null) {
            itemR = ResponseEntity.ok(item.toString());
        }else{
            itemR = ResponseEntity.status(200).body("");
        }
        return itemR;
    }

    //@RequestParam(value = "regionId",required = true) String regionId,
    @PostMapping(value = "/regionresource/add",consumes = "multipart/form-data")
    public ResponseEntity<RegionResource> addRegionResource(
                @RequestParam(value = "regionId",required = true) String regionId,
                @RequestParam("file") MultipartFile file){
        ResponseEntity<RegionResource> itemR;
        RegionResource item = regionService.addResource(file,regionId);
        if (item != null) {
            itemR = ResponseEntity.ok(item);
        }else{
            itemR = ResponseEntity.status(200).body(null);
        }
        return itemR;
    }

    @PostMapping(value = "/regionresource/addByBytes",consumes = "multipart/form-data")
    public ResponseEntity<RegionResource> addRegionResource(@RequestParam(value = "regionId",required = true) String regionId,
                                                            @RequestParam(value = "fileName",required = false,defaultValue = "Empty") String fileName,
                                                            @RequestParam(value = "postfix",required = true) String postfix,
                                                            @RequestBody(required = true) byte[] bytes){
        ResponseEntity<RegionResource> itemR ;
        RegionResource item =  regionService.addResource(bytes,fileName,postfix,regionId);

        if (item != null) {
            itemR = ResponseEntity.ok(item);
        }else{
            itemR = ResponseEntity.status(200).body(null);
        }
        return itemR;
    }

    @PostMapping(value = "/regionresource/modifyByBytes",consumes = "multipart/form-data")
    public ResponseEntity<RegionResource> moidifyRegionResource(
            @RequestParam(value = "regionResourceId",required = true) RegionResource regionResource,
            @RequestParam(value = "fileName",required = false,defaultValue = "Empty") String fileName,
            @RequestParam(value = "postfix",required = true) String postfix,
            @RequestBody(required = true) byte[] bytes){
        ResponseEntity<RegionResource> itemR ;
        RegionResource item =  regionService.modifyResource(regionResource, bytes,fileName,postfix);

        if (item != null) {
            itemR = ResponseEntity.ok(item);
        }else{
            itemR = ResponseEntity.status(200).body(null);
        }
        return itemR;
    }
}
