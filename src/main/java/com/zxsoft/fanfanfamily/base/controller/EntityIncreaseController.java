package com.zxsoft.fanfanfamily.base.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.zxsoft.fanfanfamily.base.domain.EntityIncrease;
import com.zxsoft.fanfanfamily.base.domain.Menu;
import com.zxsoft.fanfanfamily.base.domain.vo.EntityIdDTO;
import com.zxsoft.fanfanfamily.base.service.EntityIncreaseService;
import com.zxsoft.fanfanfamily.base.sys.FanfAppBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 编码规则
 */
@RestController
@RequestMapping(value = "/api/entityIncrease")
public class EntityIncreaseController {

    @Autowired
    private EntityIncreaseService entityIncreaseService;

    private Logger log = LoggerFactory.getLogger(EntityIncreaseController.class);
    
    @FanfAppBody
    @GetMapping(value = "/get/{id}")
    public ResponseEntity<EntityIncrease> getById(@PathVariable(required = false,name = "id") String id) {
        Optional<EntityIncrease> item = entityIncreaseService.getById(id);
        if (item.isPresent()) {
            return ResponseEntity.ok(item.get());
        } else {
            //未能查询出结果，可抛出异常。到@ExceptionHandler中进行处理
            return ResponseEntity.ok(null);
        }
    }
    
    @FanfAppBody
    @GetMapping(value = "/getByKey/{key}")
    public ResponseEntity<EntityIncrease> getByKey(@PathVariable(required = false,name = "key") String key) {
        Optional<EntityIncrease> item = entityIncreaseService.getByKey(key);
        if (item.isPresent()) {
            return ResponseEntity.ok(item.get());
        } else {
            //未能查询出结果，可抛出异常。到@ExceptionHandler中进行处理
            return ResponseEntity.ok(null);
        }
    }

    @FanfAppBody
    @PostMapping(value = "/getEntity")
    public ResponseEntity<EntityIncrease> getEntity(@RequestBody String entityKey) {
        Feature[] serializerFeatures = {
                Feature.AllowSingleQuotes};

//        Object obj = JSON.parse(parsingEntity, serializerFeatures);
//        TypeReference<T> type = new TypeReference<T>() {};
        EntityIdDTO dto = JSON.parseObject(entityKey,EntityIdDTO.class,serializerFeatures);
        if (dto != null) {
            Optional<EntityIncrease> item = entityIncreaseService.getById(dto.getId());
            if (item.isPresent()) {
                return ResponseEntity.ok(item.get());
            } else {
                //未能查询出结果，可抛出异常。到@ExceptionHandler中进行处理
                return ResponseEntity.ok(null);
            }
        }
        return ResponseEntity.ok(null);
    }

    @FanfAppBody
    @RequestMapping(value = "/query")
    public ResponseEntity<Page<EntityIncrease>> queryPage(@PageableDefault(size = 15,page = 0,sort = "id",direction = Sort.Direction.ASC)
                                                     Pageable pageable) {
        Page<EntityIncrease> pageColl = entityIncreaseService.findAll(pageable);

        return ResponseEntity.ok(pageColl);
    }

    @FanfAppBody
    @RequestMapping(value = "/queryAll")
    public ResponseEntity<List<EntityIncrease>> queryAll() {
        List<EntityIncrease> lst = entityIncreaseService.findAll();
        return ResponseEntity.ok(lst);
    }

    @FanfAppBody
    @PostMapping(value = "/addEntity")
    public ResponseEntity<EntityIncrease> addEntity(@RequestBody EntityIncrease t) {
        EntityIncrease item = entityIncreaseService.add(t);
        if (item != null) {
            return ResponseEntity.ok(item);
        }else {
            //未能查询出结果，可抛出异常。到@ExceptionHandler中进行处理
            return ResponseEntity.badRequest().body(null);
        }
    }

    
    @FanfAppBody
    @PostMapping(value = "/add")
    public ResponseEntity<EntityIncrease> addEntity(@RequestBody String parsingEntity) {
        Feature[] serializerFeatures = {
                Feature.AllowSingleQuotes};

        EntityIncrease t = JSON.parseObject(parsingEntity,EntityIncrease.class,serializerFeatures);

        EntityIncrease item = entityIncreaseService.add(t);
        if (item != null) {
            return ResponseEntity.ok(item);
        }else {
            //未能查询出结果，可抛出异常。到@ExceptionHandler中进行处理
            return ResponseEntity.badRequest().body(null);
        }
    }

    
    @FanfAppBody
    @PostMapping("/modifyEntity")
    public ResponseEntity<EntityIncrease> modifyEntity(@RequestBody EntityIncrease t) {
        EntityIncrease item = entityIncreaseService.modify(t);
        if (item != null) {
            return ResponseEntity.ok(item);
        }else {
            //未能查询出结果，可抛出异常。到@ExceptionHandler中进行处理
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @FanfAppBody
    @PostMapping("/modify")
    public ResponseEntity<EntityIncrease> modifyEntity(@RequestBody String parsingEntity) {
        Feature[] serializerFeatures = {
                Feature.AllowSingleQuotes};
//        Object obj = JSON.parse(parsingEntity, serializerFeatures);
//        TypeReference<T> type = new TypeReference<T>() {};
        EntityIncrease t = JSON.parseObject(parsingEntity,EntityIncrease.class,serializerFeatures);

        EntityIncrease item = entityIncreaseService.modify(t);
        if (item != null) {
            return ResponseEntity.ok(item);
        }else {
            //未能查询出结果，可抛出异常。到@ExceptionHandler中进行处理
            return ResponseEntity.badRequest().body(null);
        }
    }

    
    @FanfAppBody
    @PostMapping("/saveBatch")
    public ResponseEntity<List<EntityIncrease>> saveBatch(@RequestBody List<EntityIncrease> lstEntity) {

        List<EntityIncrease> lstItem = entityIncreaseService.saveBatch(lstEntity);
        if (lstItem.size() > 0) {
            return ResponseEntity.ok(lstItem);
        }else {
            //未能查询出结果，可抛出异常。到@ExceptionHandler中进行处理
            return ResponseEntity.badRequest().body(null);
        }
    }

    
    @FanfAppBody
    @PostMapping("/delete")
    public ResponseEntity<Boolean> deleteEntity(@RequestBody String jsonId) {
        Boolean isDel = false;
        JSONObject jobjItem = JSON.parseObject(jsonId);
        if (!jobjItem.isEmpty() && jobjItem.containsKey("id")) {
            String idValue = jobjItem.getString("id");
            isDel = entityIncreaseService.delete(idValue);
        }
        return ResponseEntity.ok(isDel);
    }

    
    @FanfAppBody
    @PostMapping("/deleteBatch")
    public ResponseEntity<Boolean> deleteBatch(@RequestBody String jsonIds) {
        Boolean isDel = false;
        JSONObject jobjItem = JSON.parseObject(jsonIds);
        if (!jobjItem.isEmpty() && jobjItem.containsKey("ids")) {
            JSONArray collId = jobjItem.getJSONArray("ids");
            if (collId.size() > 0) {
                isDel = entityIncreaseService.deleteBatch(collId.toJavaList(String.class));
            }
        }

        return ResponseEntity.ok(isDel);
    }

    @FanfAppBody
    @RequestMapping(value = "/queryParams")
    public ResponseEntity<Page<EntityIncrease>> queryPageParams(@PageableDefault(size = 15,page = 0,sort = "entityName",direction = Sort.Direction.ASC)
                                                              Pageable pageable, @RequestParam(name = "name", required = false,defaultValue = "") String name) {

        //请求页码默认以第1页开始，所以除了录入0页码时，均向前翻一页。
        if (pageable.getPageNumber() > 0) {
            pageable = pageable.previousOrFirst();
        }
        Page<EntityIncrease> pageColl = entityIncreaseService.findEntityIncreaseByName(name,pageable);

        return ResponseEntity.ok(pageColl);
    }
}
