package com.cloudclub.openstackservice.controller.vm;

import com.cloudclub.openstackservice.dto.vm.VmRequestDto;
import com.cloudclub.openstackservice.dto.vm.VmResponseDto.VmInfo;
import com.cloudclub.openstackservice.dto.vm.VmResponseDto.VmInfoList;
import com.cloudclub.openstackservice.dto.vm.VmResponseDto.VmStatus;
import com.cloudclub.openstackservice.service.vm.VmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/vms")
public class VmController {
    private final VmService vmService;

    /**
     * VM 전부 조회
     * @return VmInfoList
     */
    @GetMapping
    public ResponseEntity<VmInfoList> getAllVm() {
        return ResponseEntity.ok(vmService.getAllVm());
    }

    /**
     * VM 상세 조회
     * @param id
     * @return VmInfo
     */
    @GetMapping("/{id}")
    public VmInfo getVmById(@PathVariable String id) {
        return vmService.getVmById(id);
    }

    /**
     * VM 상태 조회
     * @return VmStatus
     */
    @GetMapping("/stat")
    public VmStatus getVmStatus() {
        return vmService.getVmStatus();
    }

    /**
     * 새로운 VM 등록
     * @param VmRequestDto
     */
    @PostMapping
    public void enrollVm(@RequestBody VmRequestDto requestDto) {
        vmService.enrollVm(requestDto);
    }

    /**
     * VM 생성 승인
     * @param id
     * todo: 해당 로직은 수정이 필요합니다.
     */
    @PostMapping("/approve/{id}")
    public void approveVm(@PathVariable String id) {
        vmService.approveVm(id);
    }

    /**
     * VM 삭제
     * @param id
     */
    @DeleteMapping("/{id}")
    public void deleteVm(@PathVariable String id) {
        vmService.deleteVm(id);
    }

    /**
     * VM 전원 켜기
     * @param id
     */
    @PostMapping("/action/poweron/{id}")
    public void powerOnVm(@PathVariable String id) {
        vmService.powerOnVm(id);
    }

    /**
     * VM 전원 끄기
     * @param id
     */
    @PostMapping("/action/poweroff/{id}")
    public void powerOffVm(@PathVariable String id) {
        vmService.powerOffVm(id);
    }

    /**
     * VM 소프트 재부팅
     * @param id
     */
    @PostMapping("/action/softreboot/{id}")
    public void softRebootVm(@PathVariable String id) {
        vmService.softRebootVm(id);
    }

    /**
     * VM 하드 재부팅
     * @param id
     */
    @PostMapping("/action/hardreboot/{id}")
    public void hardRebootVm(@PathVariable String id) {
        vmService.hardRebootVm(id);
    }

    /**
     * VM 스냅샷 생성
     * @param id
     * @return String
     */
    @PostMapping("/action/snapshot/{id}")
    public String snapshotVm(@PathVariable String id) {
        return vmService.snapshotVm(id);
    }

}