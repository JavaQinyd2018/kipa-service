package com.kipa.mock.http.service.base;

import com.kipa.common.KipaProcessException;
import com.kipa.common.core.ClientFactory;
import com.kipa.mock.http.entity.MockServerProperties;
import org.apache.commons.lang3.StringUtils;
import org.mockserver.integration.ClientAndServer;
import org.springframework.stereotype.Service;

/**
 * @author: Qinyadong
 * @date: 2019/4/8 15:51
 * mock server配置类
 */
public class MockServiceClientFactory implements ClientFactory<MockServerProperties, ClientAndServer> {

    @Override
    public ClientAndServer create(MockServerProperties properties) throws Exception {
        ClientAndServer clientAndServer = null;
        try {
            if (properties != null) {
                String remoteHost = properties.getRemoteHost();
                Integer remotePort = properties.getRemotePort();
                Integer localPort = properties.getLocalPort();
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
