package com.cloudclub.dockerservice.service;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class DeployService {

    public void deploy() {
        // EC2에 Docker 설치
        installDockerOnEC2();
        
        // EC2에 GitHub 코드 pull
        pullCodeFromGitHub();
        
        // Dockerfile로 이미지 빌드
        buildDockerImage();
        
        // 이미지로 컨테이너 실행
        runDockerContainer();
        
        // OpenStack ECR에 이미지 푸시 및 VM에서 삭제
        pushImageToECR();
        cleanUp();
    }

    private void installDockerOnEC2() {
        String user = "ubuntu"; // EC2 인스턴스의 사용자 이름
        String host = "ec2-3-39-190-81.ap-northeast-2.compute.amazonaws.com"; // EC2 인스턴스의 퍼블릭 DNS
        String privateKey = "/Users/kimtaeheon/Documents/Heart/docker-service/src/main/resources/pemKey/docker-service.pem"; // 개인 키 경로

        try {
            JSch jsch = new JSch();
            jsch.addIdentity(privateKey);
            System.out.println("identity added");

            Session session = jsch.getSession(user, host, 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("GSSAPIAuthentication","no");
            session.setServerAliveInterval(120 * 1000);
            session.setServerAliveCountMax(1000);
            session.setConfig("TCPKeepAlive","yes");
            session.connect();

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
                             "sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin";
            
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
            session.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pullCodeFromGitHub() {
        // GitHub에서 코드 pull 로직
    }

    private void buildDockerImage() {
        // Docker 이미지 빌드 로직
    }

    private void runDockerContainer() {
        // Docker 컨테이너 실행 로직
    }

    private void pushImageToECR() {
        // OpenStack ECR에 이미지 푸시 로직
    }

    private void cleanUp() {
        // VM에서 이미지 삭제 로직
    }
}
