package joe.tags.services;

import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.google.common.collect.Lists;
import joe.elasticsearch.helper.EsHelper;
import joe.elasticsearch.model.querys.GeneralQueryAggReq;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * @Project: joe
 * @Package joe.tag.services
 * @note: ES 通用查询聚合服务层
 * @date Date : 2018年09月27日 17:04
 */
@Service
public class GeneralEsService {

    private final static Logger LOGGER = LoggerFactory.getLogger(GeneralEsService.class);

    private final static String INDEX_KEY = "Joe.es.tags.index";
    @Value("${" + INDEX_KEY + ":customertags}")
    private String customerTags;

    @ApolloConfigChangeListener
    public void listen(ConfigChangeEvent changeEvent) {
        if (changeEvent.isChanged(INDEX_KEY)) {
            final ConfigChange change = changeEvent.getChange(INDEX_KEY);
            LOGGER.info("{} is change to {}, ole value is {} ", INDEX_KEY, change.getNewValue(), change.getOldValue());
            customerTags = change.getNewValue();
        }
    }

    public Map<String, Object> handler(GeneralQueryAggReq generalQueryAggReq) {
        if (CollectionUtils.isEmpty(generalQueryAggReq.getIndices())) {
            generalQueryAggReq.setIndices(Lists.newArrayList(customerTags));
        }
        return EsHelper.execute(generalQueryAggReq);
    }
}
