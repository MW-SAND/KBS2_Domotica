from MeasurementSocket import MeasurementSocket
from MusicSocket import MusicSocket
from threading import Thread
from PlayMusic import MusicPlayer

def main():
	socketForMeasurement = MeasurementSocket('192.168.178.42', 6369)
	measurementThread = Thread(target=socketForMeasurement.run)
	measurementThread.start()

	musicPlayer = MusicPlayer()
	
	socketForMusic = MusicSocket('192.168.178.42', 6370, musicPlayer)
	musicSocketThread = Thread(target=socketForMusic.run)
	musicSocketThread.start()


if __name__ == '__main__':
	main()