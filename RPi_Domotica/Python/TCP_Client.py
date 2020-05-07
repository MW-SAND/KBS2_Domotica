#!/usr/bin/env python3

import socket
from ControlSenseHat import ControlSH

class SocketClient:

	def __init__(self, host, port):
		self.host = host
		self.port = port
		self.conn = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		self.conn.connect((host, port))
		self.closed = False


	def write(self, message, eenheid=None):
		if eenheid is not None:
			message = message + str(eenheid)

		message = str(message) + '\n'
		self.conn.sendall(bytes(message, encoding="ascii"))

		print('bericht verzonden ' + message)


	def receive(self):
		data = self.conn.recv(1024)
		print('Received: ', data.decode('utf-8'))

		if data == b'exit\r\n':
			self.conn.close()
			self.closed = True

		elif data == b'gt\r\n':
			self.write(ControlSH.getTemperature())

		elif data == b'gh\r\n':
			self.write(ControlSH.getHumidity())

		elif data == b'gp\r\n':
			self.write(ControlSH.getPressure())

		elif data == b'sv\r\n':
			ControlSH.setVerwarming()
			self.write('Verwarming aangezet')

		elif data == b'uv\r\n':
			ControlSH.unsetVerwarming()
			self.write('Verwarming uitgezet')

		elif data == b'sl\r\n':
			ControlSH.setLicht()
			self.write('Licht aangezet')

		elif data == b'ul\r\n':
			ControlSH.unsetLicht()
			self.write('Licht uitgezet')
			

	def getClosed(self):
		return self.closed


def main():
	while True:
		try:
			socket1 = SocketClient('192.168.178.42', 6369)
			
			while socket1.getClosed() != True:
				socket1.receive()
		except (ConnectionResetError, TimeoutError) as e:
			print(e)
			continue

if __name__ == '__main__':
    main()
	

			
		
		
