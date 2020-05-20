#define lightSensor A0
int lighting = 11;
int heater = 12;
boolean lightingState = false;
boolean heaterState = false;

void setup() {
  Serial.begin(9600);
  pinMode(lightSensor, INPUT);
  pinMode(lighting, OUTPUT);
  pinMode(heater, OUTPUT);
}

void loop() {
  if (Serial.available() > 0) {
    char incomingByte = (char)Serial.read();
    if (incomingByte == 'g') {
        sendBrightness();
    } else if (incomingByte == 'l') {
        setLighting();
    } else if (incomingByte == 'h') {
        setHeater();
    }
  }
  delay(30);
}

void sendBrightness() {
  int brightness = analogRead(lightSensor);
  
  Serial.print(brightness);
  Serial.flush();
}

void setLighting() {
  if (lightingState) {
    digitalWrite(lighting, LOW);
  } else {
    digitalWrite(lighting, HIGH);    
  }

  lightingState = !lightingState;
}

void setHeater()  {
  if (heaterState)  {
    digitalWrite(heater, LOW);
  } else {
    digitalWrite(heater, HIGH);
  }

  heaterState = !heaterState;
}
