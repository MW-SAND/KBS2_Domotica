#!/usr/bin/env python3

from TCPSocket import SocketClient
from ControlSenseHat import ControlSH

class MeasurementSocket(SocketClient):
	def receive(self):
		data = self.conn.recv(1024)

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

		elif data == b'mf\r\n':
			print('receiving music file')
			f = open("musicFile.wav", "wb")
			data = self.conn.recv(1024)
			stringData = str(data.strip().decode("utf-8"))
			numberOfBytes = int(stringData)
			self.write(numberOfBytes)			

			for i in range(1, numberOfBytes, 1024):
				data = self.conn.recv(1024)
				print('writing data')
				f.write(data)

			f.close()
			print('receiving complete')	
			mp = MusicPlayer()
			mp.playSong()
			self.write('nummer wordt gespeeld')