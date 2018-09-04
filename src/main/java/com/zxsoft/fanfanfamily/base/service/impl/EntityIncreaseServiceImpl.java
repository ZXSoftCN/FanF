package com.zxsoft.fanfanfamily.base.service.impl;

import com.zxsoft.fanfanfamily.base.domain.EntityIncrease;
import com.zxsoft.fanfanfamily.base.domain.vo.AvatorLoadFactor;
import com.zxsoft.fanfanfamily.base.repository.EntityIncreaseDao;
import com.zxsoft.fanfanfamily.base.service.EntityIncreaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
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
    public Path uploadAvatarExtend(EntityIncrease entityIncrease, String fileName, String postfix, byte[] bytes) {
        return null;
    }

    @Override
    public Path uploadAvatarExtend(EntityIncrease entityIncrease, MultipartFile file) {
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
}
