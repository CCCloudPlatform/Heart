package com.cloudclub.openstackservice.dto.vm;

import lombok.Data;

@Data
public class VmRequestDto {
    private String name;
    private String flavorID;
    private String externalIpId;
    private String externalIP;
    private String selectedOS;
    private String keypair;
    private String selectedSecurityGroup;
    private String uuid;
}