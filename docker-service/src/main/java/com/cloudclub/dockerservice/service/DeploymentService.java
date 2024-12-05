package com.cloudclub.dockerservice.service;

import com.cloudclub.dockerservice.DeploymentProjectType;
import com.cloudclub.dockerservice.common.exception.BaseException;
import com.cloudclub.dockerservice.common.exception.errorcode.DeploymentErrorCode;
import com.cloudclub.dockerservice.dto.DeploymentRequestDto;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeploymentService {


    @Value("${vm.host}")
    private String vmHost;

    @Value("${docker.hub.username}")
    private String dockerHubUsername;

    @Value("${docker.hub.password}")
    private String dockerHubPassword;


    public void deploy(DeploymentRequestDto requestDto) {
        // 1. 깃허브 리포지토리 클론
        String clonePath = cloneRepository(requestDto.getGithubRepoUrl());
        log.info("Repository cloned to: {}", clonePath);

        // 2. 도커 이미지 빌드
        String imageName = buildDockerImage(clonePath, requestDto.getDeploymentProjectType());
        log.info("Docker image built: {}", imageName);

        // 3. docker hub 푸쉬(임시) or 하버 or ECR or Openstack
        pushToDockerHub(imageName);

//        4. VM에서 컨테이너 실행
//        runContainerOnVM(imageName);
    }

    private String cloneRepository(String githubRepoUrl) {
        try {
            Path tempDir = Files.createTempDirectory("repo-clone-");
            ProcessBuilder processBuilder = new ProcessBuilder("git", "clone", githubRepoUrl,
                    tempDir.toString());
            Process process = processBuilder.start();
            process.waitFor();
            return tempDir.toString();
        } catch (Exception e) {
            log.error("Repository cloning failed", e);
            throw new BaseException(DeploymentErrorCode.REPOSITORY_CLONE_FAILED);
        }
    }


    private String buildDockerImage(String clonePath, DeploymentProjectType projectType) {
        try {

            Path dockerfile = Paths.get(System.getProperty("user.dir") + "/docker-service/src/main/resources/docker/"
                    + projectType.getType() + "/Dockerfile");

            log.info("Dockerfile path: {}", dockerfile);

            // 클론된 리포지토리 디렉토리로 복사
            Path targetDockerfile = Paths.get(clonePath, "Dockerfile");
            log.info("Target Dockerfile path: {}", targetDockerfile);
            Files.copy(dockerfile, targetDockerfile, StandardCopyOption.REPLACE_EXISTING);
            log.info("Dockerfile copied to: {}", targetDockerfile);

            // 고유한 이미지 이름 생성
            String imageName = generateImageName(clonePath);

            ProcessBuilder processBuilder = new ProcessBuilder(
                    "docker", "build", "-t", imageName, clonePath);
            processBuilder.redirectErrorStream(true);  // 표준 에러와 출력을 병합
            Process process = processBuilder.start();

            // TODO: 12/5/24 docker daemon 실행 스크립트

            // 프로세스 출력 확인
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);  // 로그를 출력하여 문제를 추적
            }
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new BaseException(DeploymentErrorCode.DOCKER_IMAGE_BUILD_FAILED);
            }


            return imageName;

        } catch (Exception e) {
            log.error("Docker image build failed", e);
            throw new BaseException(DeploymentErrorCode.DOCKER_IMAGE_BUILD_FAILED);
        }
    }

    private void pushToDockerHub(String imageName) {
        try {
            // Docker Hub 로그인
            ProcessBuilder loginBuilder =
                    new ProcessBuilder("docker", "login", "-u", dockerHubUsername, "-p", dockerHubPassword);
            Process loginProcess = loginBuilder.start();
            loginProcess.waitFor();

            // 이미지 푸시
            ProcessBuilder pushBuilder = new ProcessBuilder("docker", "push", imageName);
            Process pushProcess = pushBuilder.start();
            pushProcess.waitFor();

            log.info("Image pushed to Docker Hub: {}", imageName);
        } catch (Exception e) {
            log.error("Docker Hub push failed", e);
            throw new BaseException(DeploymentErrorCode.DOCKER_IMAGE_PUSH_FAILED);
        }
    }

    private void runContainerOnVM(String imageName) {
        // TODO: 12/5/24 컨테이너 실행
    }

    private String generateImageName(String clonePath) {
        // 리포지토리 이름 기반으로 고유한 이미지 이름 생성
        String repoName = Paths.get(clonePath).getFileName().toString();
        return repoName + ":" + System.currentTimeMillis();
    }
}
