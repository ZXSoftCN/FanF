package com.zxsoft.fanfanfamily.config.converter;

import com.zxsoft.fanfanfamily.base.domain.Region;
import com.zxsoft.fanfanfamily.mort.repository.RegionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StringToRegion implements Converter<String,Region> {

    @Autowired
    private RegionDao regionDao;

    @Override
    public Region convert(String s) {
        Optional<Region> regItem = regionDao.findById(s);
        Optional<Region> regItemByCode = regionDao.findFirstByCode(s);
        if (regItem.isPresent()) {
            return regItem.get();
        }
        return regItem.orElse(regItemByCode.orElse(null));
    }
}
