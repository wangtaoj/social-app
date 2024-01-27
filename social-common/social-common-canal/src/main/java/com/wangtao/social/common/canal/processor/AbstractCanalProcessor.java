package com.wangtao.social.common.canal.processor;

import com.alibaba.otter.canal.protocol.FlatMessage;
import com.wangtao.social.common.canal.constant.CanalEventType;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;


/**
 * @author wangtao
 * Created at 2023/4/1 15:23
 */
public abstract class AbstractCanalProcessor<T> implements ICanalProcessor {

    private final Class<T> tClass;

    private CanalMessageParser canalMessageParser;

    @SuppressWarnings("unchecked")
    public AbstractCanalProcessor() {
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
            if (actualTypeArguments.length != 1) {
                throw new IllegalArgumentException("actual type argument count must equals 1");
            }
            this.tClass = (Class<T>)actualTypeArguments[0];
        } else {
            throw new IllegalArgumentException("genericSuperclass not be ParameterizedType instance!");
        }
    }

    public void setCanalMessageParser(CanalMessageParser canalMessageParser) {
        this.canalMessageParser = canalMessageParser;
    }

    public final void init(CanalMessageParser canalMessageParser) {
        this.setCanalMessageParser(canalMessageParser);
    }

    @Override
    public void process(FlatMessage message) {
        List<CanalResult<T>> canalResults = canalMessageParser.parse(message, tClass);
        CanalEventType eventType = CanalEventType.valueOf(message.getType());
        switch (eventType) {
            case INSERT:
                canalResults.forEach(this::doInsert);
                break;
            case UPDATE:
                canalResults.forEach(this::doUpdate);
                break;
            case DELETE:
                canalResults.forEach(this::doDelete);
                break;
            default:
                break;
        }
    }

    protected abstract void doInsert(CanalResult<T> canalResult);

    protected abstract void doUpdate(CanalResult<T> canalResult);

    protected abstract void doDelete(CanalResult<T> canalResult);
}
