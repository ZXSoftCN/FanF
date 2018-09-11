package com.zxsoft.fanfanfamily.base.controller;

import com.zxsoft.fanfanfamily.base.domain.UserInfo;
import com.zxsoft.fanfanfamily.base.domain.vo.UserPermissionDTO;
import com.zxsoft.fanfanfamily.base.domain.vo.UserPermissionInner;
import com.zxsoft.fanfanfamily.base.service.BaseService;
import com.zxsoft.fanfanfamily.base.service.UserInfoService;
import com.zxsoft.fanfanfamily.base.sys.FanfAppBody;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServlet;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping(value = "/api/user")
public class UserController extends BaseRestControllerImpl<UserInfo> {


    @Autowired
    private UserInfoService userInfoService;

    @Override
    public BaseService getBaseService() {
        return userInfoService;
    }

    @Override
    public Class<UserInfo> getEntityType() {
        return UserInfo.class;
    }

    @RequestMapping("/userinfo")
    @FanfAppBody
    public ResponseEntity<UserInfo> getUserInfo(@RequestParam(name = "userName",required = true) String userName) {
//        JSON.parseObject(userName).getString("userName")
        Optional<UserInfo> item = userInfoService.getByKey(userName);
        if (item.isPresent()) {
            return ResponseEntity.ok(item.get());
        } else {
            //未能查询出结果，可抛出异常。到@ExceptionHandler中进行处理
            return ResponseEntity.ok(item.get());
        }
    }

    @RequestMapping("/userPermission")
    @FanfAppBody
    public ResponseEntity<UserPermissionDTO> getUserInfoPermission(@RequestParam(name = "userName",required = false) String userName) {
//        JSON.parseObject(userName).getString("userName")
        if (userName == null) {
            return ResponseEntity.ok(null);
        }
        Optional<UserPermissionDTO> dto = userInfoService.findUserInfoPermission(userName);
        return ResponseEntity.ok(dto.orElse(null));
    }

    @FanfAppBody
    @RequestMapping(value = "/queryParams")
    public ResponseEntity<Page<UserInfo>> queryPageParams(@PageableDefault(size = 15,page = 0,sort = "userName",direction = Sort.Direction.ASC)
                                                                      Pageable pageable,@RequestParam(name = "userNameQuery", required = false,defaultValue = "") String userName,
                                                          @RequestParam(name = "createTimeQuery", required = false) String[] arrCreateTime) {
        /*DateTime[] createTimes = new DateTime[arrCreateTime.length];
        for (int i = 0; i < arrCreateTime.length; i++) {
            createTimes[i] = DateTime.parse(arrCreateTime[i]);
        }*/
        //请求页码默认以第1页开始，所以除了录入0页码时，均向前翻一页。
        if (pageable.getPageNumber() > 0) {
            pageable = pageable.previousOrFirst();
        }
        Page<UserInfo> pageColl = userInfoService.findUserInfoByCreateTime(userName,arrCreateTime,pageable);

        return ResponseEntity.ok(pageColl);
    }

    @FanfAppBody
    @PostMapping(value = "/upload",consumes = "multipart/form-data")
    public ResponseEntity<String> uploadAvatar(@RequestParam("avatar") MultipartFile file) {
        String strRlt=null;
//        Optional<T> item = getBaseService().getById(id);

        String path = userInfoService.uploadAvatar(file);
        if (path != null) {
            String strHeaderURL = StringUtils.replace(httpServletRequest.getRequestURL().toString(),httpServletRequest.getRequestURI(),"");
            strRlt = StringUtils.join(strHeaderURL,"/",path);
            return ResponseEntity.ok().body(strRlt);
        }

//        if (item.isPresent()) {
//            path = userInfoService.uploadAvatar(file);
//            if (path != null) {
//                return ResponseEntity.ok().body(path);
//            }
//        }
        return ResponseEntity.badRequest().body(null);
    }
}
