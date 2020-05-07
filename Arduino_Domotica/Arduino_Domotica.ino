#define lightSensor A0

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  pinMode(lightSensor, INPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
  if (Serial.available() > 0) {
    char incomingByte = (char)Serial.read();
    if (incomingByte == 'g') {
      delay(100);
      char secondByte = (char)Serial.read();
      if (secondByte == 'b') {
        sendBrightness();
      }
    }
  }
}

void sendBrightness() {
  int brightness = analogRead(lightSensor);
  
  Serial.write(brightness);
  Serial.flush();
}
