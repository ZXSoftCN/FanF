package com.zxsoft.fanfanfamily.mort.service.impl;

import com.zxsoft.fanfanfamily.base.domain.BaseEntity;
import com.zxsoft.fanfanfamily.base.domain.Region;
import com.zxsoft.fanfanfamily.base.domain.mort.Bank;
import com.zxsoft.fanfanfamily.base.domain.vo.AvatorLoadFactor;
import com.zxsoft.fanfanfamily.base.service.impl.BaseServiceImpl;
import com.zxsoft.fanfanfamily.mort.domain.vo.BankWithChildDTO;
import com.zxsoft.fanfanfamily.mort.repository.BankDao;
import com.zxsoft.fanfanfamily.mort.repository.RegionDao;
import com.zxsoft.fanfanfamily.mort.service.BankService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BankServiceImpl extends BaseServiceImpl<Bank> implements BankService {

    private final String resPathName = "bank";

    //静态变量存储编码流水号最大值
    private static final AtomicInteger atomicCodeNum = new AtomicInteger();

    @Autowired
    private BankDao bankDao;
    @Autowired
    private RegionDao regionDao;


    //<editor-fold desc="私有方法">
    private void modifyIcon(Bank bank, String path) {
        if (!StringUtils.isEmpty(bank.getIconUrl()) && !IsExistsSharedPath(bank.getId(), bank.getIconUrl())) {
            deleteInnertFile(bank.getIconUrl());
        }

        bank.setIconUrl(StringUtils.join("/",path));
        bankDao.save(bank);
    }

    //</editor-fold>

    //bank 编码和图标附件处理
    /**
     * @Author  javaloveiphone
     * @Description :初始化设置银行编码流水号最大值
     * @throws Exception
     * void
     */
    @PostConstruct
    public void initMax(){
        initCodeNumMax();
    }

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
    public String getEntityName() {
        return resPathName;
    }

    @Override
    public AtomicInteger getCodeNumMax() {
        return atomicCodeNum;
    }
    //endbank


    @Override
    public String uploadAvatarExtend(Bank bank, String fileName, String postfix, byte[] bytes) {
        String itemNew = uploadAvatar(fileName,postfix, bytes);
        if (itemNew == null) {
            return null;
        }
        //将Path路径保存至Bank的IconUrl属性
        modifyIcon(bank,itemNew);

        return itemNew;
    }

    @Override
    public Path loadAvatar(Bank bank, AvatorLoadFactor factor) {
        String strUrl = bank.getIconUrl();
        return loadAvatarInner(strUrl,factor);
    }

    @Override
    public String uploadAvatarExtend(Bank bank, MultipartFile file) {
        String itemNew = uploadAvatar(file);
        if (itemNew == null) {
            return null;
        }
        //将Path路径保存至Bank的IconUrl属性
        modifyIcon(bank,itemNew);

        return itemNew;
    }

    private void fetchSubBanks(BankWithChildDTO bank) {
        if (bank.getSubBanks() != null && bank.getSubBanks().size() > 0) {
            for (BankWithChildDTO item : bank.getSubBanks()) {
                fetchSubBanks(item);
            }
        }
    }
    @Override
    public List<BankWithChildDTO> queryTree() {
        List<BankWithChildDTO> lstRlt = new ArrayList<>();
        List<Bank> topBanks = bankDao.findAllByParentBankIsNullOrderByCode();
        for (Bank item : topBanks) {
            BankWithChildDTO dtoItem = BankWithChildDTO.convert(item);
            fetchSubBanks(dtoItem);
            lstRlt.add(dtoItem);
        }
        return lstRlt;
    }

    @Override
    public List<Bank> querySubs(String id) {
        return bankDao.customQueryAllByParentBankId(id);
    }

    public boolean IsExistsSharedPath(String currentId, String path) {
        List<Bank> lstBanks = bankDao.findAllByIconUrlContaining(path);
        List<Bank> lstOthers = bankDao.findAllByIconUrlContainingAndIdNot(path,currentId);
        //移除当前实体
        lstBanks.removeIf(item -> !StringUtils.isEmpty(currentId) && currentId.equalsIgnoreCase(item.getId()));
        if (lstBanks.size() > 0) {
            //还存在其他共享项
            return true;
        }
        return false;
    }

    @Override
    public Optional<Bank> getById(String id) {
        return bankDao.queryBankById(id);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public Bank modify(Bank bank) {
        try {
//            entityManagerUtil.getEntityManager().persist(bank);
            Bank bankWrap = bankDao.queryBankById(bank.getId()).get();//将detach实体持久化
            //移除区域
            for (Region item : bankWrap.getRegions()) {
                if (!bank.getRegions().contains(item)) {
                    item.removeBank(bankWrap);
                    regionDao.save(item);
                }
            }
            //加入区域
            for (Region item : bank.getRegions()) {
                Region itemWrap = regionDao.queryRegionById(item.getId());

                if (!itemWrap.getBanks().contains(bankWrap)) {
                    /*直接加入bank，提示Multiple representations of the same entity.
                     配置hibernate.event.merge.entity_copy_observer=allow无效*/
                    itemWrap.addBank(bankWrap);
                    regionDao.save(itemWrap);
                }
            }

            return super.modify(bank);
        } catch (Exception ex) {
            logger.error(String.format("银行【%s】更新保存失败：%s",bank.getName(), ex.getMessage()));
        }
        return null;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public Bank add(Bank bank) {
        try {
            //加入区域
            Set<Region> arrRegion = bank.getRegions();
            for (Region item : bank.getRegions()) {
                Region itemWrap = regionDao.queryRegionById(item.getId());
                arrRegion.add(itemWrap);
            }
            bank.getRegions().clear();
//            for (Region item : bank.getRegions()) {
//                entityManagerUtil.getEntityManager().persist(item);
//                Region itemWrap = regionDao.queryRegionById(item.getId());
//
//                if (!itemWrap.getBanks().contains(bank)) {
//                    itemWrap.addBank(bank);
//                    arrRegion.add(itemWrap);
////                    regionDao.save(itemWrap);
//                }
//            }

            super.add(bank);
            if (arrRegion.size() > 0) {
                bank.setRegions(arrRegion);
                modify(bank);
            }

            return bank;
        } catch (Exception ex) {
            logger.error(String.format("银行【%s】更新保存失败：%s",bank.getName(), ex.getMessage()));
        }
        return null;
    }
}
