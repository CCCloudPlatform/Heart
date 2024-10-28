package com.cloudclub.openstackservice.dto.vm;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class VmResponseDto {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class VmInfo {
        private String id;
        private String name;
        private String status;
        private String externalIp;
        private String flavorId;
        private String image;
        private String keyPair;
        private String securityGroup;
        private String Image;
        private String uuid;
        private String vmStatus;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class VmStatus {
        private int totalVmCount;
        private int runningVmCount;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class VmInfoList {
        private List<VmInfo> vmInfoList;
    }
}
