package com.zxsoft.fanfanfamily.mort.service.impl;

import com.zxsoft.fanfanfamily.base.domain.mort.Bank;
import com.zxsoft.fanfanfamily.base.domain.vo.AvatorLoadFactor;
import com.zxsoft.fanfanfamily.base.service.impl.BaseServiceImpl;
import com.zxsoft.fanfanfamily.mort.repository.BankDao;
import com.zxsoft.fanfanfamily.mort.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class BankServiceImpl extends BaseServiceImpl<Bank> implements BankService {

    private final String resPathName = "bank";
    @Autowired
    private BankDao bankDao;

    //<editor-fold desc="私有方法">
    private void modifyIcon(Bank bank, Path path) {
        try {
            String strOld = bank.getIconUrl();
            if (bank.getIconUrl().startsWith("file:/")) {
                strOld = bank.getIconUrl().replaceFirst("file:/", "");
            }
            Path pathOld = Paths.get(strOld);
            Files.deleteIfExists(pathOld);

            bank.setIconUrl(path.toString());
            bankDao.save(bank);
        }catch (IOException ex){
            logger.error(String.format("%s Failed to store file:%s.%s",
                    this.getClass().getName(),ex.getMessage(), System.lineSeparator()));//System.lineSeparator()换行符
        }
    }

    //</editor-fold>
    
    @Override
    protected void initPath() {
        super.initPath();
        rootUploadPath = super.getPath().resolve(resPathName);
        avatarUploadPath = super.getPath().resolve(super.avatar);
    }
    
    @Override
    public JpaRepository<Bank, String> getBaseDao() {
        return bankDao;
    }

    @Override
    public Path uploadAvatarExtend(Bank bank, String fileName, String postfix, byte[] bytes) {
        Path itemNew = uploadAvatar(fileName,postfix, bytes);
        if (itemNew == null) {
            return null;
        }
        //将Path路径保存至Region的IconUrl属性
        modifyIcon(bank,itemNew);

        return itemNew;
    }

    @Override
    public Path loadAvatar(Bank bank, AvatorLoadFactor factor) {
        String strUrl = bank.getIconUrl();
        return loadAvatarInner(strUrl,factor);
    }

    @Override
    public Path uploadAvatarExtend(Bank bank, MultipartFile file) {
        Path itemNew = uploadAvatar(file);
        if (itemNew == null) {
            return null;
        }
        //将Path路径保存至Region的IconUrl属性
        modifyIcon(bank,itemNew);

        return itemNew;
    }
}
