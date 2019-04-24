#include <OneWire.h>
#include <DallasTemperature.h>

// Data wire is plugged into pin 2 on the Arduino
#define ONE_WIRE_BUS 3
// Setup a oneWire instance to communicate with any OneWire devices 
// (not just Maxim/Dallas temperature ICs)
OneWire oneWire(ONE_WIRE_BUS);
// Pass our oneWire reference to Dallas Temperature.
DallasTemperature sensors(&oneWire);

//Variables
float AnalogTempC;
int AnalogTempReading;

//Pins
int tempPin = 0;
int trigPin = 9;    // Trigger
int echoPin = 8;    // Echo
int PIR_ledPin = 13;                // choose the pin for the LED
int PIR_inputPin = 2;               // choose the input pin (for PIR sensor)
int PIR_pirState = LOW;             // we start, assuming no motion detected     

void setupUltrasound(int trigPin, int echoPin){
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
}

void setupPIR(int ledPin, int inputPin){
  pinMode(ledPin, OUTPUT);      // declare LED as output
  pinMode(inputPin, INPUT);     // declare sensor as input
}

void setupDiTemp(){
  sensors.begin();
}

void setupAnTemp(){
  analogReference(INTERNAL);
}
 
void sensors_setup(){
  setupDiTemp();
  setupAnTemp();//Analog Temperatur
  setupUltrasound(trigPin, echoPin);
  setupPIR(PIR_ledPin, PIR_inputPin);
}

int ultrTotal = 0;
int ultrValid = 0;

void loopUltrasound(){
  long duration, cm;
  
  digitalWrite(trigPin, LOW);
  delayMicroseconds(5);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
 
  // Read the signal from the sensor: a HIGH pulse whose
  // duration is the time (in microseconds) from the sending
  // of the ping to the reception of its echo off of an object.
  pinMode(echoPin, INPUT);
  duration = pulseIn(echoPin, HIGH);
  // Convert the time into a distance
  cm = (duration/2) / 29.1;     // Divide by 29.1 or multiply by 0.0343
  if(cm < 200){
    ultrTotal+=cm;
    ultrValid++;
  }
}

double getUltrasound(){
  double a = ultrTotal/(double)ultrValid;
  ultrTotal = 0;
  ultrValid = 0;
  return a;
}


long pirDetections = 0;

long getPIR(){
  long val = pirDetections;
  pirDetections = 0;
  return val;
}

void loopPIR(){
  pirDetections += digitalRead(PIR_inputPin);  // read input value
}

float getDiTemp(){
  sensors.requestTemperatures();
  return sensors.getTempCByIndex(0);
}

float getAnTemp(){
  AnalogTempReading = analogRead(tempPin);
  AnalogTempC = AnalogTempReading / 9.31;
  return AnalogTempC;
}
 
 
void sensors_loop(void){
//Digital Temp
  sensors.requestTemperatures(); // Send the command to get temperatures
  Serial.print("Digital Temp: ");
  Serial.print(getDiTemp()); // Why "byIndex"? 
  Serial.print("\n");
    // You can have more than one IC on the same bus. 
    // 0 refers to the first IC on the wire
//Analog Temp
  Serial.print("Analog Temp: ");
  Serial.println(getAnTemp());
//Ultrasound
  Serial.print("Ultrasound: ");
  Serial.print(getUltrasound());
  Serial.print("\n");
//PIR
  Serial.print("PIR: ");
  Serial.print(getPIR());
  Serial.print("\n");

  delay(1000);
}
