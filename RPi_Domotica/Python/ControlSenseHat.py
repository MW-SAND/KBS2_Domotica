from sense_hat import SenseHat

class ControlSH:
	sense = SenseHat()

	B = [0, 0, 0]
	R = [255, 0, 0]
	W = [255, 255, 255]

	verwarming = False;
	licht = False;

	alleenVerwarming = [
	B, B, B, B, B, B, B, B,
	B, B, B, B, B, B, B, B,
	B, B, B, B, B, B, B, B,
	B, B, B, B, B, B, B, B,
	R, R, R, R, R, R, R, R,
	R, R, R, R, R, R, R, R,
	R, R, R, R, R, R, R, R,
	R, R, R, R, R, R, R, R
	]

	alleenLicht = [
	W, W, W, W, W, W, W, W,
	W, W, W, W, W, W, W, W,
	W, W, W, W, W, W, W, W,
	W, W, W, W, W, W, W, W,
	B, B, B, B, B, B, B, B,
	B, B, B, B, B, B, B, B,
	B, B, B, B, B, B, B, B,
	B, B, B, B, B, B, B, B
	]

	beide = [
	W, W, W, W, W, W, W, W,
	W, W, W, W, W, W, W, W,
	W, W, W, W, W, W, W, W,
	W, W, W, W, W, W, W, W,
	R, R, R, R, R, R, R, R,
	R, R, R, R, R, R, R, R,
	R, R, R, R, R, R, R, R,
	R, R, R, R, R, R, R, R
	]

	uit = [
	B, B, B, B, B, B, B, B,
	B, B, B, B, B, B, B, B,
	B, B, B, B, B, B, B, B,
	B, B, B, B, B, B, B, B,
	B, B, B, B, B, B, B, B,
	B, B, B, B, B, B, B, B,
	B, B, B, B, B, B, B, B,
	B, B, B, B, B, B, B, B
	]


	@staticmethod
	def getTemperature():
		return ControlSH.sense.get_temperature()


	@staticmethod
	def getHumidity():
		return ControlSH.sense.get_humidity()


	@staticmethod
	def getPressure():
		return ControlSH.sense.get_pressure()


	@staticmethod
	def setVerwarming():
		if ControlSH.licht == False:
			ControlSH.sense.set_pixels(ControlSH.alleenVerwarming)
		else:
			ControlSH.sense.set_pixels(ControlSH.beide)

		ControlSH.verwarming = True


	@staticmethod
	def unsetVerwarming():
		if ControlSH.licht == False:
			ControlSH.sense.set_pixels(ControlSH.uit)
		else:
			ControlSH.sense.set_pixels(ControlSH.alleenLicht)

		ControlSH.verwarming = False


	@staticmethod
	def setLicht():
		if ControlSH.verwarming == False:
			ControlSH.sense.set_pixels(ControlSH.alleenLicht)
		else:
			ControlSH.sense.set_pixels(ControlSH.beide)
		
		ControlSH.licht = True


	@staticmethod
	def unsetLicht():
		if ControlSH.verwarming == False:
			ControlSH.sense.set_pixels(ControlSH.uit)
		else:
			ControlSH.sense.set_pixels(ControlSH.alleenVerwarming)

		ControlSH.licht = False

	