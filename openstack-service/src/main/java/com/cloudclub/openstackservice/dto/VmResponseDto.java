package com.cloudclub.openstackservice.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class VmResponseDto {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class VmInfo {
        private String vmId;
        private String vmName;
        private String vmStatus;
        private String vmIp;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class VmInfoList {
        private List<VmInfo> vmInfoList;
    }
}
