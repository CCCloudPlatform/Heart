package com.cloudclub.openstackservice.service.vm;

import com.cloudclub.openstackservice.common.exception.BaseException;
import com.cloudclub.openstackservice.common.exception.errorcode.VmErrorCode;
import com.cloudclub.openstackservice.dto.vm.VmRequestDto;
import com.cloudclub.openstackservice.dto.vm.VmResponseDto.VmInfo;
import com.cloudclub.openstackservice.dto.vm.VmResponseDto.VmInfoList;
import com.cloudclub.openstackservice.dto.vm.VmResponseDto.VmStatus;
import com.cloudclub.openstackservice.mapper.VmMapper;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.compute.Action;
import org.openstack4j.model.compute.RebootType;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.openstack.OSFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VmService {
    private final OSClientV3 osClientV3;

    private final static int TIMEOUT = 60000;

    public VmInfo getVmById(String id) {
        OSClientV3 client = OSFactory.clientFromToken(osClientV3.getToken());
        Server server = client.compute().servers().get(id);
        if (server == null) {
            throw new BaseException(VmErrorCode.EMPTY_VM);
        }
        return VmMapper.toVmInfo(server);
    }

    public VmInfoList getAllVm() {
        OSClientV3 client = OSFactory.clientFromToken(osClientV3.getToken());
        List<? extends Server> servers = client.compute().servers().list();
        List<VmInfo> vmInfoList = servers.stream().map(VmMapper::toVmInfo)
            .collect(Collectors.toList());
        return VmMapper.toVmInfoList(vmInfoList);
    }

    public VmStatus getVmStatus() {
        OSClientV3 client = OSFactory.clientFromToken(osClientV3.getToken());
        return VmStatus.builder()
            .totalVmCount(client.compute().servers().list().size())
            .runningVmCount((int) client.compute().servers().list().stream()
                .filter(server -> server.getStatus().equals(Server.Status.ACTIVE))
                .count())
            .build();
    }

    public void enrollVm(VmRequestDto requestDto) {
        OSClientV3 client = OSFactory.clientFromToken(osClientV3.getToken());
        ServerCreate serverCreate = VmMapper.toServerCreate(requestDto);
        client.compute().servers().bootAndWaitActive(serverCreate, TIMEOUT);
    }

    public void approveVm(String id) {
        OSClientV3 client = OSFactory.clientFromToken(osClientV3.getToken());
        client.compute().servers().action(id, Action.UNPAUSE);
    }

    public void deleteVm(String id) {
        OSClientV3 client = OSFactory.clientFromToken(osClientV3.getToken());
        client.compute().servers().delete(id);
    }

    public void powerOnVm(String id) {
        OSClientV3 client = OSFactory.clientFromToken(osClientV3.getToken());
        ActionResponse actionResponse = client.compute().servers().action(id, Action.START);
        if(!actionResponse.isSuccess()){
            throw new BaseException(VmErrorCode.VM_POWER_ON_FAILED);
        }
    }

    public void powerOffVm(String id) {
        OSClientV3 client = OSFactory.clientFromToken(osClientV3.getToken());
        ActionResponse actionResponse = client.compute().servers().action(id, Action.STOP);
        if(!actionResponse.isSuccess()){
            throw new BaseException(VmErrorCode.VM_POWER_OFF_FAILED);
        }
    }

    public void softRebootVm(String id) {
        OSClientV3 client = OSFactory.clientFromToken(osClientV3.getToken());
        ActionResponse actionResponse = client.compute().servers().reboot(id, RebootType.SOFT);
        if(!actionResponse.isSuccess()){
            throw new BaseException(VmErrorCode.VM_REBOOT_FAILED);
        }
    }

    public void hardRebootVm(String id) {
        OSClientV3 client = OSFactory.clientFromToken(osClientV3.getToken());
        ActionResponse actionResponse = client.compute().servers().reboot(id, RebootType.HARD);
        if(!actionResponse.isSuccess()){
            throw new BaseException(VmErrorCode.VM_REBOOT_FAILED);
        }
    }

    public String snapshotVm(String id) {
        OSClientV3 client = OSFactory.clientFromToken(osClientV3.getToken());
        String randomUuid = UUID.randomUUID().toString();
        String snapshotName = id + "-snapshot-" + randomUuid;
        return client.compute().servers().createSnapshot(id, snapshotName);
    }
}
