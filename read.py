#!/usr/bin/env python
import socket
import RPi.GPIO as GPIO
import SimpleMFRC522
import time
reader = SimpleMFRC522.SimpleMFRC522()
while(1):
    try:
        id, text = reader.read()
        print(1,id)
        print(1,text)
        text=text.replace(" ","")
        if ((text==("vahid")or text.__contains__("ali"))and (id==(124031544076) or id==973252895679)):
            try:
                print(111)
                s = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
                s.connect(('192.168.88.213',16662))
                print("connect")
                s.send("rfidid:"+str(id)+",rfidtext:"+str(text))
                s.close()
                time.sleep(1)
            except Exception as e:
                print("socket not co",e)
        else:
            try:
                print(00)
                s = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
                s.connect(('192.168.88.213',16662))
                print("connect")
                s.send("rfidid:"+str(id)+",rfidtext:"+str(text)+",allert")
                s.close()
                time.sleep(1)
            except Exception as e:
                print("socket not co",e)
    except:
        print("cant read")
