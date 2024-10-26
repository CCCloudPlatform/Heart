package com.cloudclub.openstackservice.service.vm;

import com.cloudclub.openstackservice.dto.VmResponseDto.VmInfo;
import com.cloudclub.openstackservice.dto.VmResponseDto.VmInfoList;
import com.cloudclub.openstackservice.mapper.VmMapper;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.model.compute.Server;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VmService {
    private final OSClientV3 osClientV3;

    public VmInfoList getAllVm() {
        List<? extends Server> servers = osClientV3.compute().servers().list();
        List<VmInfo> vmInfoList = servers.stream().map(server -> VmMapper.toVmInfo(
            server.getId(),
            server.getName(),
            server.getStatus().name(),
            server.getAccessIPv4()
        )).collect(Collectors.toList());
        return VmMapper.toVmInfoList(vmInfoList);
    }
}
