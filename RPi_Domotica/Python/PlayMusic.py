import vlc

class MusicPlayer:
	p = None
	isPlaying = None
	hasNext = None

	def __init__(self):
		self.isPlaying = False
		self.hasNext = False


	def playSong(self):
		global p

		try:
			p.stop()
			print('stopped')
		except NameError:
			print('no player found')

		p = vlc.MediaPlayer("musicFile.mp3")
		self.hasNext = False
		p.play()
		self.isPlaying = True
		print('playing')


	def changeState(self, pause):
		global p
		
		print('pausing')
		p.set_pause(pause)
		
		if (p.is_playing()):
			isPlaying = True
		else:
			isPlaying = False


	def setHasNext(self, boolean):
		self.hasNext = boolean


	def getHasNext(self):
		return self.hasNext

		
	def getTime(self):
		global p
		if (p.is_playing()):
			return p.get_time()
		else:
			return 'np'
			
		