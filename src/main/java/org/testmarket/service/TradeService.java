package org.testmarket.service;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.LockModeType;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;
import org.testmarket.domain.Company;
import org.testmarket.domain.FinType;
import org.testmarket.domain.FinancialInstrument;

public interface TradeService {

    long change(FinType type, Company seller, Company buyer, long count, BigDecimal delta);
    
    
    
}
