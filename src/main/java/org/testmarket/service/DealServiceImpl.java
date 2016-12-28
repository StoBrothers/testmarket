package org.testmarket.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.testmarket.domain.Deal;
import org.testmarket.domain.DealRepository;
import org.testmarket.domain.FinType;

/**
 * Deal service.
 *
 * @author Sergey Stotskiy
 *
 */
@Service("dealService")
public class DealServiceImpl implements DealService {

    @Autowired
    DealRepository dealRepository;
    /**
     * Storage last 10 deals
     */
    Map<FinType, Queue<BigDecimal>> avgValues = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        FinType[] types = FinType.values();
        for (FinType type : types) {
            avgValues.put(type, new ConcurrentLinkedQueue<BigDecimal>());
        }
    }

    @Override
    public BigDecimal getAveragePriceFinInstrument(FinType type) {
        return getAvgValueByType(type);
    }

    @Override
    @Transactional
    public Deal addDeal(FinType type, BigDecimal price, BigDecimal amount, long count) {
        BigDecimal averagePrice = price;// dealRepository.getAvgValueByType(type);
        Deal newDeal = new Deal(type, price, amount, count, averagePrice);

        Queue<BigDecimal> queue = avgValues.get(type);
        if (queue.size() >= 10) {
            queue.remove();
        }
        queue.add(price);
        return dealRepository.save(newDeal);
    }

    public BigDecimal getAvgValueByType(FinType type) {

        Queue<BigDecimal> queue = avgValues.get(type);

        OptionalDouble optional = queue.stream().mapToDouble((s) -> s.doubleValue())
            .average();

        if (optional.isPresent()) {
            return BigDecimal.valueOf(optional.getAsDouble());
        } else {
            throw new NullPointerException(" Average price is null ");
        }
    }
}
