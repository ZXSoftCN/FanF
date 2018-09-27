package com.zxsoft.fanfanfamily.mort.controller;

import com.zxsoft.fanfanfamily.base.controller.BaseRestControllerImpl;
import com.zxsoft.fanfanfamily.base.domain.mort.Bank;
import com.zxsoft.fanfanfamily.base.service.BaseService;
import com.zxsoft.fanfanfamily.base.sys.FanfAppBody;
import com.zxsoft.fanfanfamily.mort.domain.vo.BankWithChildDTO;
import com.zxsoft.fanfanfamily.mort.repository.BankDao;
import com.zxsoft.fanfanfamily.mort.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/bank")
public class BankController  extends BaseRestControllerImpl<Bank> {


    @Autowired
    private BankService bankService;

    @Override
    public BaseService getBaseService() {
        return bankService;
    }

    @Override
    public Class<Bank> getEntityType() {
        return Bank.class;
    }

    @RequestMapping("/queryTree")
    @FanfAppBody
    public ResponseEntity<List<BankWithChildDTO>> queryTree() {

        List<BankWithChildDTO> lstBank = bankService.queryTree();

        if (lstBank != null) {
            return ResponseEntity.ok(lstBank);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @RequestMapping("/querySubs")
    @FanfAppBody
    public ResponseEntity<List<Bank>> querySubs(@RequestParam(name = "id") String id) {

        List<Bank> lstBank = bankService.querySubs(id);
        if (lstBank != null) {
            return ResponseEntity.ok(lstBank);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
