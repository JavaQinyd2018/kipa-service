package com.kipa.mock.service.base;

import com.kipa.mock.entity.MockServerConfig;
import org.apache.commons.lang3.StringUtils;
import org.mockserver.integration.ClientAndServer;
import org.springframework.stereotype.Service;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 15:51
 * mock server配置类
 */
@Service("mockServiceGenerator")
public class MockServiceGenerator {

    public ClientAndServer build(MockServerConfig mockServerConfig) {
        ClientAndServer clientAndServer = null;
        try {
            if (mockServerConfig != null) {
                    String remoteHost = mockServerConfig.getRemoteHost();
                    Integer remotePort = mockServerConfig.getRemotePort();
                    Integer localPort = mockServerConfig.getLocalPort();
                    if (StringUtils.isNotBlank(remoteHost) && remotePort != null && localPort != null) {
                        clientAndServer = new ClientAndServer(remoteHost, remotePort, localPort);
                    }else if (localPort != null) {
                        clientAndServer = new ClientAndServer(localPort);
                    }
                }else {
                clientAndServer = new ClientAndServer();
            }
        } catch (Exception e) {
            throw new RuntimeException("clientAndServer创建失败",e);
        }
        return clientAndServer;
    }

    public void close(ClientAndServer clientAndServer) {
        if (clientAndServer != null) {
            clientAndServer.close();
        }
    }
}
