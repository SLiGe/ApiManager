package cn.zjiali.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author zJiaLi
 * @since 2021-12-04 23:44
 */
@Component
public class JsonUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;
    private static ObjectMapper objectMapper;
    private static final MapTypeReference MAP_TYPE = new MapTypeReference();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        JsonUtil.applicationContext = applicationContext;
        JsonUtil.objectMapper = applicationContext.getBean(ObjectMapper.class);
    }


    public static String toJson(Object data) throws JsonProcessingException {
        return objectMapper.writeValueAsString(data);
    }

    public static Map<String, String> parseMap(InputStream json) throws IOException {
        return objectMapper.readValue(json, MAP_TYPE);
    }


    private static class MapTypeReference extends TypeReference<Map<String, String>> {

    }
}
