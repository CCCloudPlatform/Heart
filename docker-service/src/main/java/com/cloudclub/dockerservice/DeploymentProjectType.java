package com.cloudclub.dockerservice;

public enum DeploymentProjectType {
    SPRINGBOOT("springboot"),
    NODEJS("nodejs"),
    REACT("react"),
    ;

    private final String type;

    DeploymentProjectType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
