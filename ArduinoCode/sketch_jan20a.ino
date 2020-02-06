#include <ArduinoWebsockets.h>
#include <ArduinoJson.h>
#include <ESP8266WiFi.h>

const char* ssid = "your network name"; //Enter SSID
const char* password = "your password"; //Enter Password
const char* websockets_server = "ws://192.168.1.120:9000/socket"; //server adress and port
int analogValue = 0;
const int analogInPin = A0;
const int capacity = JSON_OBJECT_SIZE(2);
StaticJsonDocument<capacity> doc;
using namespace websockets;

void onMessageCallback(WebsocketsMessage message) {
    Serial.print("Got Message: ");
    Serial.println(message.data());
}

void onEventsCallback(WebsocketsEvent event, String data) {
    if(event == WebsocketsEvent::ConnectionOpened) {
        Serial.println("Connnection Opened");
    } else if(event == WebsocketsEvent::ConnectionClosed) {
        Serial.println("Connnection Closed");
    } else if(event == WebsocketsEvent::GotPing) {
        Serial.println("Got a Ping!");
    } else if(event == WebsocketsEvent::GotPong) {
        Serial.println("Got a Pong!");
    }
}

WebsocketsClient client;
void setup() {
    Serial.begin(115200);
    // Connect to wifi
    WiFi.begin(ssid, password);
    
    // Wait some time to connect to wifi
    for(int i = 0; i < 10 && WiFi.status() != WL_CONNECTED; i++) {
        Serial.print(".");
        delay(1000);
    }

//    // Setup Callbacks
//    client.onMessage(onMessageCallback);
//    client.onEvent(onEventsCallback);
    
    // Connect to server
    client.connect(websockets_server);

    // Send a message


}

void loop() {
    client.poll();
    analogValue = analogRead(analogInPin);
    doc["id"] = 1;
    doc["value"] = analogValue;
    
    Serial.print(analogValue);
    client.send(doc.as<String>());
    
    delay(1000);
}
