package me.demo.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;

/**
 * Jackson utils
 *
 * @date 2022/10/15
 */
@Slf4j
public class JacksonUtils {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  static {
    MAPPER.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    MAPPER.setSerializationInclusion(Include.NON_NULL);
    // 缩进排版
    MAPPER.configure(SerializationFeature.INDENT_OUTPUT, false);
    MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    MAPPER.configure(Feature.ALLOW_SINGLE_QUOTES, true);

    // 属性排序
    MAPPER.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
    MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  /**
   * 将实体对象转换为json字符串
   *
   * @param entity 实体对象
   * @param <T> 泛型
   * @return json string
   * @throws JsonProcessingException
   */
  public static <T> String obj2json(T entity) throws JsonProcessingException {
    return MAPPER.writeValueAsString(entity);
  }

  /**
   * 将实体对象转换为json字符串
   *
   * @param entity 实体对象
   * @param pretty 是否转换为美观格式
   * @param <T> 泛型
   * @return json string
   * @throws JsonProcessingException
   */
  public static <T> String obj2json(T entity, boolean pretty) throws JsonProcessingException {
    if (pretty) {
      return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(entity);
    }
    return MAPPER.writeValueAsString(entity);
  }

  /**
   * 将实体对象转换为字节数组
   *
   * @param entity 实体对象
   * @param <T> 泛型
   * @return json string
   * @throws JsonProcessingException
   */
  public static <T> byte[] obj2jsonBytes(T entity) throws JsonProcessingException {
    return MAPPER.writeValueAsBytes(entity);
  }

  /**
   * 将实体类转换为JsonNode对象
   *
   * @param entity 实体对象
   * @param <T> 泛型
   * @return JsonNode
   */
  public static <T> JsonNode obj2node(T entity) {
    return MAPPER.valueToTree(entity);
  }

  /**
   * 将json字符串转换为实体类对象
   *
   * @param json json字符串
   * @param type 实体对象类型
   * @param <T> 泛型
   * @return 转换成功后的对象
   * @throws JsonProcessingException
   */
  public static <T> T json2obj(String json, Class<T> type) throws JsonProcessingException {
    return MAPPER.readValue(json, type);
  }

  /**
   * 泛化转换方式
   *
   * <p>example：
   *
   * <p>{@code Map<String, List<UserInfo>> listMap = genericConvert(jsonStr, new
   * TypeReference<Map<String, List<UserInfo>>>() {});}
   *
   * @param json json字符串
   * @param type type
   * @param <T> 泛化
   * @return T
   * @throws JsonProcessingException
   */
  public static <T> T genericConvert(String json, TypeReference<T> type)
      throws JsonProcessingException {
    return MAPPER.readValue(json, type);
  }

  /**
   * 将json字符串转换为map
   *
   * @param json json字符串
   * @return Map
   * @throws JsonProcessingException
   */
  public static Map<String, Object> json2map(String json) throws JsonProcessingException {
    return genericConvert(json, new TypeReference<Map<String, Object>>() {});
  }

  /**
   * 将map转换为实体类对象
   *
   * @param map map
   * @param type 实体对象类型
   * @param <T> 泛型
   * @return 实体对象
   */
  public static <T> T map2obj(Map map, Class<T> type) {
    return MAPPER.convertValue(map, type);
  }

  /**
   * 将json字符串转换为实体类集合
   *
   * @param json json字符串
   * @param type 实体对象类型
   * @param <T> 泛型
   * @return list集合
   * @throws JsonProcessingException
   */
  public static <T> List<T> json2list(String json, Class<T> type) throws JsonProcessingException {
    CollectionType collectionType =
        MAPPER.getTypeFactory().constructCollectionType(List.class, type);
    return MAPPER.readValue(json, collectionType);
  }

  /**
   * 将json字符串转换为JsonNode对象
   *
   * @param json json字符串
   * @return JsonNode对象
   * @throws JsonProcessingException
   */
  public static JsonNode json2node(String json) throws JsonProcessingException {
    return MAPPER.readTree(json);
  }

  /**
   * 检查字符串是否是json格式
   *
   * @param str 待检查字符串
   * @return 是：true，否：false
   */
  public static boolean isJsonString(String str) {
    try {
      MAPPER.readTree(str);
      return true;
    } catch (Exception e) {
      log.info("[isJsonString]-检查字符串是否是json格式...{}", e.getMessage());
      return false;
    }
  }
}
