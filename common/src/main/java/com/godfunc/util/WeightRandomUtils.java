package com.godfunc.util;

import com.godfunc.entity.WeightEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class WeightRandomUtils {

    public static Integer getByWeight(List<? extends WeightEntity> list) {
        Integer sum = list.parallelStream().mapToInt(WeightEntity::getWeight).sum();
        if (sum <= 0) {
            log.error("当前权重选择出错，sum={}", sum);
            return 0;
        }
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int current = random.nextInt(sum);
        int m = 0;
        for (int i = 0; i < list.size(); i++) {
            WeightEntity weight = list.get(i);
            if (m <= current && current < m + weight.getWeight()) {
                return i;
            }
            m += weight.getWeight();
        }
        log.error("当前权重选择出错，没有选择到可用的数据，请检查权重数据是否正确");
        return 0;
    }
}
