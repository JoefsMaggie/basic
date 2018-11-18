package joe.elasticsearch.model.aggs;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.annotations.ApiModel;

import javax.validation.Valid;

/**
 * @author : Joe joe_fs@sina.com
 * @version : V1.0
 * @Project : joe
 * @Package : joe.elasticsearch.model.aggs
 * @Description : agg 请求模型
 * @date : 2018年09月30日 17:26
 */
@ApiModel
public class AggReq {
    @Valid
    private CountAgg count;
    @Valid
    private MaxAgg max;
    @Valid
    private MinAgg min;
    @Valid
    private AvgAgg avg;
    @Valid
    private SumAgg sum;
    @Valid
    private StatsAgg stats;
    @Valid
    private CardinalityAgg distinct;
    @Valid
    private MissingAgg missing;
    @Valid
    private HistogramAgg histogram;
    @Valid
    @JsonAlias("date_histogram")
    private DateHistogramAgg dateHistogram;
    @Valid
    private RangeAgg range;
    @Valid
    @JsonAlias("date_range")
    private DateRangeAgg dateRange;
    @Valid
    @JsonAlias("top_hits")
    private TopHitsAgg topHits;
    @Valid
    private FilterAgg filter;
    @Valid
    private TermsAgg terms;

    public CountAgg getCount() {
        return count;
    }

    public AggReq setCount(CountAgg count) {
        this.count = count;
        return this;
    }

    public MaxAgg getMax() {
        return max;
    }

    public AggReq setMax(MaxAgg max) {
        this.max = max;
        return this;
    }

    public MinAgg getMin() {
        return min;
    }

    public AggReq setMin(MinAgg min) {
        this.min = min;
        return this;
    }

    public AvgAgg getAvg() {
        return avg;
    }

    public AggReq setAvg(AvgAgg avg) {
        this.avg = avg;
        return this;
    }

    public SumAgg getSum() {
        return sum;
    }

    public AggReq setSum(SumAgg sum) {
        this.sum = sum;
        return this;
    }

    public StatsAgg getStats() {
        return stats;
    }

    public AggReq setStats(StatsAgg stats) {
        this.stats = stats;
        return this;
    }

    public CardinalityAgg getDistinct() {
        return distinct;
    }

    public AggReq setDistinct(CardinalityAgg distinct) {
        this.distinct = distinct;
        return this;
    }

    public MissingAgg getMissing() {
        return missing;
    }

    public AggReq setMissing(MissingAgg missing) {
        this.missing = missing;
        return this;
    }

    public HistogramAgg getHistogram() {
        return histogram;
    }

    public AggReq setHistogram(HistogramAgg histogram) {
        this.histogram = histogram;
        return this;
    }

    public DateHistogramAgg getDateHistogram() {
        return dateHistogram;
    }

    public AggReq setDateHistogram(DateHistogramAgg dateHistogram) {
        this.dateHistogram = dateHistogram;
        return this;
    }

    public RangeAgg getRange() {
        return range;
    }

    public AggReq setRange(RangeAgg range) {
        this.range = range;
        return this;
    }

    public DateRangeAgg getDateRange() {
        return dateRange;
    }

    public AggReq setDateRange(DateRangeAgg dateRange) {
        this.dateRange = dateRange;
        return this;
    }

    public TopHitsAgg getTopHits() {
        return topHits;
    }

    public AggReq setTopHits(TopHitsAgg topHits) {
        this.topHits = topHits;
        return this;
    }

    public FilterAgg getFilter() {
        return filter;
    }

    public AggReq setFilter(FilterAgg filter) {
        this.filter = filter;
        return this;
    }

    public TermsAgg getTerms() {
        return terms;
    }

    public AggReq setTerms(TermsAgg terms) {
        this.terms = terms;
        return this;
    }
}
