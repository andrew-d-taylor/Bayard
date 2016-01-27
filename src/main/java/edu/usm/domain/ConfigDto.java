package edu.usm.domain;

import java.io.Serializable;

/**
 * Created by andrew on 1/27/16.
 */
public class ConfigDto implements Serializable{

    private String implementationName;

    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getImplementationName() {
        return implementationName;
    }

    public void setImplementationName(String implementationName) {
        this.implementationName = implementationName;
    }
}
