package com.godfunc.pay.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PayUrlRequestAdviceFinder {

    private final ApplicationContext applicationContext;

    /**
     * 找到所有实现了{@link PayUrlRequestAdvice}的类
     * @return
     */
    public List<PayUrlRequestAdvice> findAll() {
        String[] beanNames = applicationContext.getBeanNamesForType(PayUrlRequestAdvice.class);
        List<PayUrlRequestAdvice> list = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(beanNames)) {
            for (String beanName : beanNames) {
                PayUrlRequestAdvice advice = (PayUrlRequestAdvice) applicationContext.getBean(beanName);
                list.add(advice);
            }
            sort(list);
        }
        return list;
    }


    private void sort(List<?> payUrlRequestAdvices) {
        if (payUrlRequestAdvices.size() <= 1) {
            return;
        }
        Comparator<Object> comparatorToUse = OrderComparator.INSTANCE;
        payUrlRequestAdvices.sort(comparatorToUse);
    }
}
