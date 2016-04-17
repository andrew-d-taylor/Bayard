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

    @Value("${version}")
    private String version;

    @Value("${bayard.implementation.largeLogoFilePath}")
    private String largeLogoFilePath;

    @Value("${bayard.implementation.faviconFilePath}")
    private String faviconFilePath;

    @Value("${bayard.implementation.enableDevelopmentFeatures}")
    private String developmentEnabled;

    @Value("${bayard.implementation.startupMode}")
    private String startupMode;

    @Override
    public ConfigDto getImplementationConfig() {
        ConfigDto dto = new ConfigDto();
        dto.setImplementationName(implementationName);
        dto.setVersion(version);
        dto.setLargeLogoFilePath(largeLogoFilePath);
        dto.setFaviconFilePath(faviconFilePath);
        dto.setDevelopmentEnabled(Boolean.valueOf(developmentEnabled));
        dto.setStartupMode(Boolean.valueOf(startupMode));
        return dto;
    }

    @Override
    public ConfigDto getStartupMode() {
        ConfigDto dto = new ConfigDto();
        dto.setStartupMode(Boolean.valueOf(startupMode));
        return dto;
    }

}
