package com.zxsoft.fanfanfamily.base.service.impl;

import com.zxsoft.fanfanfamily.base.domain.EntityIncrease;
import com.zxsoft.fanfanfamily.base.domain.vo.AvatorLoadFactor;
import com.zxsoft.fanfanfamily.base.repository.EntityIncreaseDao;
import com.zxsoft.fanfanfamily.base.service.EntityIncreaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class EntityIncreaseServiceImpl extends BaseServiceImpl<EntityIncrease> implements EntityIncreaseService {

    @Autowired
    private EntityIncreaseDao entityIncreaseDao;

    @Override
    public JpaRepository<EntityIncrease, String> getBaseDao() {
        return entityIncreaseDao;
    }

    @Override
    public Path loadAvatar(EntityIncrease entityIncrease, AvatorLoadFactor factor) {
        return null;
    }

    @Override
    public String uploadAvatarExtend(EntityIncrease entityIncrease, String fileName, String postfix, byte[] bytes) {
        return null;
    }

    @Override
    public String uploadAvatarExtend(EntityIncrease entityIncrease, MultipartFile file) {
        return null;
    }

    @Override
    public Optional<EntityIncrease> findByEntityName(String entityName) {
        return entityIncreaseDao.findFirstByEntityNameEquals(entityName);
    }

    @Override
    public int getSortNoMaxPlus(String entityName) {
        return 0;
    }

    @Override
    public int getSortNoMaxPlus(EntityIncrease t) {
        return 0;
    }

    @Override
    public Page<EntityIncrease> findEntityIncreaseByName(String name, Pageable pageable) {
        Page<EntityIncrease> infos = entityIncreaseDao.findEntityIncreaseByNameContaining(name,  pageable);
        return infos;
    }

    @Override
    public void updateCodeMaxNum(String entityName,String code) {
        Optional<EntityIncrease> itemOp = entityIncreaseDao.findFirstByEntityNameIgnoreCase(entityName);
        if (!itemOp.isPresent()) {
            logger.warn(String.format("已【%s】更新编码规则失败，没有找到规则中的实体名【%s】。",code,entityName));
            return;
        }
        EntityIncrease item = itemOp.get();
        String[] strSplit = StringUtils.split(code,item.getSeparate());
        if (strSplit.length < 1) {
            logger.warn(String.format("更新编码规则【%s】失败：编码【%s】使用【%s】分隔符无法分隔出编码流水号。",
                    entityName,code,item.getSeparate()));
            return;
        }
        String lastCodeNum = "";
        if (strSplit.length > 1) {
            lastCodeNum = strSplit[strSplit.length - 1];
        } else{
            lastCodeNum = strSplit[0];
        }
        if (StringUtils.isEmpty(lastCodeNum)) {
            logger.warn(String.format("更新编码规则【%s】失败：编码【%s】分隔出来的当前流水号为空字符串。",entityName,code));
            return;
        }
        try {
            Integer codeMax = Integer.parseInt(lastCodeNum);
            entityIncreaseDao.updateCodeNumMax(entityName,codeMax);
        } catch (Exception ex) {
            logger.error(String.format("更新编码规则【%s】失败：编码【%s】分隔出来的当前流水号【%s】无法转换成Int型",
                    entityName,code,lastCodeNum));
            return;
        }
    }
}
