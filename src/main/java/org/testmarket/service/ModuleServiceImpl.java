package org.testmarket.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testmarket.domain.Module;
import org.testmarket.domain.ModuleRepository;

/**
 * Module service and implementation.
 * 
 * @author Sergey Stotskiy
 *
 */
@Service("moduleService")
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    @Override
    public Optional<Module> getModuleById(long id) {
        return moduleRepository.findOneById(id);
    }

    @Override
    public Optional<Module> getModule(String name) {
        return moduleRepository.findOneByName(name);
    }

    @Override
    public Collection<Module> getAllModuless() {
        return moduleRepository.findAll();
    }

    @Override
    public Module create(String name) {
        Module module = new Module();
        module.setName(name);
        return moduleRepository.save(module);
    }
}
