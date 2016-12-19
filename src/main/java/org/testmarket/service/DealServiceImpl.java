package org.testmarket.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.testmarket.domain.Account;
import org.testmarket.domain.Deal;
import org.testmarket.domain.DealRepository;
import org.testmarket.domain.FinType;

@Service("dealService")
public class DealServiceImpl implements DealService {

    @Autowired
    DealRepository dealRepository;
    
    public  BigDecimal getAveragePriceFinInstrument (FinType type) {
        return dealRepository.getAvgValueByType(type);
    }
    
    
    @Transactional
    public Deal addDeal(FinType type, BigDecimal price, BigDecimal amount, long count, Account buyer, Account seller) {
        BigDecimal averagePrice =  dealRepository.getAvgValueByType(type);
        Deal newDeal = new Deal(type, price, amount, count, buyer, seller, averagePrice);
        return dealRepository.saveAndFlush(newDeal);
    }

}
