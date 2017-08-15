package com.ubt.alpha2.statistics;

/**
 * @author: liwushu
 * @description: 获取统计对象的简单工厂
 * @created: 2017/7/19
 * @version: 1.0
 * @modify: liwushu
*/
class StatisticsFactory {
    protected static IStatistics produce(StatisticsKind statisticsKind) {
        switch (statisticsKind) {
            case STATISTICS_UMENG:
                return new UmengStatistics();
            default:
                return null;
        }
    }
}
