#! /usr/bin/python
import socket
import RPi.GPIO as gpio 
import time
listensocket =socket.socket(socket.AF_INET,socket.SOCK_STREAM)
port = 16662
IP = "192.168.88.225"
pin=7
gpio.setmode(gpio.BOARD)
try:
	listensocket.bind((IP,port))
	listensocket.listen(999)
        connectionStatus =False
        
	print("server started "+IP+" on port "+ str(port))
	while True:
		clientsocket , add= listensocket.accept()
		message =clientsocket.recv(8)
		print("message is : ", message )
		if message == "end":
			print("the end")
			#clientsocket.clese()
		if message != "":			
			if message =="false":
                            gpio.setup(pin,gpio.OUT)
                            connectionStatus=True
                        elif message=="true":
                            connectionStatus=False    
                            gpio.setup(pin,gpio.IN)
			    print(message)
                        elif message=="getstate":
                            try:
                                sendsocket =socket.socket(socket.AF_INET,socket.SOCK_STREAM)
                                print("sendsocket creat",add[0])

                                sendsocket.connect((str(add[0]),16662))
                                print("connect",add[0])
                                sendsocket.send("connectionStatus:"+str(connectionStatus))
                                sendsocket.close()
                                print("send")
                            except:
                                print("not send")

except Exception as e :
	gpio.cleanup()
	print("cleanUP")
        print(e)
	listensocket.close()
