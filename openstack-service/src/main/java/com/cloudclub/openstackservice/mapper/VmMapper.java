package com.cloudclub.openstackservice.mapper;

import com.cloudclub.openstackservice.dto.vm.VmRequestDto;
import com.cloudclub.openstackservice.dto.vm.VmResponseDto.VmInfo;
import com.cloudclub.openstackservice.dto.vm.VmResponseDto.VmInfoList;
import java.util.List;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.openstack.compute.domain.NovaServerCreate;
import org.springframework.stereotype.Component;

@Component
public class VmMapper {
    public static VmInfo toVmInfo(Server server) {
        return VmInfo.builder()
            .id(server.getId())
            .name(server.getName())
            .status(server.getStatus().name())
            .externalIp(server.getAccessIPv4())
            .flavorId(server.getFlavor().getId())
            .image(server.getImage().getName())
            .keyPair(server.getKeyName())
            // todo: need to implement after configuring OpenStack
//            .securityGroup(server.getSecurityGroups().toString())
            .uuid(server.getTenantId()) // Assuming UUID is stored in TenantId
            .vmStatus(server.getVmState())
            .build();
    }

    public static VmInfoList toVmInfoList(List<VmInfo> vmList) {
        return VmInfoList.builder()
            .vmInfoList(vmList)
            .build();
    }

    public static ServerCreate toServerCreate(VmRequestDto requestDto) {
        ServerCreate serverCreate = NovaServerCreate.builder()
            .name(requestDto.getName())
            .flavor(requestDto.getFlavorID())
            .image(requestDto.getSelectedOS())
            .keypairName(requestDto.getKeypair())
            .addSecurityGroup(requestDto.getSelectedSecurityGroup())
            .build();

        serverCreate.addNetwork(requestDto.getExternalIpId(), requestDto.getExternalIP());
        return serverCreate;
    }
}
