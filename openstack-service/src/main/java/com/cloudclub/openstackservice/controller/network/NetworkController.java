package com.cloudclub.openstackservice.controller.network;


import com.cloudclub.openstackservice.dto.network.NetworkResponseDto.NetworkInfo;
import com.cloudclub.openstackservice.dto.network.NetworkResponseDto.NetworkInfoList;
import com.cloudclub.openstackservice.service.network.NetworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/networks")
public class NetworkController {

    private final NetworkService networkService;


    @GetMapping
    public ResponseEntity<NetworkInfoList> getAllNetwork() {
        return ResponseEntity.ok(networkService.getAllNetwork());
    }

    @GetMapping("/{id}")
    public NetworkInfo getNetworkById(@PathVariable String id) {
        return networkService.getNetworkById(id);
    }

}
