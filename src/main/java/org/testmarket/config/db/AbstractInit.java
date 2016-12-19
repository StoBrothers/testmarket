package org.testmarket.config.db;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.testmarket.service.ModuleService;

/**
 * Abstract class for loading data into DB.
 * 
 * @author Sergey Stotskiy
 */
public abstract class AbstractInit {

    private static final Logger logger = LoggerFactory.getLogger(AbstractInit.class);

    @Autowired
    private ModuleService moduleService;

    abstract protected void init();

    @Transactional
    @PostConstruct
    public void executeInit() {

        if (!moduleService.getModule(getClass().getSimpleName()).isPresent()) {

            logger.info("Starting installation of '{}' for version '{}'.",
                getModuleName());

            init();

            moduleService.create(getClass().getSimpleName());

            logger.info("Finished installation of '{}'.", getClass().getSimpleName());
        }

    }

    private String getModuleName() {
        return getClass().getSimpleName();
    }

}