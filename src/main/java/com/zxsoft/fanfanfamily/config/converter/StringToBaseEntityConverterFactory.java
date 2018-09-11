package com.zxsoft.fanfanfamily.config.converter;

import com.zxsoft.fanfanfamily.base.domain.BaseEntity;
import com.zxsoft.fanfanfamily.base.domain.Region;
import com.zxsoft.fanfanfamily.base.repository.BaseEntityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StringToBaseEntityConverterFactory implements ConverterFactory<String,BaseEntity> {


    @Override
    public <T extends BaseEntity> Converter<String, T> getConverter(Class<T> Class) {

        if (Class == Region.class) {
            return (Converter<String, T>) new StringToRegion();
        }
        return new StringToBaseEntityConverter<>(Class);
    }

    private final class StringToBaseEntityConverter<T extends BaseEntity> implements Converter<String, T> {

        private Class<T> entityType;

        @Autowired
        private BaseEntityDao entityDao;

        public StringToBaseEntityConverter(Class<T> type){
            entityType =type;
        }

        @Override
        public T convert(String s) {
            Optional destObj = entityDao.findById(s);
            if (destObj.isPresent()) {
                /*
                    这里如何通过Class<T>目标实体的类型和Id获取到对应的实体对象。
                    这里无法得到对应的Dao类进行检索。
                 */
                try {
                } catch (Exception ex) {

                }
            }
            return null;
        }
    }


    private final class StringToEnumConverter<T extends Enum> implements Converter<String, T> {
        private Class<T> enumType;

        public StringToEnumConverter(Class<T> enumType) {
            this.enumType = enumType;
        }

        public T convert(String source) {


            return (T) Enum.valueOf(this.enumType, source.trim());
        }
    }
}
