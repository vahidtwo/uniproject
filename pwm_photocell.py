import RPi.GPIO as GPIO
import time
import socket
import os

led_pin = 22
GPIO.setmode(GPIO.BOARD)
GPIO.setup(led_pin, GPIO.OUT)
pin_to_circuit = 38
pwm_led = GPIO.PWM(led_pin, 500)
pwm_led.start(100)

def getCPUuse():
    return(str(os.popen("top -n1 | awk '/Cpu\(s\):/ {print $2}'").readline().strip()))


def getCPUtemperature():
    res = os.popen('vcgencmd measure_temp').readline()
    return (res.replace("temp=", "").replace("'C\n", ""))

def rc_time(pin_to_circuit):
    count = 0
    GPIO.setup(pin_to_circuit, GPIO.OUT)
    GPIO.output(pin_to_circuit, GPIO.LOW)
    try:
        time.sleep(0.1)
    except:
        print("error in pwm")
    GPIO.setup(pin_to_circuit, GPIO.IN)
    while (GPIO.input(pin_to_circuit) == GPIO.LOW):
        count += 1
    return (count)


try:

    while True:
        photo = float(rc_time(pin_to_circuit))
        if photo > 1000:
            photo = 100
        else:
            photo = ((photo) / 100) - 3
        if photo < 0:
            photo = 0
        #time.sleep(0.1)
        print("pwm :   "+str(photo), 'and  ' + str(rc_time(pin_to_circuit)))
        pwm_led.ChangeDutyCycle(photo)
        try:

            temp = str(getCPUtemperature())
            usage = str(getCPUuse())
            print("pwm :   cpu%:"+usage+",cpuTemp:"+temp)
        except:
            print("pwm :   cpu error")
        try:
            s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            time.sleep(0.1)
            s.connect(('192.168.88.213', 16662))
            s.send("photocell:" + str(rc_time(pin_to_circuit))+",cpu%:"+usage+",cpuTemp:"+temp)
            s.close()
        except:
            print("pwm :   socket not co")
except KeyboardInterrupt:
    pass
finally:
    GPIO.cleanup()
