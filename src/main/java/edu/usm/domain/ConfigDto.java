package edu.usm.domain;

import java.io.Serializable;

/**
 * Created by andrew on 1/27/16.
 */
public class ConfigDto implements Serializable{

    private String implementationName;

    public String getImplementationName() {
        return implementationName;
    }

    public void setImplementationName(String implementationName) {
        this.implementationName = implementationName;
    }
}
