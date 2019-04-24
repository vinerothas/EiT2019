// Basic demo for accelerometer readings from Adafruit LIS3DH
// https://learn.adafruit.com/adafruit-lis3dh-triple-axis-accelerometer-breakout/arduino
#include <Wire.h>
#include <SPI.h>
#include <Adafruit_LIS3DH.h>
#include <Adafruit_Sensor.h>

// Used for software SPI
#define LIS3DH_CLK 13
#define LIS3DH_MISO 11
#define LIS3DH_MOSI 12
// Used for hardware & software SPI
#define LIS3DH_CS 10

// software SPI
//Adafruit_LIS3DH lis = Adafruit_LIS3DH(LIS3DH_CS, LIS3DH_MOSI, LIS3DH_MISO, LIS3DH_CLK);
// hardware SPI
Adafruit_LIS3DH lis;
// I2C
//Adafruit_LIS3DH lis = Adafruit_LIS3DH();

#if defined(ARDUINO_ARCH_SAMD)
// for Zero, output on USB Serial console, remove line below if using programming port to program the Zero!
   #define Serial SerialUSB
#endif

void accelerator_setup() {
  Serial.println("accelerator_setup");
  lis = Adafruit_LIS3DH(LIS3DH_CS);
  if (! lis.begin(0x18)) {   // change this to 0x19 for alternative i2c address
    Serial.println("Accelerometer couldnt start");
  }else{
    Serial.println("LIS3DH found!");
  }
  
  lis.setRange(LIS3DH_RANGE_4_G);   // 2, 4, 8 or 16 G!
  
  //Serial.print("Range = "); 
  //Serial.print(2 << lis.getRange());  
  //Serial.println("G");
}

float lastX = 0;
float lastY = 0;
float lastZ = 0;
int iteration = 0;
float sensitivity = 0.25;
int detections = 0;
int noDetections = 0;

void accelerator_loop() {
  sensors_event_t event;
  
  lis.read(); 
  lis.getEvent(&event);
  lastX = event.acceleration.x;
  lastY = event.acceleration.y;
  delay(50); 
  
  bool detected = false;
  for(int i = 0; i<20; i++){
    lis.read();      // get X Y and Z data at once
    // Then print out the raw data
    //Serial.print("X:  "); Serial.print(lis.x); 
    //Serial.print("  \tY:  "); Serial.print(lis.y); 
    //Serial.print("  \tZ:  "); Serial.print(lis.z); 
  
    /* Or....get a new sensor event, normalized */  
    lis.getEvent(&event);
    if(abs(lastX-event.acceleration.x)>sensitivity || abs(lastY-event.acceleration.y)>sensitivity){
      /* Display the results (acceleration is measured in m/s^2) */
      //Serial.print("\t\tX: "); Serial.print(event.acceleration.x);
      //Serial.print(" \tY: "); Serial.print(event.acceleration.y); 
      //Serial.print(" \tZ: "); Serial.print(event.acceleration.z); 
      //Serial.println(" m/s^2 ");
    
      //Serial.print(iteration);
      //Serial.println("1");
      detections++;
      iteration++;
    }else{
      noDetections++;
      //Serial.println("0");
    }
    lastX = event.acceleration.x;
    lastY = event.acceleration.y;
    delay(50); 
  }
  Serial.println(detections);
  //if(detected){
      //Serial.println(" 1");
  //}else{
      //Serial.println(" 0");
  //}
}

int accelerator_poll(){
    int value = detections;
    int value2 = noDetections+value;
    //Serial.print("Accelerator loops: ");
    //Serial.println(value2);
    detections = 0;
    noDetections = 0;
    return value;
}
