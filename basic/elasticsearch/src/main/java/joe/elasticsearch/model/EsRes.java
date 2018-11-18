package joe.elasticsearch.model;

import org.elasticsearch.search.SearchHits;

import java.util.Map;

/**
 * @author : Joe joe_fs@sina.com
 * @version V1.0


 * @note: ES 返回模型
 * Date Date : 2018年09月27日 16:14
 */
public class EsRes {
    /**
     * 总数
     */
    private Long count;
    /**
     * 最大值
     */
    private Long max;
    /**
     * 最小值
     */
    private Long min;
    /**
     * 平均值
     */
    private Long avg;
    /**
     * 总和
     */
    private Long sum;
    /**
     * 去重数量
     */
    private Long distinctCount;
    /**
     * 过滤数量
     */
    private Long filterCount;
    /**
     * 不存在数量
     */
    private Long missingCount;
    /**
     * 聚合嵌套子聚合
     */
    private Map<String, Map<String, EsRes>> termsSub;
    /**
     * 直方图返回格式
     */
    private Map<String, Map<String, Long>> histograms;
    /**
     * 区间返回格式
     */
    private Map<String, Map<String, Long>> ranges;
    /**
     * tophit直接返回
     */
    private SearchHits hits;

    public Long getCount() {
        return count;
    }

    public EsRes setCount(Long count) {
        this.count = count;
        return this;
    }

    public Long getMax() {
        return max;
    }

    public EsRes setMax(Long max) {
        this.max = max;
        return this;
    }

    public Long getMin() {
        return min;
    }

    public EsRes setMin(Long min) {
        this.min = min;
        return this;
    }

    public Long getAvg() {
        return avg;
    }

    public EsRes setAvg(Long avg) {
        this.avg = avg;
        return this;
    }

    public Long getSum() {
        return sum;
    }

    public EsRes setSum(Long sum) {
        this.sum = sum;
        return this;
    }

    public Long getDistinctCount() {
        return distinctCount;
    }

    public EsRes setDistinctCount(Long distinctCount) {
        this.distinctCount = distinctCount;
        return this;
    }

    public Long getFilterCount() {
        return filterCount;
    }

    public EsRes setFilterCount(Long filterCount) {
        this.filterCount = filterCount;
        return this;
    }

    public Long getMissingCount() {
        return missingCount;
    }

    public EsRes setMissingCount(Long missingCount) {
        this.missingCount = missingCount;
        return this;
    }

    public Map<String, Map<String, EsRes>> getTermsSub() {
        return termsSub;
    }

    public EsRes setTermsSub(Map<String, Map<String, EsRes>> termsSub) {
        this.termsSub = termsSub;
        return this;
    }

    public Map<String, Map<String, Long>> getHistograms() {
        return histograms;
    }

    public EsRes setHistograms(Map<String, Map<String, Long>> histograms) {
        this.histograms = histograms;
        return this;
    }

    public Map<String, Map<String, Long>> getRanges() {
        return ranges;
    }

    public EsRes setRanges(Map<String, Map<String, Long>> ranges) {
        this.ranges = ranges;
        return this;
    }

    public SearchHits getHits() {
        return hits;
    }

    public EsRes setHits(SearchHits hits) {
        this.hits = hits;
        return this;
    }
}
