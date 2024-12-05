package com.cloudclub.dockerservice.service;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
@Service
public class DeployService {

    private String user = "ubuntu"; // EC2 인스턴스의 사용자 이름
    private String host = "ec2-3-39-190-81.ap-northeast-2.compute.amazonaws.com"; // EC2 인스턴스의 퍼블릭 DNS
    private String privateKey = "/Users/kimtaeheon/Documents/Heart/docker-service/src/main/resources/pemKey/docker-service.pem"; // 개인 키 경로
    private String repoUrl = "https://github.com/Jake-huen/spring-template.git"; // GitHub 리포지토리 URL
    private String logFilePath = "/home/ubuntu/docker_logs.txt";

    public void deploy() {
        // EC2에 Docker 설치
        installDockerOnEC2();
        
        // EC2에 GitHub 코드 pull
        pullCodeFromGitHub();
        
        // Gradle로 프로젝트 빌드
        // buildGradleProject();
        
        // Dockerfile로 이미지 빌드
        buildDockerImage();
        
        // 이미지로 컨테이너 실행
        runDockerContainer();
        
        // OpenStack ECR에 이미지 푸시 및 VM에서 삭제
        pushImageToECR();
        cleanUp();
    }

    private Session createSession() {
        log.info("세션 시작");
        try {
            JSch jsch = new JSch();
            jsch.addIdentity(privateKey);
            Session session = jsch.getSession(user, host, 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            return session;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void executeCommand(String command) {
        log.info("명령 시작");
        Session session = createSession();
        if (session == null) return;

        try {
            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand(command);
            channelExec.setErrStream(System.err);
            InputStream in = channelExec.getInputStream();
            channelExec.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            channelExec.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.disconnect();
        }
    }

    private void installDockerOnEC2() {
        log.info("EC2에 Docker 설치");
        String command = "# Add Docker's official GPG key:\n" + //
                         "sudo apt-get update\n" + //
                         "sudo apt-get install ca-certificates curl\n" + //
                         "sudo install -m 0755 -d /etc/apt/keyrings\n" + //
                         "sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc\n" + //
                         "sudo chmod a+r /etc/apt/keyrings/docker.asc\n" + //
                         "\n" + //
                         "# Add the repository to Apt sources:\n" + //
                         "echo \\\n" + //
                         "  \"deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \\\n" + //
                         "  $(. /etc/os-release && echo \"$VERSION_CODENAME\") stable\" | \\\n" + //
                         "  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null\n" + //
                         "sudo apt-get update\n" + //
                         "sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin\n" + //
                         "sudo service docker start";
        
        executeCommand(command);
    }

    private void pullCodeFromGitHub() {
        log.info("Github code clone");
        String command = "git clone " + repoUrl + " /home/ubuntu/spring-template"; // GitHub에서 코드 클론
        executeCommandAndLog(command, logFilePath);
    }

    // private void buildGradleProject() {
    //     log.info("Gradle로 빌드");
    //     String command = "cd /home/ubuntu/spring-template && ./gradlew build"; // Gradle로 빌드
    //     executeCommand(command);
    // }

    private void buildDockerImage() {
        log.info("Docker Image 빌드");
        String command = "cd /home/ubuntu/spring-template/spring-template && sudo docker build -t spring-template ."; // Docker 이미지 빌드
        executeCommandAndLog(command, logFilePath);
    }

    private void runDockerContainer() {
        log.info("컨테이너 형태로 실행");
        String command = "sudo docker run -d -p 8080:8080 spring-template"; // Docker 컨테이너 실행
        executeCommandAndLog(command, logFilePath);
    }

    private void pushImageToECR() {
        // OpenStack ECR에 이미지 푸시 로직
    }

    private void cleanUp() {
        // VM에서 이미지 삭제 로직
    }

    private void executeCommandAndLog(String command, String logFilePath) {
        Session session = createSession();
        if (session == null) return;

        try {
            ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand(command);
            channelExec.setErrStream(System.err);
            InputStream in = channelExec.getInputStream();
            channelExec.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // 콘솔에 출력
                output.append(line).append("\n"); // 로그에 기록
            }
            channelExec.disconnect();

            // 로그 파일에 기록
            writeLogToFile(logFilePath, output.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.disconnect();
        }
    }

    private void writeLogToFile(String logFilePath, String logContent) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath, true))) {
            writer.write(logContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
