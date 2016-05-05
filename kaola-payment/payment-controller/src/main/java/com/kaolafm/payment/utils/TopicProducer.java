package com.kaolafm.payment.utils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;


public class TopicProducer {
	
	private static final String exchangeName = "monitor.log";

    private static final Logger logger = LogManager.getLogger(TopicProducer.class);

    private Channel channel = null;

    private Connection connection = null;

    private String host;

    private int port;

    private String virtualHost;

    private String username;
    private String password;

    public void init() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setVirtualHost(virtualHost);
        factory.setUsername(username);
        factory.setPassword(password);
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(exchangeName, "topic", true, false, null);
        } catch (Exception e) {
            logger.error("init rabbit mq error", e);
        }
    }

    /**
     * 停止
     *
     * @throws IOException
     * @throws TimeoutException
     * @author gaofeng  2016年1月21日
     */
    public void stopMq() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }

    /**
     * 发送消息
     *
     * @param exchangeName exchange名称
     * @param routingKey   routingKey
     * @param msg          消息
     * @throws Exception
     * @author gaofeng  2016年1月21日
     */
    public void sendMessage(String exchangeName, String routingKey, String msg) {
        try {
            channel.basicPublish(exchangeName, routingKey,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    msg.getBytes());
            logger.info("success send msg at time " + System.currentTimeMillis() + " sent msg: " + msg);
        } catch (Throwable e) {
            logger.info("fail send msg at time " + System.currentTimeMillis() + ", sent msg: " + msg);
            logger.error("send rabbit mq message error", e);
        }
    }
    
    /**
     * 发送消息
     *
     * @param exchangeName exchange名称
     * @param routingKey   routingKey
     * @param msg          消息
     * @throws Exception
     * @author gaofeng  2016年1月21日
     */
    public void sendMessage(String routingKey, String msg) {
        try {
            channel.basicPublish(exchangeName, routingKey,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    msg.getBytes());
            logger.info("success send msg at time " + System.currentTimeMillis() + " sent msg: " + msg);
        } catch (Throwable e) {
            logger.info("fail send msg at time " + System.currentTimeMillis() + ", sent msg: " + msg);
            logger.error("send rabbit mq message error", e);
        }
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
