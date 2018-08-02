import argparse

HIGH = True
LOW = False
OUT = "out"
IN = "in"
UNASSIGNED = "unassigned"
BOARD = 1
didSetMode = False

GPIO_PINS = {3:UNASSIGNED, 5:UNASSIGNED, 7:UNASSIGNED, 8:UNASSIGNED, 10:UNASSIGNED,
             11:UNASSIGNED, 12:UNASSIGNED, 13:UNASSIGNED, 15:UNASSIGNED, 16:UNASSIGNED,
             18:UNASSIGNED, 19:UNASSIGNED, 21:UNASSIGNED, 22:UNASSIGNED, 23:UNASSIGNED,
             24:UNASSIGNED, 26:UNASSIGNED, 29:UNASSIGNED, 31:UNASSIGNED, 32:UNASSIGNED,
             33:UNASSIGNED, 35:UNASSIGNED, 36:UNASSIGNED, 37:UNASSIGNED, 38:UNASSIGNED,
             40:UNASSIGNED}

parser = argparse.ArgumentParser()
parser.add_argument("destination_file")
args = parser.parse_args()
destination_file = args.destination_file

def setmode(mode):
	global didSetMode
	if mode != BOARD:
		raise Exception("Please set pin numbering mode to BOARD")
	else:
		didSetMode = True

def setup(pin:int, pinType):
	if not didSetMode:
		raise Exception("Pin numbering mode has not been set up!")
	if pin in GPIO_PINS.keys():
		if pinType == OUT or pinType == IN:
			with open(destination_file, "a") as file:
				file.write("{0}:{1}\n".format(pin,pinType))
			GPIO_PINS[pin] = pinType
		else:
			raise Exception("An invalid direction was passed to setup()")
	else:
		raise Exception("Pin {} is invalid!".format(pin))


def output(pin:int, state):
	if not didSetMode:
		raise Exception("Pin numbering mode has not been set up!")
	if not(pin in GPIO_PINS.keys()):
		raise Exception("Pin {} is invalid!".format(pin))
	elif GPIO_PINS[pin] == OUT:
		with open(destination_file, "a") as file:
			file.write("{0}>{1}\n".format(pin, state))
	else:
		raise Exception("The GPIO channel has not been set up as an OUTPUT")
