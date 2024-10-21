package com.cloudclub.openstackservice.controller.vm;

import com.cloudclub.openstackservice.dto.VmResponseDto.VmInfoList;
import com.cloudclub.openstackservice.service.vm.VmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/vm")
public class VmController {
    private final VmService vmService;

    @GetMapping
    public ResponseEntity<VmInfoList> getAllVm() {
        return ResponseEntity.ok(vmService.getAllVm());
    }
}
