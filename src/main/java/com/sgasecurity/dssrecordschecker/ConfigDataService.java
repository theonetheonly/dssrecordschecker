package com.sgasecurity.dssrecordschecker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigDataService {
    @Autowired
    ConfigDataRepository configDataRepository;
    public ConfigData getConfigDataByConfigName(String configName) {
        return (ConfigData)this.configDataRepository.findByConfigName(configName);
    }
}
