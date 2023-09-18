package com.sgasecurity.dssrecordschecker;

import javax.persistence.*;

@Entity
@Table(name = "configs")
public class ConfigData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "config_name")
    private String configName;

    @Column(name = "config_value")
    private String configValue;
    @Column(name = "description")
    private String description;

    public ConfigData() {
    }

    public ConfigData(int id, String configName, String configValue, String description) {
        this.id = id;
        this.configName = configName;
        this.configValue = configValue;
        this.description = description;
    }

    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }

    public String getConfigName()
    {
        return configName;
    }
    public void setConfigName(String configName)
    {
        this.configName = configName;
    }

    public String getConfigValue()
    {
        return configValue;
    }
    public void setConfigValue(String configValue)
    {
        this.configValue = configValue;
    }

    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
}


