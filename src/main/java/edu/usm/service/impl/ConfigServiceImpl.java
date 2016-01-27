package edu.usm.service.impl;

import edu.usm.domain.ConfigDto;
import edu.usm.service.ConfigService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by andrew on 1/27/16.
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    @Value("${bayard.implementation.name}")
    private String implementationName;

    @Override
    public ConfigDto getImplementationConfig() {
        ConfigDto dto = new ConfigDto();
        dto.setImplementationName(implementationName);
        return dto;
    }
}
