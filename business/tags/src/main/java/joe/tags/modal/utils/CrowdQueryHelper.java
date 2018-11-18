package joe.tags.modal.utils;

import joe.elasticsearch.helper.EsQueryHelper;
import joe.tags.modal.bean.request.CrowdQueryModel;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * @Project: basic
 * @Package joe.tag.modal.utils
 * @note: 用于生成 QueryBuilder
 * @date Date : 2018年09月06日 16:00
 */
public class CrowdQueryHelper {

    /**
     * ES 查询模型生成
     *
     * @param crowdQueryModels 人群详情查询模型
     * @return ES 查询模型
     */
    public static BoolQueryBuilder create(List<CrowdQueryModel> crowdQueryModels) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        for (CrowdQueryModel crowdQueryModel : crowdQueryModels) {
            switch (crowdQueryModel.getFormat()) {
                case DATE:
                    createDate(boolQuery, crowdQueryModel);
                    break;
                case VALUE:
                    createValue(boolQuery, crowdQueryModel);
                    break;
                case BOOLEAN:
                    createBoolean(boolQuery, crowdQueryModel);
                    break;
                case STRING:
                    createString(boolQuery, crowdQueryModel);
                    break;
                case ENUM:
                    createEnum(boolQuery, crowdQueryModel);
                    break;
                default:
                    throw new IllegalArgumentException("非合法参数");
            }
        }
        return boolQuery;
    }

    /**
     * ES 字符串类查询模型生成
     *
     * @param boolQuery       基础查询模型
     * @param crowdQueryModel 人群详情查询模型
     */
    private static void createString(BoolQueryBuilder boolQuery, CrowdQueryModel crowdQueryModel) {
        String tagName = crowdQueryModel.getField();
        Consumer<CrowdQueryModel.DataMetric> dataMetricConsumer = dataMetric -> {
            switch (dataMetric.getMetric()) {
                case EQ:
                case PARTICIPLE:
                    boolQuery.must(QueryBuilders.matchQuery(tagName, dataMetric.getValues()));
                    break;
                case NE:
                    boolQuery.mustNot(QueryBuilders.termsQuery(tagName, dataMetric.getValues()));
                    break;
                case MATCH_END:
                    boolQuery.must(QueryBuilders.wildcardQuery(tagName, dataMetric.getValues().get(0) + "*"));
                    break;
                case MATCH_MID:
                    boolQuery.must(QueryBuilders.wildcardQuery(tagName, dataMetric.getLeft() + "*" + dataMetric.getRight()));
                    break;
                case MATCH_FRONT:
                    boolQuery.must(QueryBuilders.wildcardQuery(tagName, "*" + dataMetric.getValues().get(0)));
                    break;
                case MATCH_FRONT_END:
                    boolQuery.must(QueryBuilders.wildcardQuery(tagName, "*" + dataMetric.getValues().get(0) + "*"));
                    break;
                case MATCH_FRONT_MID_END:
                    boolQuery.must(QueryBuilders.wildcardQuery(tagName, "*" + dataMetric.getLeft() + "*" + dataMetric.getRight() + "*"));
                    break;
                default:
                    EsQueryHelper.error();
            }
        };
        crowdQueryModel.getDataMetrics()
                       .parallelStream()
                       .forEach(dataMetricConsumer);
    }

    /**
     * ES 布尔类查询模型生成
     *
     * @param boolQuery       基础查询模型
     * @param crowdQueryModel 查询模型
     */
    private static void createBoolean(BoolQueryBuilder boolQuery, CrowdQueryModel crowdQueryModel) {
        createEnum(boolQuery, crowdQueryModel);
    }

    /**
     * ES 枚举类查询模型生成
     *
     * @param boolQuery       基础查询模型
     * @param crowdQueryModel 查询模型
     */
    private static void createEnum(BoolQueryBuilder boolQuery, CrowdQueryModel crowdQueryModel) {
        boolQuery.must(QueryBuilders.termsQuery(crowdQueryModel.getField(), crowdQueryModel.getDataMetrics().get(0).getValues()));
    }

    /**
     * ES 数值类查询模型生成
     *
     * @param boolQuery       基础查询模型
     * @param crowdQueryModel 查询模型
     */
    private static void createValue(BoolQueryBuilder boolQuery, CrowdQueryModel crowdQueryModel) {
        createDate(boolQuery, crowdQueryModel);
    }

    /**
     * ES 时间日期类查询模型生成
     *
     * @param boolQuery       基础查询模型
     * @param crowdQueryModel 查询模型
     */
    private static void createDate(BoolQueryBuilder boolQuery, CrowdQueryModel crowdQueryModel) {
        String tagName = crowdQueryModel.getField();
        Consumer<CrowdQueryModel.DataMetric> dataMetricConsumer = dataMetric -> {
            BoolQueryBuilder bool = queryQueryBuilder(boolQuery, tagName);
            switch (dataMetric.getMetric()) {
                case EQ:
                    bool.should(QueryBuilders.termsQuery(tagName, dataMetric.getValues()));
                    break;
                case NE:
                    bool.mustNot(QueryBuilders.termsQuery(tagName, dataMetric.getValues()));
                    break;
                case GT:
                    bool.should(QueryBuilders.rangeQuery(tagName).gt(dataMetric.getLeft()));
                    break;
                case GTE:
                    bool.should(QueryBuilders.rangeQuery(tagName).gte(dataMetric.getLeft()));
                    break;
                case LT:
                    bool.should(QueryBuilders.rangeQuery(tagName).lt(dataMetric.getLeft()));
                    break;
                case LTE:
                    bool.should(QueryBuilders.rangeQuery(tagName).lte(dataMetric.getLeft()));
                    break;
                case GT_LT:
                    bool.should(QueryBuilders.rangeQuery(tagName).gt(dataMetric.getLeft()).lt(dataMetric.getRight()));
                    break;
                case GTE_LT:
                    bool.should(QueryBuilders.rangeQuery(tagName).gte(dataMetric.getLeft()).lt(dataMetric.getRight()));
                    break;
                case GT_LTE:
                    bool.should(QueryBuilders.rangeQuery(tagName).gt(dataMetric.getLeft()).lte(dataMetric.getRight()));
                    break;
                case GTE_LTE:
                    bool.should(QueryBuilders.rangeQuery(tagName).gte(dataMetric.getLeft()).lte(dataMetric.getRight()));
                    break;
                case LT_GT:
                    bool.should(QueryBuilders.boolQuery()
                                             .should(QueryBuilders.rangeQuery(tagName).lt(dataMetric.getLeft()))
                                             .should(QueryBuilders.rangeQuery(tagName).gt(dataMetric.getRight())));
                    break;
                case LTE_GT:
                    bool.should(QueryBuilders.boolQuery()
                                             .should(QueryBuilders.rangeQuery(tagName).lte(dataMetric.getLeft()))
                                             .should(QueryBuilders.rangeQuery(tagName).gt(dataMetric.getRight())));
                    break;
                case LT_GTE:
                    bool.should(QueryBuilders.boolQuery()
                                             .should(QueryBuilders.rangeQuery(tagName).lt(dataMetric.getLeft()))
                                             .should(QueryBuilders.rangeQuery(tagName).gte(dataMetric.getRight())));
                    break;
                case LTE_GTE:
                    bool.should(QueryBuilders.boolQuery()
                                             .should(QueryBuilders.rangeQuery(tagName).lte(dataMetric.getLeft()))
                                             .should(QueryBuilders.rangeQuery(tagName).gte(dataMetric.getRight())));
                    break;
                default:
                    EsQueryHelper.error();
            }
        };
        crowdQueryModel.getDataMetrics().forEach(dataMetricConsumer);
    }


    /**
     * 过滤查询关系，旧版中需要找到已有的查询关系再其中添加新的查询模型
     *
     * @param boolQuery 查询关系
     * @param tagName   标签名
     * @return 查询关系
     */
    public static BoolQueryBuilder queryQueryBuilder(BoolQueryBuilder boolQuery, String tagName) {
        BoolQueryBuilder bool = boolQuery.must()
                                         .stream()
                                         .filter(queryBuilder -> StringUtils.equals(BoolQueryBuilder.NAME, queryBuilder.getName()))
                                         .map(queryBuilder -> (BoolQueryBuilder) queryBuilder)
                                         .filter(boolQueryBuilder ->
                                                         boolQueryBuilder.should()
                                                                         .stream()
                                                                         .anyMatch(queryBuilder -> queryBuilder instanceof RangeQueryBuilder
                                                                                 && StringUtils.equals(((RangeQueryBuilder) queryBuilder).fieldName(), tagName))
                                         )
                                         .findFirst()
                                         .orElseGet(QueryBuilders::boolQuery);

        if (boolQuery.should().isEmpty()) {
            boolQuery.must(bool);
        }
        return bool;

    }
}
