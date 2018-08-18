package com.zxsoft.fanfanfamily.base.controller;

import com.zxsoft.fanfanfamily.base.domain.Role;
import com.zxsoft.fanfanfamily.base.domain.UserInfo;
import com.zxsoft.fanfanfamily.base.service.BaseService;
import com.zxsoft.fanfanfamily.base.service.RoleService;
import com.zxsoft.fanfanfamily.base.service.UserInfoService;
import com.zxsoft.fanfanfamily.base.service.impl.UserInfoServiceImpl;
import com.zxsoft.fanfanfamily.base.sys.FanfAppBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
            return ResponseEntity.badRequest().body(null);
        }
    }
}
