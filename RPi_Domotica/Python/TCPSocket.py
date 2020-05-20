#!/usr/bin/env python3

import socket
from threading import Thread

class SocketClient:

	def __init__(self, host, port):
		self.host = host
		self.port = port
		self.conn = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		while True:
            		try:
                		self.conn.connect((host, port))
                		break
            		except TimeoutError:
                		print('socket timeout')
                		continue
		self.closed = False
		print('connection found')
		self._running = True


	def write(self, message, eenheid=None):
		if eenheid is not None:
			message = message + str(eenheid)

		message = str(message) + '\n'
		self.conn.sendall(bytes(message, encoding="ascii"))

		print('bericht verzonden ' + message)


	def getClosed(self):
		return self.closed


	def run(self):
		global cycle
		while self._running:
			self.receive()
			
		
		