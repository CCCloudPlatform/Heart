package com.cloudclub.openstackservice.mapper;

import com.cloudclub.openstackservice.dto.network.NetworkResponseDto.NetworkInfo;
import com.cloudclub.openstackservice.dto.network.NetworkResponseDto.NetworkInfoList;
import java.util.List;
import org.openstack4j.model.network.Network;
import org.springframework.stereotype.Component;

@Component
public class NetworkMapper {
    public static NetworkInfo toNetworkInfo(Network network) {
        return NetworkInfo.builder()
            .name(network.getName())
            .id(network.getId())
            .projectId(network.getTenantId())
            .status(network.getStatus().name())
            .isExternal(network.isRouterExternal())
            .mtu(network.getMTU())
            .build();
    }

    public static NetworkInfoList toNetworkInfoList(List<NetworkInfo> networkList) {
        return NetworkInfoList.builder()
            .networkInfoList(networkList)
            .build();
    }


}
