package org.testmarket.service;

import java.util.Collection;
import java.util.Optional;

import org.testmarket.domain.Module;

/**
 * Service for work with system modules.
 * 
 * @author Sergey Stotskiy
 *
 */
public interface ModuleService {

    /**
     * Looking module by id
     * 
     * 
     * @param id
     *            identificator
     * 
     * @return Module
     */
    Optional<Module> getModuleById(long id);

    /**
     * Looking module by name
     * 
     * @param version
     *            number of version
     * @param name
     *            version name
     * @return module
     */
    Optional<Module> getModule(String name);

    /**
     * @param version
     *            system version
     * @return collection of modules by version
     */
    Collection<Module> getAllModuless();

    /**
     * Created new module by version and name
     * 
     * @param name
     *            name
     * @return new module
     */
    Module create(String name);

}
