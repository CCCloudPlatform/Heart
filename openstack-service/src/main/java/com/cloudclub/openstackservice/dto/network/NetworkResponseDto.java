package com.cloudclub.openstackservice.dto.network;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class NetworkResponseDto {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class NetworkInfo {
        private String name;
        private String id;
        private String projectId;
        private String status;
        private boolean isExternal;
        private int mtu;
    }


    @Getter
    @Builder
    @AllArgsConstructor
    public static class NetworkInfoList {
        private List<NetworkInfo> networkInfoList;
    }

}
