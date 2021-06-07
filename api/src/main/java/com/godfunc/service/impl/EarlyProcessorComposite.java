package com.godfunc.service.impl;

import com.godfunc.param.PayOrderParam;
import com.godfunc.service.EarlyProcessor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * earlyProcessor的组合，获取所有的earlyProcessor进行排序，然后执行。
 */
@Component
@RequiredArgsConstructor
public class EarlyProcessorComposite implements EarlyProcessor {

    private final ApplicationContext applicationContext;

    public List<EarlyProcessor> earlyProcessorCached = null;

    @Override
    public boolean support(PayOrderParam param) {
        // 自定义操作，可以根据需要决定哪些商户走风控策略，哪些商户不走
        return false;
    }

    @Override
    public boolean check(HttpServletRequest request, PayOrderParam param) {
        if (earlyProcessorCached == null) {
            String[] earlyProcessorBeanNames = applicationContext.getBeanNamesForType(EarlyProcessor.class);
            List<EarlyProcessor> priorityOrderedEarlyProcessors = new ArrayList<>();
            List<EarlyProcessor> orderedEarlyProcessors = new ArrayList<>();
            List<EarlyProcessor> nonOrderedEarlyProcessors = new ArrayList<>();
            for (int i = 0; i < earlyProcessorBeanNames.length; i++) {
                String ebn = earlyProcessorBeanNames[i];
                if (applicationContext.isTypeMatch(ebn, PriorityOrdered.class)) {
                    EarlyProcessor earlyProcessor = (EarlyProcessor) applicationContext.getBean(ebn);
                    priorityOrderedEarlyProcessors.add(earlyProcessor);
                } else if (applicationContext.isTypeMatch(ebn, Ordered.class)) {
                    EarlyProcessor earlyProcessor = (EarlyProcessor) applicationContext.getBean(ebn);
                    orderedEarlyProcessors.add(earlyProcessor);
                } else {
                    EarlyProcessor earlyProcessor = (EarlyProcessor) applicationContext.getBean(ebn);
                    if (this != earlyProcessor) {
                        nonOrderedEarlyProcessors.add(earlyProcessor);
                    }
                }
            }
            sortPostProcessors(priorityOrderedEarlyProcessors);
            cachePostProcessors(priorityOrderedEarlyProcessors);

            sortPostProcessors(orderedEarlyProcessors);
            cachePostProcessors(orderedEarlyProcessors);

            sortPostProcessors(nonOrderedEarlyProcessors);
            cachePostProcessors(nonOrderedEarlyProcessors);
        }
        if (CollectionUtils.isNotEmpty(earlyProcessorCached)) {
            return true;
        }
        for (EarlyProcessor earlyProcessor : earlyProcessorCached) {
            if (earlyProcessor.support(param)) {
                if (!earlyProcessor.check(request, param)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void sortPostProcessors(List<EarlyProcessor> earlyProcessor) {
        if (earlyProcessor.size() > 1) {
            Comparator<Object> comparatorToUse = null;
            if (applicationContext instanceof DefaultListableBeanFactory) {
                comparatorToUse = ((DefaultListableBeanFactory) applicationContext).getDependencyComparator();
            }
            if (comparatorToUse == null) {
                comparatorToUse = OrderComparator.INSTANCE;
            }
            earlyProcessor.sort((Comparator) comparatorToUse);
        }
    }

    private void cachePostProcessors(List<EarlyProcessor> earlyProcessor) {
        earlyProcessorCached.addAll(earlyProcessor);
    }

}
