package com.wangtao.social.common.canal.processor;

import com.alibaba.otter.canal.protocol.FlatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangtao.social.common.canal.constant.CanalEventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangtao
 * Created at 2023/4/1 15:38
 */
@Component
public class CanalMessageParser {

    @Autowired
    private ObjectMapper objectMapper;

    public <T> List<CanalResult<T>> parse(FlatMessage flatMessage, Class<T> clazz) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<Map<String, String>> dataList = flatMessage.getData();
        List<Map<String, String>> oldDataList = flatMessage.getOld();
        for (int i = 0; i < dataList.size(); i++) {
            Map<String, String> data = dataList.get(i);
            Map<String, Object> canalResultModel = new HashMap<>();
            canalResultModel.put("canalEventType", flatMessage.getType());
            canalResultModel.put("afterData", data);
            if (CanalEventType.UPDATE.getType().equals(flatMessage.getType())) {
                canalResultModel.put("beforeData", oldDataList.get(i));
            }
            list.add(canalResultModel);
        }
        try {
            String json = objectMapper.writeValueAsString(list);
            JavaType tempJavaType = objectMapper.getTypeFactory().constructParametricType(CanalResult.class, clazz);
            JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, tempJavaType);
            return objectMapper.readValue(json, javaType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
