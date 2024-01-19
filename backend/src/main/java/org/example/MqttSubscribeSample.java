package org.example;//package org.example;
//
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Component
public class MqttSubscribeSample {

    @PostConstruct
    public void connectAndSubscribe() {
        String broker       = "tcp://broker.emqx.io:1883";
        String clientId     = "JavaSample";
        MemoryPersistence persistence = new MemoryPersistence();
        System.out.println("start");
        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName("admin");
            connOpts.setPassword("13611443604lwz".toCharArray());

            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");

            sampleClient.setCallback(new MqttCallback() {
                public void connectionLost(Throwable cause) {
                    // 在这里添加日志
                    System.out.println("Connection lost!");
                    if (cause != null) {
                        cause.printStackTrace();
                    }
                    connectAndSubscribe();
                }

                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.out.println("Message arrived. Topic: " + topic + "  Message: " + message.toString());

                    // Convert message to valid JSON
                    String validJson = message.toString();
//                    String validJson = invalidJson.replaceAll("([a-zA-Z0-9]+)\\s*:\\s*([^,}\\]]+)", "\"$1\":\"$2\"");
                    System.out.println("Valid JSON: " + validJson);

                    // Parse the message
                    JSONObject jsonMessage = new JSONObject(validJson);
                    int clientId = jsonMessage.getInt("clientId");
                    int alert = jsonMessage.getInt("alert");
                    double lat = jsonMessage.getDouble("lat");
                    double lng = jsonMessage.getDouble("lng");
                    int value = jsonMessage.getInt("value");

                    // Connect to database and get device name
                    Connection conn = DriverManager.getConnection("jdbc:mysql://mysql:3306/web_of_things", "root", "13611443604lwz");
                    PreparedStatement stmt = conn.prepareStatement("SELECT name FROM device WHERE id = ?");
                    stmt.setInt(1, clientId);
                    ResultSet rs = stmt.executeQuery();
                    String deviceName = null;
                    if (rs.next()) {
                        deviceName = rs.getString("name");
                    }

                    // Check if device name was found
                    if (deviceName == null) {
                        System.out.println("No device found with id: " + clientId);
                        return;
                    }

                    // Prepare and execute insert statement
                    stmt = conn.prepareStatement("INSERT INTO message (device, alert, info, lat, lng, stamp, value) VALUES (?, ?, ?, ?, ?, ?, ?)");
                    stmt.setString(1, deviceName);
                    stmt.setInt(2, alert);
                    stmt.setString(3, jsonMessage.getString("info"));
                    stmt.setDouble(4, lat);
                    stmt.setDouble(5, lng);
                    String stamp = jsonMessage.getString("stamp").replace("_", " ");
                    stmt.setString(6, stamp);
                    stmt.setInt(7, value);
                    stmt.executeUpdate();
                    conn.close();
                }

                public void deliveryComplete(IMqttDeliveryToken token) {}
            });

            sampleClient.subscribe("device_msg5691");

        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}