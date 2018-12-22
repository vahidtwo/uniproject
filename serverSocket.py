import socket
import RPi.GPIO as gpio 
import time
listensocket =socket.socket(socket.AF_INET,socket.SOCK_STREAM)
port = 16662
IP = "192.168.88.225"
pin=32
gpio.setmode(gpio.BOARD)
gpio.setup(pin,gpio.OUT)
try:
	listensocket.bind((IP,port))
	listensocket.listen(999)
	print("server started "+IP+" on port "+ str(port))
	#clientsocket , add= listensocket.accept()
	print("new connection made")
	#pin=32
	#gpio.setmode(gpio.BOARD)
	#gpio.setup(pin,gpio.OUT)
        for i in range(0,3):
            gpio.output(pin,True)
	    time.sleep(1)
	    gpio.output(pin,False)
	print("setup")
	while True:
		clientsocket , add= listensocket.accept()
		#print("new connection made")
		print("in while")
		message =clientsocket.recv(7)
		print("message is : ", message )
		#time.sleep(1)
		if message == "end":
			print("the end")
			#clientsocket.clese()
		if message != "":			
			if message =="true":
				gpio.output(pin,True)
			else:
				print(message)
				gpio.output(pin,False)
				#clientsocket.close()
				#running=False 

except:
	gpio.cleanup()
	print("cleanUP")
	listensocket.close()
