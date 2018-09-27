package com.zxsoft.fanfanfamily.mort.domain.vo;

import com.zxsoft.fanfanfamily.base.domain.SimpleEntity;
import com.zxsoft.fanfanfamily.base.domain.mort.Bank;
import com.zxsoft.fanfanfamily.common.SpringUtil;
import com.zxsoft.fanfanfamily.mort.repository.BankDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.core.ReflectUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class BankWithChildDTO extends SimpleEntity {

    private String code;
    private String name;
    private String parentBankId;
    private String fullName;
    private String iconUrl;

    private List<BankWithChildDTO> subBanks = new ArrayList<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getParentBankId() {
        return parentBankId;
    }

    public void setParentBankId(String parentBankId) {
        this.parentBankId = parentBankId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public List<BankWithChildDTO> getSubBanks() {
        if (getId() != null) {
            BankDao bankDao = (BankDao) SpringUtil.getBean("bankDao");
            List<Bank> lstSub = bankDao.customQueryAllByParentBankId(getId());
            if (lstSub.size() > 0) {
                List<BankWithChildDTO> lstRlt = new ArrayList<>();
                lstSub.forEach(item -> lstRlt.add(BankWithChildDTO.convert(item)));
                return lstRlt;
            }
        }
        return null;
    }

    public void setSubBanks(List<BankWithChildDTO> subBanks) {
        this.subBanks = subBanks;
    }

    /**
     * 将bank转化成包含SubBanks的简易DTO。注意：parentBank是懒加载无法直接获取到ID，所以做了特别处理。
     * @param bank
     * @return
     */
    public static BankWithChildDTO convert(Bank bank) {
        BankWithChildDTO item = new BankWithChildDTO();
        PropertyDescriptor arrSrcProSet[] = ReflectUtils.getBeanSetters(bank.getClass());
        PropertyDescriptor arrDestProSet[] = ReflectUtils.getBeanSetters(item.getClass());

        try {
            for (int i = 0; i < arrSrcProSet.length; i++) {
                for (int j = 0; j < arrDestProSet.length; j++) {
                    if (arrSrcProSet[i].getName().equalsIgnoreCase(arrDestProSet[j].getName())) {
                        if (arrSrcProSet[i].getName().equalsIgnoreCase("parentBankId")) {
                            Bank parentBank = bank.getParentBank();
                            if (parentBank != null  && !StringUtils.isEmpty(parentBank.getId())) {
                                arrDestProSet[j].getWriteMethod().invoke(item, parentBank.toString());
                            }
                        } else {
                            Object srcValue = arrSrcProSet[i].getReadMethod().invoke(bank);
                            arrDestProSet[j].getWriteMethod().invoke(item,srcValue);
                        }
                    }
                }
            }
        } catch (InvocationTargetException ex) {
            item = null;
        }catch (IllegalAccessException ex) {
            item = null;
        }

        return item;
    }
}
