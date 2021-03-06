package com.zxsoft.fanfanfamily.base.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.zxsoft.fanfanfamily.base.domain.BaseEntity;
import com.zxsoft.fanfanfamily.base.domain.vo.AvatorLoadFactor;
import com.zxsoft.fanfanfamily.base.domain.vo.EntityIdDTO;
import com.zxsoft.fanfanfamily.base.service.BaseService;
import com.zxsoft.fanfanfamily.base.sys.FanfAppBody;
import com.zxsoft.fanfanfamily.config.converter.FanFResponseBodyBuilder;
import com.zxsoft.fanfanfamily.config.converter.FanFResponseBuilder;
import com.zxsoft.fanfanfamily.config.converter.FanfAppData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

/**
 * 在Controller层提供基础方法：getById、queryPage、queryAll、addEntity、modifyEntity、
 * deleteEntity、deleteBatch、upload、loadAvatar。
 *
 *
 *  * 将Controller返回为FanfAppData格式。
 * 提供两种响应处理：
 * 1、注解@FanfAppBody，返回ResponseEntity对象，通过ApiResponseAdvice会将其转化成FanfAppData格式；
 * 2、使用FanFResponseBodyBuilder中方法，可加入msg内容，描述服务执行信息。
 * @param <T>
 */
public abstract class BaseRestControllerImpl<T extends BaseEntity> implements BaseRestController<T> {

    private Logger log = LoggerFactory.getLogger(getEntityType().getClass());
    @Autowired
    protected HttpServletRequest httpServletRequest;


    public abstract BaseService<T> getBaseService();
    public abstract Class<T> getEntityType();

    @Override
    @FanfAppBody
    @GetMapping(value = "/get/{id}")
    public ResponseEntity<T> getById(@PathVariable(required = false,name = "id") String id) {
        Optional<T> item = getBaseService().getById(id);
        return ResponseEntity.ok(item.orElse(null));
//        if (item.isPresent()) {
//            return ResponseEntity.ok(item.get());
//        } else {
//            //未能查询出结果，可抛出异常。到@ExceptionHandler中进行处理
//            return ResponseEntity.ok(null);
//        }
    }

    @Override
    @FanfAppBody
    @GetMapping(value = "/getByKey/{key}")
    public ResponseEntity<T> getByKey(@PathVariable(required = false,name = "key") String key) {
        Optional<T> item = getBaseService().getByKey(key);
        return ResponseEntity.ok(item.orElse(null));
    }

    @Override
    @FanfAppBody
    @PostMapping(value = "/getEntity")
    public ResponseEntity<T> getEntity(@RequestBody String entityKey) {
        Feature[] serializerFeatures = {
                Feature.AllowSingleQuotes};

//        Object obj = JSON.parse(parsingEntity, serializerFeatures);
//        TypeReference<T> type = new TypeReference<T>() {};
        EntityIdDTO dto = JSON.parseObject(entityKey,EntityIdDTO.class,serializerFeatures);
        if (dto != null) {
            Optional<T> item = getBaseService().getById(dto.getId());
            return ResponseEntity.ok(item.orElse(null));
        }
        return ResponseEntity.ok(null);
    }

    @Override
//    @PageableBody
    @FanfAppBody
    @RequestMapping(value = "/query")
    public ResponseEntity<Page<T>> queryPage(@PageableDefault(size = 15,page = 0,sort = "id",direction = Sort.Direction.ASC)
                                                        Pageable pageable) {
        //请求页码默认以第1页开始，所以除了录入0页码时，均向前翻一页。
        if (pageable.getPageNumber() > 0) {
            pageable = pageable.previousOrFirst();
        }
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
        return ResponseEntity.ok(item);
//        if (item != null) {
//            return ResponseEntity.ok(item);
//        }else {
//            //未能查询出结果，可抛出异常。到@ExceptionHandler中进行处理
//            return ResponseEntity.badRequest().body(null);
//        }
    }

    @Override
    @FanfAppBody
    @PostMapping(value = "/add")
    public ResponseEntity<T> addEntity(@RequestBody String parsingEntity) {
        Feature[] serializerFeatures = {
                Feature.AllowSingleQuotes};

//        Object obj = JSON.parse(parsingEntity, serializerFeatures);
//        TypeReference<T> type = new TypeReference<T>() {};
        try {
            T t = JSON.parseObject(parsingEntity, getEntityType(), serializerFeatures);

            T item = getBaseService().add(t);
            return ResponseEntity.ok(item);
        } catch (Exception ex) {
            return FanFResponseBuilder.failure(ex.getMessage(),null);
        }
    }

    @Override
    @FanfAppBody
    @PostMapping("/modifyEntity")
    public ResponseEntity<T> modifyEntity(@RequestBody T t) {
        T item = getBaseService().modify(t);
        return ResponseEntity.ok(item);
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
        return ResponseEntity.ok(item);
    }

    @Override
    @FanfAppBody
    @PostMapping("/saveBatch")
    public ResponseEntity<List<T>> saveBatch(@RequestBody List<T> lstEntity) {

        List<T> lstItem = getBaseService().saveBatch(lstEntity);
        return ResponseEntity.ok(lstItem);
//        if (lstItem.size() > 0) {
//            return ResponseEntity.ok(lstItem);
//        }else {
//            //未能查询出结果，可抛出异常。到@ExceptionHandler中进行处理
//            return ResponseEntity.badRequest().body(null);
//        }
    }

    @Override
//    @FanfAppBody
    @PostMapping("/delete")
    public FanfAppData deleteEntity(@RequestBody String jsonId) {
        Boolean isDel = false;
        JSONObject jobjItem = JSON.parseObject(jsonId);
        if (!jobjItem.isEmpty() && jobjItem.containsKey("id")) {
            String idValue = jobjItem.getString("id");
            isDel = getBaseService().delete(idValue);
        }
        return isDel ?  FanFResponseBodyBuilder.ok("删除完成", isDel) : FanFResponseBodyBuilder.failure(isDel);
//        return ResponseEntity.ok(isDel);
    }

    @Override
//    @FanfAppBody
    @PostMapping("/deleteBatch")
    public FanfAppData deleteBatch(@RequestBody String jsonIds) {
        Boolean isDel = false;
        JSONObject jobjItem = JSON.parseObject(jsonIds);
        if (!jobjItem.isEmpty() && jobjItem.containsKey("ids")) {
            JSONArray collId = jobjItem.getJSONArray("ids");
            if (collId.size() > 0) {
                isDel = getBaseService().deleteBatch(collId.toJavaList(String.class));
            }
        }

        return isDel ?  FanFResponseBodyBuilder.ok("删除完成", isDel) : FanFResponseBodyBuilder.failure(isDel);
//        return ResponseEntity.ok(isDel);
    }



    @Override
    @FanfAppBody
    @PostMapping(value = "/upload",consumes = "multipart/form-data")
    public ResponseEntity<String> upload(@RequestParam(value = "id",required = true) String id,
                                         @RequestParam(value = "fileName",required = false,defaultValue = "Empty") String fileName,
                                         @RequestParam(value = "postfix",required = true) String postfix,
                                         @RequestBody(required = true) byte[] bytes) {
        String path =null;
        Optional<T> item = getBaseService().getById(id);
        if (item.isPresent()) {
            path = getBaseService().uploadAvatarExtend(item.get(),fileName,postfix,bytes);
        }
        return ResponseEntity.ok().body(path);
    }

    @Override
    @FanfAppBody
    @PostMapping(value = "/uploadAvatar",consumes = "multipart/form-data")
    public ResponseEntity<String> upload(@RequestParam(name = "file") MultipartFile file,@RequestParam(name = "id",required = false) String id) {
        String strRlt=null;
        String path = null;
        Optional<T> item = getBaseService().getById(id);
        if (item.isPresent()) {
            path = getBaseService().uploadAvatarExtend(item.get(),file);
        }else{
            path = getBaseService().uploadAvatar(file);
        }

        if (path != null) {
            String strHeaderURL = StringUtils.replace(httpServletRequest.getRequestURL().toString(),httpServletRequest.getRequestURI(),"");
            strRlt = StringUtils.join(strHeaderURL,"/",path);
        }
        return ResponseEntity.ok().body(strRlt);
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
