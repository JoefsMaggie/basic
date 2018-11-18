package joe.elasticsearch.helper;

import com.google.common.collect.Lists;
import joe.elasticsearch.model.querys.GeneralQueryModel;
import joe.elasticsearch.model.querys.QueryRelation;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.List;
import java.util.Objects;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * @Project : joe
 * @Package : joe.elasticsearch.helper
 * @Description : ES 查询帮助类
 * @date : 2018年09月30日 11:52
 */
public class EsQueryHelper {
    /**
     * ES 查询模型生成
     *
     * @param queryRelation 查询关系
     * @return 查询模型
     */
    public static QueryBuilder create(QueryRelation queryRelation) {
        List<BoolQueryBuilder> boolQueryBuilders = createQuery(queryRelation);
        if (CollectionUtils.isEmpty(boolQueryBuilders)) {
            return QueryBuilders.matchAllQuery();
        }
        return boolQueryBuilders.get(0);
    }

    /**
     * ES 查询模型生成
     *
     * @param queryRelation 查询关系
     * @return 查询模型
     */
    private static List<BoolQueryBuilder> createQuery(QueryRelation queryRelation) {
        List<BoolQueryBuilder> queryBuilders = Lists.newArrayList();
        boolean andIsEmpty = CollectionUtils.isEmpty(queryRelation.getAnd());
        boolean orIsEmpty = CollectionUtils.isEmpty(queryRelation.getOr());
        boolean notIsEmpty = CollectionUtils.isEmpty(queryRelation.getNot());
        if (!andIsEmpty) {
            queryBuilders.add(createQuery(queryRelation.getAnd(), 1));
        }
        if (!orIsEmpty) {
            queryBuilders.add(createQuery(queryRelation.getOr(), 2));
        }
        if (!notIsEmpty) {
            queryBuilders.add(createQuery(queryRelation.getNot(), 3));
        }
        /*if (CollectionUtils.isNotEmpty(queryRelation.getMetrics())) {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            for (GeneralQueryModel generalQueryModel : queryRelation.getMetrics()) {
                BoolQueryBuilder dataQueryBuilder = QueryBuilders.boolQuery();
                generalQueryModel.getRelations().forEach(
                        dataMetric -> buildMetric(dataQueryBuilder, dataMetric, generalQueryModel.getField()));
                addQuery(1, boolQuery, dataQueryBuilder);
            }
            queryBuilders.add(boolQuery);
        }*/
        return queryBuilders;
    }

    /**
     * ES 查询模型生成
     *
     * @param relations 查询关系列表
     * @param type      查询类型，1：and；2：or；3：not
     * @return 查询模型
     */
    private static BoolQueryBuilder createQuery(List<QueryRelation> relations, int type) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        for (QueryRelation relation : relations) {
            List<BoolQueryBuilder> queryBuilders = createQuery(relation);
            addQuery(type, boolQuery, queryBuilders);
            if (CollectionUtils.isNotEmpty(relation.getMetrics())) {
//                if (type == 3) {
//                    // 当为非时，内部查询关系换成 OR
//                    type = 2;
//                }
                for (GeneralQueryModel generalQueryModel : relation.getMetrics()) {
                    BoolQueryBuilder dataQueryBuilder = QueryBuilders.boolQuery();
                    generalQueryModel.getRelations().forEach(
                            dataMetric -> buildMetric(dataQueryBuilder, dataMetric, generalQueryModel.getField()));
                    addQuery(type, boolQuery, dataQueryBuilder);
                }
            }
        }
        return boolQuery;
    }

    /**
     * 查询模型添加
     *
     * @param type                 查询类型，1：and；2：or；3：not
     * @param parentBoolQuery      父查询模型
     * @param childrenBoolBuilders 子查询模型列表
     */
    private static void addQuery(int type, BoolQueryBuilder parentBoolQuery, List<BoolQueryBuilder> childrenBoolBuilders) {
        childrenBoolBuilders.forEach(boolQueryBuilder -> addQuery(type, parentBoolQuery, boolQueryBuilder));
    }

    /**
     * 查询模型添加
     *
     * @param type                 查询类型，1：and；2：or；3：not
     * @param parentBoolQuery      父查询模型
     * @param childrenBoolBuilders 子查询模型
     */
    private static void addQuery(int type, BoolQueryBuilder parentBoolQuery, BoolQueryBuilder childrenBoolBuilders) {
        if (Objects.nonNull(childrenBoolBuilders)) {
            switch (type) {
                case 1:
                    parentBoolQuery.must(childrenBoolBuilders);
                    break;
                case 2:
                    parentBoolQuery.should(childrenBoolBuilders);
                    break;
                case 3:
                    parentBoolQuery.mustNot(childrenBoolBuilders);
            }
        }
    }

    /**
     * 根据数据指标来生成 ES 查询模型
     *
     * @param dataQueryBuilder 查询模型
     * @param relation         查询关系
     * @param field            查询字段
     */
    private static void buildMetric(BoolQueryBuilder dataQueryBuilder, GeneralQueryModel.Relation relation, String field) {
        QueryBuilder queryBuilder;
        switch (relation.getMetric()) {
            case EXISTS:
                queryBuilder = QueryBuilders.existsQuery(field);
                break;
            case NOT_EXISTS:
                queryBuilder = QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery(field));
                break;
            case EQ:
                queryBuilder = QueryBuilders.termsQuery(field, relation.getValues());
                break;
            case NE:
                queryBuilder = QueryBuilders.boolQuery().mustNot(QueryBuilders.termsQuery(field, relation.getValues()));
                break;
            case GT:
                queryBuilder = QueryBuilders.rangeQuery(field).gt(relation.getValues().get(0));
                break;
            case GTE:
                queryBuilder = QueryBuilders.rangeQuery(field).gte(relation.getValues().get(0));
                break;
            case LT:
                queryBuilder = QueryBuilders.rangeQuery(field).lt(relation.getValues().get(0));
                break;
            case LTE:
                queryBuilder = QueryBuilders.rangeQuery(field).lte(relation.getValues().get(0));
                break;
            case GT_LT:
                queryBuilder = QueryBuilders.rangeQuery(field).gt(relation.getValues().get(0)).lt(relation.getValues().get(1));
                break;
            case GTE_LT:
                queryBuilder = QueryBuilders.rangeQuery(field).gte(relation.getValues().get(0)).lt(relation.getValues().get(1));
                break;
            case GT_LTE:
                queryBuilder = QueryBuilders.rangeQuery(field).gt(relation.getValues().get(0)).lte(relation.getValues().get(1));
                break;
            case GTE_LTE:
                queryBuilder = QueryBuilders.rangeQuery(field).gte(relation.getValues().get(0)).lte(relation.getValues().get(1));
                break;
            case LT_GT:
                queryBuilder = QueryBuilders.boolQuery()
                                            .should(QueryBuilders.rangeQuery(field).gt(relation.getValues().get(0)))
                                            .should(QueryBuilders.rangeQuery(field).lt(relation.getValues().get(1)));
                break;
            case LTE_GT:
                queryBuilder = QueryBuilders.boolQuery()
                                            .should(QueryBuilders.rangeQuery(field).gt(relation.getValues().get(0)))
                                            .should(QueryBuilders.rangeQuery(field).lte(relation.getValues().get(1)));
                break;
            case LT_GTE:
                queryBuilder = QueryBuilders.boolQuery()
                                            .should(QueryBuilders.rangeQuery(field).gte(relation.getValues().get(0)))
                                            .should(QueryBuilders.rangeQuery(field).lt(relation.getValues().get(1)));
                break;
            case LTE_GTE:
                queryBuilder = QueryBuilders.boolQuery()
                                            .should(QueryBuilders.rangeQuery(field).gte(relation.getValues().get(0)))
                                            .should(QueryBuilders.rangeQuery(field).lte(relation.getValues().get(1)));
                break;
            case PARTICIPLE:
                queryBuilder = QueryBuilders.matchQuery(field, relation.getValues());
                break;
            case MATCH_FRONT:
                queryBuilder = QueryBuilders.wildcardQuery(field, "*" + relation.getValues().get(0));
                break;
            case MATCH_END:
                queryBuilder = QueryBuilders.wildcardQuery(field, relation.getValues().get(0) + "*");
                break;
            case MATCH_FRONT_END:
                queryBuilder = QueryBuilders.wildcardQuery(field, "*" + relation.getValues().get(0) + "*");
                break;
            case MATCH_MID:
                queryBuilder = QueryBuilders.wildcardQuery(field, relation.getValues().get(0) + "*" + relation.getValues().get(1));
                break;
            case MATCH_FRONT_MID_END:
                queryBuilder = QueryBuilders.wildcardQuery(field, "*" + String.join("*", relation.getValues()) + "*");
                break;
            default:
                error();
                return;
        }
        dataQueryBuilder.should(queryBuilder);
    }

    /**
     * 错误
     */
    public static void error() {
        throw new IllegalArgumentException("错误的度量指标");
    }
}
