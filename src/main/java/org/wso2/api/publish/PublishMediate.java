package org.wso2.api.publish;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.transport.passthru.util.RelayUtils;
import org.wso2.carbon.databridge.agent.DataPublisher;
import org.wso2.carbon.databridge.commons.exception.AuthenticationException;
import org.wso2.carbon.databridge.commons.exception.TransportException;
import org.wso2.carbon.databridge.core.exception.DataBridgeException;
import org.wso2.carbon.databridge.commons.exception.MalformedStreamDefinitionException;
import org.wso2.carbon.databridge.core.exception.DataBridgeException;
import org.wso2.carbon.databridge.core.exception.StreamDefinitionStoreException;
import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.util.Arrays;

/**
 * Created by irangamuthuthanthri on 8/5/16.
 */
public class PublishMediate extends AbstractMediator {
    private String dasHost="localhost" ;
    private String dasPort="7615" ;
    private String dasUsername="admin" ;
    private String dasPassword ="admin";
    private String streamName="API_Stream1:1.0.0" ;





    private String streamId;
    private DataPublisher dataPublisher;

    private static final Log log = LogFactory.getLog(PublishMediate.class);

    public PublishMediate() {
    }



    public boolean mediate(MessageContext messageContext) {

        try {

            dataPublisher = new DataPublisher(String.format("tcp://%s:%s", dasHost, dasPort), dasUsername, dasPassword);

            Object[] payload = payloadStream(messageContext);

            log.debug(String.format("transaction stream payload => %s", Arrays.toString(payload)));
            dataPublisher.publish(streamName, null, null, payload);
            dataPublisher.shutdown();


        } catch (Exception e) {
            e.printStackTrace();

        }
        return true;
    }


    private Object[] payloadStream(MessageContext messageContext) {
        String drinkName = messageContext.getEnvelope().getBody().getFirstElement().getFirstChildWithName(new QName("Order")).getFirstChildWithName(new QName("drinkName")).getText();
        String additions = messageContext.getEnvelope().getBody().getFirstElement().getFirstChildWithName(new QName("Order")).getFirstChildWithName(new QName("additions")).getText();
        String orderQuantity =messageContext.getEnvelope().getBody().getFirstElement().getFirstChildWithName(new QName("Order")).getFirstChildWithName(new QName("orderQuantity")).getText();


        return new Object[]{drinkName, additions, orderQuantity};
    }


    public String setDasHost(String Host) {
        dasHost = Host;
        return dasHost;
    }

    public String setDasPort(String Port) {
        dasPort = Port;
        return dasPort;
    }

    public String setDasUsername(String Username) {
        dasUsername = Username;
        return dasUsername;
    }

    public String setDasPassword(String Password) {
        dasPassword = Password;
        return dasPassword;
    }
    public String setStreamName(String stream) {
        streamName = stream;
        return streamName;
    }

    public String getDasHost() {
        return dasHost;
    }

    public String getDasPort() {
        return dasPort;
    }
    public String getDasUsername() {
        return dasUsername;
    }
    public String getDasPassword() {
        return dasPassword;
    }
    public String getStreamName() {
        return streamName;
}
}



