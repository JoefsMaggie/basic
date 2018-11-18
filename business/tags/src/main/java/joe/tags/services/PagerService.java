package joe.tags.services;

import com.google.common.collect.Lists;
import joe.core.exception.CommonRuntimeException;
import joe.elasticsearch.common.Metric;
import joe.elasticsearch.helper.EsHelper;
import joe.elasticsearch.model.querys.GeneralQueryModel;
import joe.elasticsearch.model.querys.QueryRelation;
import joe.tags.common.CusTagField;
import joe.tags.modal.bean.request.FieldPagerReq;
import joe.tags.modal.bean.request.SpecialReq;
import joe.tags.modal.bean.response.FieldPagerRes;
import joe.tags.modal.query.PagerQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0
 * @Project: joe
 * @Package joe.tag.services
 * @note: ES 分页查询服务层
 * @date Date : 2018年09月20日 12:03
 */
@Service
public class PagerService {

    private final static Logger LOG = LoggerFactory.getLogger(PagerService.class);

    private final PagerQuery query;

    @Autowired
    public PagerService(PagerQuery query) {
        this.query = query;
    }

    /**
     * 分页查询
     *
     * @param fieldPagerReq 根据字段做分页的请求模型
     * @return 根据字段做分页的数据返回模型
     */
    public FieldPagerRes fieldValues(FieldPagerReq fieldPagerReq) {
        if (fieldPagerReq.isDistinct()) {
            return this.distinct(fieldPagerReq);
        } else {
            return this.notDistinct(fieldPagerReq);
        }
    }

    /**
     * 特殊分页查询，提供测试使用
     *
     * @param fieldPagerReq 根据字段做分页的请求模型
     * @param specialReq    特殊处理数据
     * @return 根据字段做分页的数据返回模型
     */
    public FieldPagerRes specialFieldValues(FieldPagerReq fieldPagerReq, SpecialReq specialReq) {
        fieldPagerReq.setFields(new String[]{CusTagField.OPEN_ID, CusTagField.ID, CusTagField.GZ_STATUS,
                CusTagField.MOBILE, CusTagField.GROWTH_VALUE, CusTagField.JF_BALANCE, CusTagField.HB_BALANCE});
        if (specialReq.isUseSpecial()) {
            specialToConditions(fieldPagerReq, specialReq);
        } else {
            if (!specialReq.getSpecial().isEmpty()) {
                specialToConditions(fieldPagerReq, specialReq.getSpecial());
            }
        }
        return this.fieldValues(fieldPagerReq);
    }

    private void specialToConditions(FieldPagerReq fieldPagerReq, SpecialReq specialReq) {
        fieldPagerReq.setOrderBy(specialReq.getOrderby());
        QueryRelation condition = QueryRelation.builder().build();
        fieldPagerReq.setQueryRelation(condition);
        Field[] declaredFields = SpecialReq.class.getDeclaredFields();
        ArrayList<QueryRelation> and = Lists.newArrayListWithExpectedSize(declaredFields.length);
        condition.setAnd(and);
        try {
            // 处理关注时间
            gzTime(specialReq);
            // 处理成长值
            growthValue(specialReq, and);
            // 处理绑定手机
            isBindMobile(specialReq);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        Map<String, Pair<Object, Metric>> specialField = SpecialReq.specialField;
        Arrays.stream(declaredFields).forEach(field -> {
            try {
                field.setAccessible(true);
                String fieldName = field.getName();
                switch (fieldName) {
                    case "pageindex":
                        fieldPagerReq.setPageIndex(specialReq.getPageindex());
                        return;
                    case "pagesize":
                        fieldPagerReq.setPageSize(specialReq.getPagesize());
                        return;
                    case "orderby":
                        fieldPagerReq.setOrderBy(specialReq.getOrderby());
                        return;
                    case "gztimeday":
//                    case "mingrowthvalue":
//                    case "maxgrowthvalue":
                    case "growthvaluelist":
                        return;
                    default:
                        Pair<Object, Metric> objectMetricPair = specialField.get(fieldName);
                        Object value = field.get(specialReq);
                        if (Objects.isNull(value) || Objects.isNull(objectMetricPair)) {
                            return;
                        }
                        this.other(and, objectMetricPair, value);
                }
            } catch (Exception e) {
                LOG.error("反射异常", e);
                throw new CommonRuntimeException("500", e);
            }
        });
    }

    private void other(List<QueryRelation> and, Pair<Object, Metric> objectMetricPair, Object value) {
        List<String> values;
        String esField;
        if (objectMetricPair.getLeft() instanceof Map) {
            Map<String, Object> subMap = (Map<String, Object>) objectMetricPair.getLeft();
            values = subMap.values()
                           .stream()
                           .sorted()
                           .map(Object::toString)
                           .collect(Collectors.toList());
            esField = subMap.keySet().stream().findFirst().orElseThrow(() -> new CommonRuntimeException("500", "未知错误"));
        } else {
            if (value instanceof List) {
                values = (List<String>) value;
            } else {
                values = Lists.newArrayList(value.toString());
            }
            esField = objectMetricPair.getLeft().toString();
        }
        if (values.isEmpty()) {
            return;
        }
        GeneralQueryModel.Relation relation = GeneralQueryModel.Relation
                .builder()
                .values(values)
                .metric(objectMetricPair.getRight())
                .build();
        GeneralQueryModel generalQueryModel = createGeneralQueryModel(and);
        generalQueryModel.setRelations(Lists.newArrayList(relation));
        generalQueryModel.setField(esField);
    }

    private void isBindMobile(SpecialReq specialReq) throws NoSuchFieldException, IllegalAccessException {
        String isbindmoblie = "isbindmoblie";
        Field isBindMobile = SpecialReq.class.getDeclaredField(isbindmoblie);
        isBindMobile.setAccessible(true);
        if (Objects.isNull(isBindMobile.get(specialReq))) {
            return;
        }
        Integer isBindMobileValue = (Integer) isBindMobile.get(specialReq);
        switch (isBindMobileValue) {
            /*case 1:
                isBindMobile.set(specialReq, 0);
                break;*/
            case 2:
                isBindMobile.set(specialReq, null);
                Field mobileEmpty = SpecialReq.class.getDeclaredField("mobileEmpty");
                mobileEmpty.setAccessible(true);
                mobileEmpty.set(specialReq, "");
                Field mobileExists = SpecialReq.class.getDeclaredField("mobileExists");
                mobileExists.setAccessible(true);
                mobileExists.set(specialReq, "");
                break;
            case 3:
                isBindMobile.set(specialReq, null);
                Field mobileNotEmpty = SpecialReq.class.getDeclaredField("mobileNotEmpty");
                mobileNotEmpty.setAccessible(true);
                mobileNotEmpty.set(specialReq, "");
                mobileExists = SpecialReq.class.getDeclaredField("mobileExists");
                mobileExists.setAccessible(true);
                mobileExists.set(specialReq, "");
                break;
            case 4:
                isBindMobile.set(specialReq, 0);
                mobileNotEmpty = SpecialReq.class.getDeclaredField("mobileNotEmpty");
                mobileNotEmpty.setAccessible(true);
                mobileNotEmpty.set(specialReq, "");
                mobileExists = SpecialReq.class.getDeclaredField("mobileExists");
                mobileExists.setAccessible(true);
                mobileExists.set(specialReq, "");
                break;
            default:
                break;
        }
    }

    private void gzTime(SpecialReq specialReq) throws NoSuchFieldException, IllegalAccessException {
        Field gzTimeDay = SpecialReq.class.getDeclaredField("gztimeday");
        gzTimeDay.setAccessible(true);
        if (Objects.isNull(gzTimeDay.get(specialReq))) {
            return;
        }
        Integer dayCount = (Integer) gzTimeDay.get(specialReq);
        LocalDate now = LocalDate.now();
        LocalDateTime startDay;
        LocalDateTime endDay;
        if (dayCount == 181) {
            endDay = now.minusDays(179).atStartOfDay();
            startDay = endDay.minusDays(365 * 4);
        } else {
            startDay = now.minusDays(dayCount - 1).atStartOfDay();
            endDay = now.plusDays(1).atStartOfDay();
        }
        LOG.info(startDay.toString());
        LOG.info(endDay.toString());
        specialReq.setGzstarttime(startDay.toEpochSecond(ZoneOffset.of("+8")) * 1000);
        specialReq.setGzendtime(endDay.toEpochSecond(ZoneOffset.of("+8")) * 1000);
    }

    private void growthValue(SpecialReq specialReq, ArrayList<QueryRelation> and) throws NoSuchFieldException, IllegalAccessException {
        Field growthValueList = SpecialReq.class.getDeclaredField("growthvaluelist");
        growthValueList.setAccessible(true);
        Object growthValueObj = growthValueList.get(specialReq);
        List<GeneralQueryModel.Relation> relations = Lists.newArrayList();
        if (Objects.nonNull(growthValueObj)) {
            if (growthValueObj instanceof List) {
                List<Map<String, Integer>> growthValues = (List<Map<String, Integer>>) growthValueObj;
                if (growthValues.size() != 0) {
                    Field isMember = SpecialReq.class.getDeclaredField("isMember");
                    isMember.setAccessible(true);
                    isMember.set(specialReq, 1);
                }
                growthValues.forEach(growthValue -> {
                    String minGrowthValue = growthValue.get("mingrowthvalue").toString();
                    String maxGrowthValue = growthValue.get("maxgrowthvalue").toString();
                    relations.add(GeneralQueryModel.Relation.builder()
                                                            .values(Lists.newArrayList(minGrowthValue, maxGrowthValue))
                                                            .metric(Metric.GTE_LTE)
                                                            .build());
                });
            }
        }
//        Field minGrowthValue = SpecialReq.class.getDeclaredField("mingrowthvalue");
//        Field maxGrowthValue = SpecialReq.class.getDeclaredField("maxgrowthvalue");
//        minGrowthValue.setAccessible(true);
//        maxGrowthValue.setAccessible(true);
//        if (Objects.nonNull(minGrowthValue.get(specialReq)) && Objects.nonNull(maxGrowthValue.get(specialReq))) {
//            List<String> values = Lists.newArrayList(minGrowthValue.get(specialReq).toString(), maxGrowthValue.get(specialReq).toString());
//            relations.add(GeneralQueryModel.Relation.builder()
//                                                    .values(values)
//                                                    .metric(Metric.GTE_LTE)
//                                                    .build());
//            GeneralQueryModel generalQueryModel = createGeneralQueryModel(and);
//            generalQueryModel.setRelations(relations);
//            generalQueryModel.setField(CusTagField.GROWTH_VALUE);
//        }
    }

    private void specialToConditions(FieldPagerReq fieldPagerReq, Map<String, Object> special) {
        Object gzTimeDay = special.get("gztimeday");
        if (Objects.nonNull(gzTimeDay)) {
            Integer dayCount = Integer.valueOf(gzTimeDay.toString());
            LocalDate now = LocalDate.now();
            LocalDate startDay = now.minusDays(dayCount);
            special.put("gzstarttime", startDay.toString());
            special.put("gzendtime", now.toString());
            special.remove("gztimeday");
        }
        QueryRelation condition = QueryRelation.builder().build();
        ArrayList<QueryRelation> and = Lists.newArrayListWithExpectedSize(special.size());
        condition.setAnd(and);
        fieldPagerReq.setQueryRelation(condition);
        Map<String, Pair<Object, Metric>> specialField = SpecialReq.specialField;
        Object growthValueObj = special.get("growthvaluelist");
        if (Objects.nonNull(growthValueObj)) {
            if (growthValueObj instanceof List) {
                List<Map<String, Integer>> growthValues = (List<Map<String, Integer>>) growthValueObj;
                List<GeneralQueryModel.Relation> relations = Lists.newArrayListWithExpectedSize(growthValues.size() + 1);
                growthValues.forEach(growthValue -> {
                    String minGrowthValue = growthValue.get("mingrowthvalue").toString();
                    String maxGrowthValue = growthValue.get("maxgrowthvalue").toString();
                    relations.add(GeneralQueryModel.Relation.builder()
                                                            .values(Lists.newArrayList(minGrowthValue, maxGrowthValue))
                                                            .metric(Metric.GTE_LTE)
                                                            .build());
                });
                Object minGrowthValue = special.get("mingrowthvalue");
                Object maxGrowthValue = special.get("maxgrowthvalue");
                if (Objects.nonNull(minGrowthValue) && Objects.nonNull(maxGrowthValue)) {
                    relations.add(GeneralQueryModel.Relation.builder()
                                                            .values(Lists.newArrayList(minGrowthValue.toString(), maxGrowthValue.toString()))
                                                            .metric(Metric.GTE_LTE)
                                                            .build());
                }
                GeneralQueryModel generalQueryModel = createGeneralQueryModel(and);
                generalQueryModel.setRelations(relations);
                generalQueryModel.setField(CusTagField.GROWTH_VALUE);
                special.remove("growthvaluelist");
                special.remove("mingrowthvalue");
                special.remove("maxgrowthvalue");
            }
        }
        special.forEach((key, value) -> {
            switch (key) {
                case "pageindex":
                    fieldPagerReq.setPageIndex(Integer.valueOf(value.toString()));
                    return;
                case "pagesize":
                    fieldPagerReq.setPageSize(Integer.valueOf(value.toString()));
                    return;
                case "orderby":
                    if (StringUtils.isNotBlank(value.toString())) {
                        fieldPagerReq.setOrderBy(value.toString());
                    }
                    return;
            }
            Pair<Object, Metric> objectMetricPair = specialField.get(key);
            this.other(and, objectMetricPair, value);
        });
        LOG.info("special to fieldPagerReq: \n{}", fieldPagerReq);
    }

    private GeneralQueryModel createGeneralQueryModel(List<QueryRelation> and) {
        GeneralQueryModel generalQueryModel = GeneralQueryModel.builder().build();
        ArrayList<GeneralQueryModel> generalQueryModels = Lists.newArrayList(generalQueryModel);
        QueryRelation queryRelation = QueryRelation.builder().metrics(generalQueryModels).build();
        and.add(queryRelation);
        return generalQueryModel;
    }

    /**
     * 去重分页查询
     *
     * @param fieldPagerReq 根据字段做分页的请求模型
     * @return 根据字段做分页的数据返回模型
     */
    private FieldPagerRes distinct(FieldPagerReq fieldPagerReq) {
        SearchResponse response = query.distinctFieldValues(fieldPagerReq);
        Terms terms = response.getAggregations().get(fieldPagerReq.getFields()[0]);
        List<Map<String, Object>> values;
        Function<Terms.Bucket, Map<String, Object>> mapFunction = bucket -> {
            TopHits top = bucket.getAggregations().get("top");
            return top.getHits().getHits()[0].getSourceAsMap();
        };
        if (fieldPagerReq.getPageIndex() == 0) {
            values = terms.getBuckets()
                          .stream()
                          .map(mapFunction)
                          .collect(Collectors.toList());
        } else {
            values = terms.getBuckets()
                          .stream()
                          .skip(fieldPagerReq.getPageSize() * (fieldPagerReq.getPageIndex() - 1))
                          .limit(fieldPagerReq.getPageSize())
                          .map(mapFunction)
                          .collect(Collectors.toList());
        }
        return new FieldPagerRes()
                .setList(values)
                .setCount(terms.getBuckets().size())
                .setCurrentPage(fieldPagerReq.getPageIndex());
    }

    /**
     * 不去重分页查询
     *
     * @param fieldPagerReq 根据字段做分页的请求模型
     * @return 根据字段做分页的数据返回模型
     */
    private FieldPagerRes notDistinct(FieldPagerReq fieldPagerReq) {
        SearchResponse response = query.fieldValues(fieldPagerReq);
        long totalHits = response.getHits().totalHits;
        List<SearchHit> values;
        if (fieldPagerReq.getPageIndex() == 0) {
            String scrollId = response.getScrollId();
            List<String> scrollIds = Lists.newArrayList();
            values = Lists.newArrayListWithExpectedSize((int) totalHits);
            while (!(response.getHits().getHits().length == 0)) {
                scrollIds.add(scrollId);
                values.addAll(Arrays.asList(response.getHits().getHits()));
                response = EsHelper.queryScroll(scrollId);
                scrollId = response.getScrollId();
            }
            EsHelper.cleanScrollIds(scrollIds);
        } else {
            values = Arrays.asList(response.getHits().getHits());
        }
        return new FieldPagerRes()
                .setList(values.stream()
                               .map(SearchHit::getSourceAsMap)
                               .collect(Collectors.toList()))
                .setCount(totalHits)
                .setCurrentPage(fieldPagerReq.getPageIndex());
    }

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime localDateTime = now.minusDays(90);
        System.out.println(now);
        System.out.println(localDateTime);
        System.out.println(Duration.between(localDateTime, now).toDays());
    }
}
