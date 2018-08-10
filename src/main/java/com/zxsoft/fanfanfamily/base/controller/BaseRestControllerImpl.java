package com.zxsoft.fanfanfamily.base.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.zxsoft.fanfanfamily.base.domain.BaseEntity;
import com.zxsoft.fanfanfamily.base.domain.vo.AvatorLoadFactor;
import com.zxsoft.fanfanfamily.base.service.BaseService;
import com.zxsoft.fanfanfamily.base.sys.FanfAppBody;
import com.zxsoft.fanfanfamily.base.sys.PageableBody;
import com.zxsoft.fanfanfamily.config.converter.FanFResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public abstract class BaseRestControllerImpl<T extends BaseEntity> implements BaseRestController<T> {

    private Logger log = LoggerFactory.getLogger(BaseRestControllerImpl.class);

    public abstract BaseService<T> getBaseService();
    public abstract Class<T> getEntityType();

    @Override
    @FanfAppBody
    @GetMapping(value = "/get/{id}")
    public ResponseEntity<T> getById(@PathVariable(required = false,name = "id") String id) {
        Optional<T> item = getBaseService().getById(id);
        if (item.isPresent()) {
            return ResponseEntity.ok(item.get());
        } else {
            //未能查询出结果，可抛出异常。到@ExceptionHandler中进行处理
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Override
//    @PageableBody
    @FanfAppBody
    @RequestMapping(value = "/query")
    public ResponseEntity<Page<T>> queryPage(@PageableDefault(size = 15,page = 0,sort = "id",direction = Sort.Direction.ASC)
                                                        Pageable pageable) {
        Page<T> pageColl = getBaseService().findAll(pageable);

        return ResponseEntity.ok(pageColl);
    }

    @Override
    @FanfAppBody
    @RequestMapping(value = "/queryAll")
    public ResponseEntity<List<T>> queryAll() {
        List<T> lst = getBaseService().findAll();
        return ResponseEntity.ok(lst);
    }

    @Override
    @FanfAppBody
    @PostMapping(value = "/addEntity")
    public ResponseEntity<T> addEntity(@RequestBody T t) {
        T item = getBaseService().add(t);
        if (item != null) {
            return ResponseEntity.ok(item);
        }else {
            //未能查询出结果，可抛出异常。到@ExceptionHandler中进行处理
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Override
    @FanfAppBody
    @PostMapping(value = "/add")
    public ResponseEntity<T> addEntity(@RequestBody String parsingEntity) {
        Feature[] serializerFeatures = {
                Feature.AllowSingleQuotes};

//        Object obj = JSON.parse(parsingEntity, serializerFeatures);
//        TypeReference<T> type = new TypeReference<T>() {};
        T t = JSON.parseObject(parsingEntity,getEntityType(),serializerFeatures);

        T item = getBaseService().add(t);
        if (item != null) {
            return ResponseEntity.ok(item);
        }else {
            //未能查询出结果，可抛出异常。到@ExceptionHandler中进行处理
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Override
    @FanfAppBody
    @PostMapping("/modifyEntity")
    public ResponseEntity<T> moidifyEntity(@RequestBody T t) {
        T item = getBaseService().modify(t);
        if (item != null) {
            return ResponseEntity.ok(item);
        }else {
            //未能查询出结果，可抛出异常。到@ExceptionHandler中进行处理
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Override
    @FanfAppBody
    @PostMapping("/modify")
    public ResponseEntity<T> modifyEntity(@RequestBody String parsingEntity) {
        Feature[] serializerFeatures = {
                Feature.AllowSingleQuotes};
//        Object obj = JSON.parse(parsingEntity, serializerFeatures);
//        TypeReference<T> type = new TypeReference<T>() {};
        T t = JSON.parseObject(parsingEntity,getEntityType(),serializerFeatures);

        T item = getBaseService().modify(t);
        if (item != null) {
            return ResponseEntity.ok(item);
        }else {
            //未能查询出结果，可抛出异常。到@ExceptionHandler中进行处理
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Override
    @FanfAppBody
    @PostMapping("/delete")
    public ResponseEntity<Boolean> deleteEntity(@RequestBody String jsonId) {
        Boolean isDel = false;
        JSONObject jobjItem = JSON.parseObject(jsonId);
        if (!jobjItem.isEmpty() && jobjItem.containsKey("id")) {
            String idValue = jobjItem.getString("id");
            isDel = getBaseService().delete(idValue);
        }
        return ResponseEntity.ok(isDel);
    }

    @Override
    @FanfAppBody
    @PostMapping("/deleteBatch")
    public ResponseEntity<Boolean> deleteBatch(@RequestBody String jsonIds) {
        Boolean isDel = false;
        JSONObject jobjItem = JSON.parseObject(jsonIds);
        if (!jobjItem.isEmpty() && jobjItem.containsKey("ids")) {
            JSONArray collId = jobjItem.getJSONArray("ids");
            if (collId.size() > 0) {
                isDel = getBaseService().deleteBatch(collId.toJavaList(String.class));
            }
        }

        return ResponseEntity.ok(isDel);
    }



    @Override
    @FanfAppBody
    @PostMapping(value = "/updateAvatar",consumes = "multipart/form-data")
    public ResponseEntity<Path> uploadAvatar(@RequestParam(value = "id",required = true) String id,
                             @RequestParam(value = "fileName",required = false,defaultValue = "Empty") String fileName,
                             @RequestParam(value = "postfix",required = true) String postfix,
                             @RequestBody(required = true) byte[] bytes) {
        Path path =null;
        Optional<T> item = getBaseService().getById(id);
        if (item.isPresent()) {
            path = getBaseService().uploadAvatarExtend(item.get(),fileName,postfix,bytes);
            if (path != null) {
                return ResponseEntity.ok().body(path);
            }
        }
        return ResponseEntity.badRequest().body(null);
    }

    @Override
    @FanfAppBody
    @PostMapping(value = "/loadAvatar/{id}")
    public ResponseEntity<Path> loadAvatar(@PathVariable(value = "id") String id,
                                             AvatorLoadFactor factor) {
        Path path = null;
        Optional<T> item = getBaseService().getById(id);
        if (item.isPresent()) {
            if (factor != null && factor.getWidth() != null && factor.getHeight() != null) {
                path = getBaseService().loadAvatar(item.get(),factor);
            } else {
                path = getBaseService().loadAvatar(item.get());
            }

            if (path != null) {
                return ResponseEntity.ok().body(path);
            }
        }
        return ResponseEntity.badRequest().body(null);
    }
}
