package com.zinnaworks.deploy.util;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

//提供将java对象转化成json结构
public final class JsonUtil{
  private static final ObjectMapper objectMapper=new ObjectMapper();
  private static final JsonUtil INSTANCE=new JsonUtil(); 
  private JsonUtil(){}
  public static JsonUtil getInstance(){
      return INSTANCE;
  }
  static{
      SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期输出格式
      objectMapper.setDateFormat(dateFormat);
      //设置将对象转换成JSON字符串时候:包含的属性不能为空或"";
      //Include.Include.ALWAYS 默认
      //Include.NON_DEFAULT 属性为默认值不序列化
      //Include.NON_EMPTY 属性为 空（""） 或者为NULL都不序列化
      //Include.NON_NULL 属性为NULL 不序列化
      //objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);//序列化的时候序列化全部的属性
      objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
      //反序列化的时候如果多了其他属性,不抛出异常
      objectMapper.enable(SerializationFeature.INDENT_OUTPUT);//转化成格式化的json
      objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
      //是否允许一个类型没有注解表明打算被序列化。默认true，抛出一个异常；否则序列化一个空对象，比如没有任何属性。
      objectMapper.disable(SerializationFeature.FLUSH_AFTER_WRITE_VALUE);
      //该特性决定是否在writeValue()方法之后就调用JsonGenerator.flush()方法。
      //当我们需要先压缩，然后再flush，则可能需要false。
  }

   /**
   * @ 将对象序列化
   * @ param obj
   * @ return byte[]
   * @ create by ostreamBaba on 上午12:35 18-9-18
   */

  public static <T> byte[] serialize(T obj){
      byte[] bytes;
      try{
          bytes=objectMapper.writeValueAsBytes(obj);
      }catch (JsonProcessingException e){
          throw new IllegalStateException(e.getMessage(),e);
      }
      return bytes;
  }

  /**
   * @ 反序列成对象
   * @ param data
   * @ param clazz
   * @ return T
   * @ create by ostreamBaba on 上午12:35 18-9-18
   */

  public static <T> T deserialize(byte[] data,Class<T> clazz){
      T obj;
      try{
          obj=objectMapper.readValue(data,clazz);
      } catch (IOException e) {
          throw new IllegalStateException(e.getMessage(),e);
      }
      return obj;
  }

  /**
   * @ 将json转化成对象
   * @ param json
   * @ param clazz
   * @ return T
   * @ create by ostreamBaba on 上午12:37 18-9-18
   */

  public static <T> T jsonToObject(String json,Class<?> clazz){
      T obj;
      JavaType javaType=objectMapper.getTypeFactory().constructType(clazz);
      try{
          obj=objectMapper.readValue(json,javaType);
      } catch (IOException e) {
         throw new IllegalStateException(e.getMessage(),e);
      }
      return obj;
  }

  /**
   * @ 将json转化成list
   * @ param json
   * @ param collectionClass
   * @ param elementClass
   * @ return T
   * @ create by ostreamBaba on 上午12:38 18-9-18
   */

  public static <T> T jsonToObjectList(String json,Class<?> collectionClass,Class<?>... elementClass){
      T obj;
      JavaType javaType=objectMapper.getTypeFactory().constructParametricType(collectionClass,elementClass);
      try{
          obj=objectMapper.readValue(json,javaType);
      }catch (IOException e){
          throw new IllegalStateException(e.getMessage(),e);
      }
      return obj;
  }

  /**
   * @ 将json转化成Map
   * @ param json
   * @ param keyClass
   * @ param valueClass
   * @ return T
   * @ create by ostreamBaba on 上午12:38 18-9-18
   */

  public static <T> T jsonToObjectHashMap(String json,Class<?> keyClass,Class<?> valueClass){
      T obj;
      JavaType javaType=objectMapper.getTypeFactory().constructParametricType(HashMap.class,keyClass,valueClass);
      try {
          obj=objectMapper.readValue(json,javaType);
      }catch (IOException e){
          throw new IllegalStateException(e.getMessage(),e);
      }
      return obj;
  }

  /**
   * @ 将对象转化成json
   * @ param obj
   * @ return java.lang.String
   * @ create by ostreamBaba on 上午12:39 18-9-18
   */

  public static String objectToJson(Object obj){
      String json;
      try{
          json=objectMapper.writeValueAsString(obj);
      }catch (IOException e){
          throw new IllegalStateException(e.getMessage(),e);
      }
      return json;
  }
}
