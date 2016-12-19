package org.testmarket.service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testmarket.domain.Account;
import org.testmarket.domain.FinType;
import org.testmarket.domain.FinancialInstrument;
import org.testmarket.domain.FinancialInstrumentRepository;

@Service("finInstrumentService")
public class FinancialInstrumentServiceImpl implements FinancialInstrumentService {

    @Autowired
    FinancialInstrumentRepository finRepository;

    @Autowired
    TradeService tradeService; 


    private static Pattern pattern = Pattern.compile("[\\w,/]{3,7}");

    
    /**
     * Get or create one FinancialInstrument
     * @throws Exception 
     * 
     */
    @Transactional
    public FinancialInstrument getOrCreateFinInstrument(String symbolID, Account account, FinType type) throws Exception {
        
        Optional<FinancialInstrument> value = finRepository.findOneById(symbolID);

        if (value.isPresent()) {
            return value.get();
        }
        
        Matcher matcher = pattern.matcher(symbolID);
        if (!matcher.find()){
            throw new Exception(String.format("Not valid symbolID: %s for create financialInstrument", symbolID));
        };

        FinancialInstrument fin = new FinancialInstrument();
        fin.setId(symbolID);
        fin.setAccount(account);
        fin.setType(type);
        fin.setCompanyId(account.getCompany().getId());
        finRepository.save(fin);
        return fin;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void change () {
        //        tradeService.change();
        
    }    

}
