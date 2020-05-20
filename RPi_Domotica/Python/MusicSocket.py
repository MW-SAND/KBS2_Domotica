#!/usr/bin/env python3

import os
from TCPSocket import SocketClient
from PlayMusic import MusicPlayer
import vlc

class MusicSocket(SocketClient):
	mp = None

	def __init__(self, ip, port, mp):
		self.mp = mp
		super().__init__(ip, port)

	def receive(self):
		data = self.conn.recv(1024)

		if data == b'exit\r\n':
			self.conn.close()
			self.closed = True

		elif data == b'gt\r\n':
			time = self.mp.getTime()
			self.write(time)

		elif data == b'ps\r\n':
			self.write('Pausing song...')
			self.mp.changeState(1)

		elif data == b'rs\r\n':
			self.write('Resuming song...')
			self.mp.changeState(0)

		elif data == b'ns\r\n':
			if (self.mp.getHasNext()):
				os.rename("nextSong.mp3", "musicFile.mp3")
				self.mp.playSong()
				self.write('ps')
			else:
				self.write('sr')

		elif data == b'mf\r\n':
			print('receiving music file')
			f = open("musicFile.mp3", "wb")
			self.newFile(f)
			self.mp.playSong()
			self.write('Playing')

		elif data == b'nf\r\n':
			print('receiving music file')
			f = open("nextSong.mp3", "wb")
			self.newFile(f)
			self.mp.setHasNext(True)
			self.write('Received next')


	def newFile(self, file):
		data = self.conn.recv(1024)
		stringData = str(data.strip().decode('utf-8'))
		numberOfBytes = int(stringData)
		self.write(numberOfBytes)			
		bytesTeGaan = numberOfBytes

		while bytesTeGaan > 0:
			data = self.conn.recv(1024)
			file.write(data)
			bytesTeGaan -= len(data)

		file.close()

				