package me.demo.bean.entity;

import com.alibaba.fastjson.JSON;
import javax.persistence.AttributeConverter;

/**
 * @description
 * @date 2022/10/02
 **/
public class JpaConverterList implements AttributeConverter<Object, String> {

  @Override
  public String convertToDatabaseColumn(Object object) {
//    if (Objects.isNull(object)) {
//      return null;
//    }
    return JSON.toJSONString(object);
  }

  @Override
  public Object convertToEntityAttribute(String s) {
    return JSON.parseArray(s);
  }
}
