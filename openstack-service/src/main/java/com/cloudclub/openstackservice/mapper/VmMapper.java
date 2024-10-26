package com.cloudclub.openstackservice.mapper;

import com.cloudclub.openstackservice.dto.VmResponseDto.VmInfo;
import com.cloudclub.openstackservice.dto.VmResponseDto.VmInfoList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class VmMapper {
    public static VmInfo toVmInfo(String vmId, String vmName, String vmStatus, String vmIp) {
        return VmInfo.builder()
            .vmId(vmId)
            .vmName(vmName)
            .vmStatus(vmStatus)
            .vmIp(vmIp).
            build();
    }

    public static VmInfoList toVmInfoList(List<VmInfo> vmList) {
        return VmInfoList.builder()
            .vmInfoList(vmList)
            .build();
    }

}
