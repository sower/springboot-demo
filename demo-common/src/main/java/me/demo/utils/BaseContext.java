package me.demo.utils;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/**
 * 基础上下文
 *
 * @date 2023/02/26
 */
@Data
public class BaseContext {

  private Map<Object, Object> properties = new HashMap<>();

  public Object getProperty(Object var) {
    return this.properties.get(var);
  }

  public void addProperty(Object varKey, Object varValue) {
    this.properties.put(varKey, varValue);
  }

  public void removeProperty(Object var) {
    this.properties.remove(var);
  }

  public void removeAllProperties() {
    this.properties.clear();
  }
}
