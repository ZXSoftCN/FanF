package com.zxsoft.fanfanfamily.mort.controller;

import com.zxsoft.fanfanfamily.base.domain.mort.Bank;
import com.zxsoft.fanfanfamily.mort.repository.BankDao;
import com.zxsoft.fanfanfamily.mort.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class BankController {


    @Autowired
    private BankService bankService;
    @Autowired
    private BankDao bankDao;


    @PostMapping(value = "/bank/add")
    public Bank addBank(@RequestBody Bank Bank){
        return bankService.save(Bank);
    }

    @GetMapping("/bank/get/{bankId}")
    public ResponseEntity<Bank> queryBank(@PathVariable(required = false) Bank bankId) {

        ResponseEntity<Bank> itemR;
        Optional<Bank> rltBank = bankService.getById(bankId.getId());
        if (rltBank.isPresent()) {
            itemR = ResponseEntity.ok(rltBank.get());
        }else{
            itemR = ResponseEntity.status(200).body(null);
        }
        return itemR;
    }

    @GetMapping("/bank/get")
    public ResponseEntity<Page<Bank>> queryBankList(@RequestParam(value = "page",required = false,defaultValue = "0") int page,
                                                        @RequestParam(value = "size",required = false,defaultValue = "5") int size,
                                                        @RequestParam(value = "sort",required = false,defaultValue = "code") String sort) {
        ResponseEntity<Page<Bank>> itemR;
        Sort itemSort = Sort.by(Sort.Direction.ASC,sort);
        Pageable pageable = PageRequest.of(page,size, itemSort);
        Page<Bank> pcollDao = bankDao.findAll(pageable);
        Page<Bank> pcollBank = bankService.findAll(pageable);
        if (pcollBank.getSize() > 0) {
            itemR = ResponseEntity.ok(pcollBank);
        }else{
            itemR = ResponseEntity.status(200).body(null);
        }
        return itemR;
    }

    @PostMapping(value = "/bank/updateAvatar",consumes = "multipart/form-data")
    public ResponseEntity<String> uploadAvatar(@RequestParam(value = "bankId",required = true) Bank bank,
                                                 @RequestParam(value = "fileName",required = false,defaultValue = "Empty") String fileName,
                                                 @RequestParam(value = "postfix",required = true) String postfix,
                                                 @RequestBody(required = true) byte[] bytes){
        ResponseEntity<String> itemR ;
        Path item =  bankService.uploadAvatarExtend(bank,fileName,postfix,bytes);

        if (item != null) {
            itemR = ResponseEntity.ok(item.toString());
        }else{
            itemR = ResponseEntity.status(200).body("");
        }
        return itemR;
    }

    @PostMapping(value = "/bank/loadAvatar")
    public ResponseEntity<String> loadAvatar(@RequestParam(value = "bankId",required = true) Bank bank,
                                                @RequestParam(value = "width",required = false,defaultValue = "0") int width,
                                                @RequestParam(value = "height",required = false,defaultValue = "0") int height,
                                                @RequestParam(value = "scaling",required = false,defaultValue = "1") double scaling){
        ResponseEntity<String> itemR ;
        Path item;
        if (width == 0 && height == 0) {
            item = bankService.loadAvatar(bank);
        }else{
            item = bankService.loadAvatar(bank,width,height,scaling);
        }

        if (item != null) {
            itemR = ResponseEntity.ok(item.toString());
        }else{
            itemR = ResponseEntity.status(200).body("");
        }
        return itemR;
    }

}
