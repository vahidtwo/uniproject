import time
import dht11lib
import RPi.GPIO as GPIO
import socket
GPIO.setmode(GPIO.BOARD)
red = 37
green = 33
yellow = 35
Humidity =0
temperature = 0
#GPIO.cleanup()
GPIO.setup(red,GPIO.OUT)
GPIO.setup(yellow,GPIO.OUT)
GPIO.setup(green,GPIO.OUT)
sensor=dht11lib.DHT11(pin = 31)
def clean_LED():
        GPIO.output(red,False)
        GPIO.output(green,False)
        GPIO.output(yellow,False)

def get_temp_humidity():
    while(1):
            try:
                print("getting temp")
                result=sensor.read()
                print("geted")
                if result.is_valid():
                    clean_LED()
                    #Humidity, temperature = Adafruit_DHT.read_retry(sensor, DHT_connected_gpio)
                    #result =sensor.read()
                    temperature=result.temperature
                    Humidity=result.humidity
                    print('Temp={0:0.1f}*C  Humidity={1:0.1f}%'.format(temperature, Humidity))
                    if temperature >= 40 :
                        clean_LED()
                        GPIO.output(red,True)
                    elif temperature <= 30 :
                        clean_LED()
                        GPIO.output(green,True)
                    else:
                         clean_LED()
                         GPIO.output(yellow,True)
                    #return Humidity , temperature
                    try:
                        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                        s.connect(('192.168.88.213', 16662))
                        s.send("temp:"+str(temperature)+",humidity:"+str(Humidity))
                        s.close()
                    except :
                        print("socket not co")
                    print("end")
                else:
                  print('Failed to get !',result.error_code)
                  try:
                      time.sleep(1)
                  except:
                      print("err")
            except:
                print("error in try")


get_temp_humidity()

