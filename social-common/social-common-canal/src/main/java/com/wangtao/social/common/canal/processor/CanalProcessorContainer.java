package com.wangtao.social.common.canal.processor;

import com.wangtao.social.common.canal.constant.CanalTableModel;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author wangtao
 * Created at 2023/4/1 15:38
 */
@Component
public class CanalProcessorContainer implements SmartInitializingSingleton, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private final ConcurrentMap<CanalTableModel, ICanalProcessor> cache = new ConcurrentHashMap<>();

    public void register(CanalTableModel tableModel, ICanalProcessor canalProcessor) {
        cache.put(tableModel, canalProcessor);
    }

    public ICanalProcessor get(CanalTableModel tableModel) {
        ICanalProcessor canalProcessor = cache.get(tableModel);
        if (canalProcessor == null) {
            throw new IllegalArgumentException(tableModel.toString());
        }
        return canalProcessor;
    }

    @Override
    public void afterSingletonsInstantiated() {
        @SuppressWarnings({"rawtypes"})
        Map<String, AbstractCanalProcessor> canalProcessorMap = applicationContext.getBeansOfType(AbstractCanalProcessor.class);
        CanalMessageParser canalMessageParser = applicationContext.getBean(CanalMessageParser.class);
        canalProcessorMap.forEach((k, processor) -> {
            processor.init(canalMessageParser);
            this.register(processor.support(), processor);
        });
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
