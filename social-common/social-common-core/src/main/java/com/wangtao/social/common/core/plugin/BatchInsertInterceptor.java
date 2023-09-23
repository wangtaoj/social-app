package com.wangtao.social.common.core.plugin;

import org.apache.commons.collections4.ListUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * 针对SQL insert into values(), ()语句, 为了避免大量数据使得SQL语句超长, 分批次插入
 * 使用方式: 插入集合的name为_batchList, 最大的数目name为_maxCount
 * <pre> {@code
 * int batchInsert(@Param("_batchList")List<User> users, @Param("_maxCount")int maxCount);
 * }</pre>
 *
 * @author wangtao
 * Created at 2023-09-23
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class BatchInsertInterceptor implements Interceptor {

    /**
     * 参数名字
     */
    public static final String DEFAULT_DATA_NAME = "_batchList";

    /**
     * 一次插入的最大条数参数名
     */
    public static final String DEFAULT_MAX_COUNT_NAME = "_maxCount";

    private static final int DEFAULT_MAX_COUNT = 100;

    private final Logger logger = LoggerFactory.getLogger(BatchInsertInterceptor.class);

    private String dataName = DEFAULT_DATA_NAME;

    private String maxCountName = DEFAULT_MAX_COUNT_NAME;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];
        Executor executor = (Executor) invocation.getTarget();
        Configuration configuration = ms.getConfiguration();
        MetaObject metaObject = configuration.newMetaObject(parameter);
        if (metaObject.hasGetter(dataName)) {
            Object dataList = metaObject.getValue(dataName);
            int maxCount = DEFAULT_MAX_COUNT;
            if (metaObject.hasGetter(maxCountName)) {
                maxCount = (Integer) metaObject.getValue(maxCountName);
            }
            if (dataList instanceof List) {
                @SuppressWarnings({"unchecked"})
                List<Object> list = (List<Object>) dataList;
                if (list.size() > maxCount) {
                    logger.info("================proxy executor.update method.========");
                    return batchInsertData(executor, ms, metaObject, list, maxCount);
                }
            }
        }
        // not proxy
        return invocation.proceed();
    }

    private Object batchInsertData(Executor executor, MappedStatement ms, MetaObject metaObject,
                                   List<Object> dataList, int maxCount) throws SQLException {
        int updateCount = 0;
        List<List<Object>> partitionList = ListUtils.partition(dataList, maxCount);
        for (List<Object> subList : partitionList) {
            metaObject.setValue(dataName, subList);
            updateCount += executor.update(ms, metaObject.getOriginalObject());
        }
        // 还原参数
        metaObject.setValue(dataName, dataList);
        return updateCount;
    }

    @Override
    public void setProperties(Properties properties) {
        if (properties != null) {
            this.dataName = properties.getProperty("dataName", DEFAULT_DATA_NAME);
            this.maxCountName = properties.getProperty("maxCountName", DEFAULT_MAX_COUNT_NAME);
        }
    }
}
