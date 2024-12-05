package com.cloudclub.openstackservice.service.network;

import com.cloudclub.openstackservice.dto.network.NetworkResponseDto.NetworkInfo;
import com.cloudclub.openstackservice.dto.network.NetworkResponseDto.NetworkInfoList;
import com.cloudclub.openstackservice.mapper.NetworkMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.model.network.Network;
import org.openstack4j.openstack.OSFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NetworkService {
    private final OSClientV3 osClientV3;

    public NetworkInfoList getAllNetwork() {
        OSClientV3 client = OSFactory.clientFromToken(osClientV3.getToken());
        List<? extends Network> networks = client.networking().network().list();

        List<NetworkInfo> networkInfoList = networks.stream().map(NetworkMapper::toNetworkInfo)
            .toList();

        return NetworkMapper.toNetworkInfoList(networkInfoList);
    }

    public NetworkInfo getNetworkById(String id) {
        OSClientV3 client = OSFactory.clientFromToken(osClientV3.getToken());
        Network network = client.networking().network().get(id);
        return NetworkMapper.toNetworkInfo(network);
    }
}
