package com.kipa.mock.http.service.base;

import com.kipa.common.KipaProcessException;
import com.kipa.mock.http.entity.MockServerProperties;
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

    public ClientAndServer build(MockServerProperties mockServerProperties) {
        ClientAndServer clientAndServer = null;
        try {
            if (mockServerProperties != null) {
                    String remoteHost = mockServerProperties.getRemoteHost();
                    Integer remotePort = mockServerProperties.getRemotePort();
                    Integer localPort = mockServerProperties.getLocalPort();
                    if (StringUtils.isNotBlank(remoteHost) && remotePort != null && localPort != null) {
                        clientAndServer = new ClientAndServer(remoteHost, remotePort, localPort);
                    }else if (localPort != null) {
                        clientAndServer = new ClientAndServer(localPort);
                    }
                }else {
                clientAndServer = new ClientAndServer();
            }
        } catch (Exception e) {
            throw new KipaProcessException("clientAndServer创建失败",e);
        }
        return clientAndServer;
    }

    public void close(ClientAndServer clientAndServer) {
        if (clientAndServer != null) {
            clientAndServer.close();
        }
    }
}
