
int m1a = 7;
int m1b =8;
int m2a =5;
int m2b =6;
int actuatorA =9;
int actuatorB =10;

void setup() {
  Serial.begin(9600);
  pinMode(m1a, OUTPUT);
  pinMode(m1b, OUTPUT);
  pinMode(m2a, OUTPUT);
  pinMode(m2b, OUTPUT);
  pinMode(actuatorA, OUTPUT);
  pinMode(actuatorB, OUTPUT);
}

void halt(){
  digitalWrite(m1a, LOW);
  digitalWrite(m1b, LOW);
  digitalWrite(m2a, LOW);
  digitalWrite(m2b, LOW);
  delay(10);
}
void forward(){
  digitalWrite(m1a, HIGH);
  digitalWrite(m1b, LOW);
  digitalWrite(m2a, HIGH);
  digitalWrite(m2b, LOW);
  delay(10);
}
void backward(){
  digitalWrite(m1a, LOW);
  digitalWrite(m1b, HIGH);
  digitalWrite(m2a, LOW);
  digitalWrite(m2b, HIGH);
  delay(10);
}
void right(){
  digitalWrite(m1a, LOW);
  digitalWrite(m1b, HIGH);
  digitalWrite(m2a, HIGH);
  digitalWrite(m2b, LOW);
  delay(10);
}
void left(){
  digitalWrite(m1a, HIGH);
  digitalWrite(m1b, LOW);
  digitalWrite(m2a, LOW);
  digitalWrite(m2b, HIGH);
  delay(10);
}
void actuator(int direction){
  if(direction==1){
    digitalWrite(actuatorA, HIGH);
    digitalWrite(actuatorB, LOW);
  }
  else if(direction==2){
    digitalWrite(actuatorA, LOW);
    digitalWrite(actuatorB, HIGH);
  }
  else{
    digitalWrite(actuatorA, LOW);
    digitalWrite(actuatorB, LOW);
  }
}

void soloTreads(int pwm){
  if(pwm==44){
    }
  else{
    int left=int(pwm%10);
    int right=int(pwm/10);
    int motor1A;
    int motor1B;
    int motor2A;
    int motor2B;
    if(left>4){
      motor1A=HIGH;
      motor1B=LOW;
      left=left-4;
      }
    else{
      motor1A=LOW;
      motor1B=HIGH;
      left=4-left;
    }
    if(right>4){
      motor2A=LOW;
      motor2B=HIGH;
      right=right-4;
      }
    else{
      motor2A=HIGH;
      motor2B=LOW;
      right=4-right;
    }
    digitalWrite(m1a, motor1A);
    digitalWrite(m1b, motor1B);
    delay(left);
  
    digitalWrite(m1a, LOW);
    digitalWrite(m1b, LOW);
    delay(5-left);
    
    digitalWrite(m2a, motor2A);
    digitalWrite(m2b, motor2B);
    delay(right);
  
    digitalWrite(m2a, LOW);
    digitalWrite(m2b, LOW);
    delay(5-right);
    
    if(Serial.available()<=0){
      soloTreads(pwm);
    }
  }
}
void leftTread(int pwm){
  digitalWrite(m1a, HIGH);
  digitalWrite(m1b, LOW);
  delay(pwm);

  digitalWrite(m1a, LOW);
  digitalWrite(m1b, LOW);
  delay(10-pwm);
}
void rightTread(int pwm){
  digitalWrite(m2a, LOW);
  digitalWrite(m2b, HIGH);
  delay(pwm);

  digitalWrite(m2a, LOW);
  digitalWrite(m2b, LOW);
  delay(10-pwm);
}
void loop() {
  if(Serial.available()>0){
    int data;
    data=Serial.read();
    Serial.print(data);
    Serial.print(" ");
    if(data==0){
      halt();
    }
    if(data==10){
      backward();
    }
    if(data==20){
      forward();
    }
    if(data==30){
      right();
    }
    if(data==40){
      left();
    }
    if(int(data/10)==5){
      int d=data%10;
      //Serial.print("data - ");
      //Serial.print(d);
      actuator(d);
      
    }
    if(int(data/100)==1){
      soloTreads(data%100);
    }
  }
}
