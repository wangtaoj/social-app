package com.wangtao.social.square.canal.processor;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.wangtao.social.common.canal.constant.CanalTableModel;
import com.wangtao.social.common.canal.processor.AbstractCanalProcessor;
import com.wangtao.social.common.canal.processor.CanalResult;
import com.wangtao.social.common.core.enums.ResponseEnum;
import com.wangtao.social.common.core.exception.BusinessException;
import com.wangtao.social.common.es.constant.EsIndexEnum;
import com.wangtao.social.common.es.model.EsPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author wangtao
 * Created at 2024-01-27
 */
@Component
public class PostCanalProcessor extends AbstractCanalProcessor<EsPost> {

    @Autowired
    private ElasticsearchClient esClient;

    @Override
    protected void doInsert(CanalResult<EsPost> canalResult) {
        EsPost esPost = canalResult.getAfterData();
        try {
            esClient.create(builder -> builder
                    .index(EsIndexEnum.POST.getName())
                    .id(String.valueOf(esPost.getId()))
                    .document(esPost)
            );
        } catch (IOException e) {
            throw new BusinessException(ResponseEnum.ES_SYNC_FAIL, "es新增帖子异常, id: " + esPost.getId(), e);
        }
    }

    @Override
    protected void doUpdate(CanalResult<EsPost> canalResult) {

    }

    @Override
    protected void doDelete(CanalResult<EsPost> canalResult) {
        EsPost esPost = canalResult.getAfterData();
        try {
            esClient.delete(builder -> builder
                    .index(EsIndexEnum.POST.getName())
                    .id(String.valueOf(esPost.getId()))
            );
        } catch (IOException e) {
            throw new BusinessException(ResponseEnum.ES_SYNC_FAIL, "es删除帖子异常, id: " + esPost.getId(), e);
        }
    }

    @Override
    public CanalTableModel support() {
        return CanalTableModel.SS_POST;
    }
}
