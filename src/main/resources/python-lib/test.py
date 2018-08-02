import sys
sys.modules['time'] = __import__('time_mock')
import RPi.GPIO as GPIO
import time

GPIO.setmode(GPIO.BOARD)
GPIO.setup(7, GPIO.OUT)

for i in range(5):
    GPIO.output(7, GPIO.LOW)
    time.sleep(0.1)
    GPIO.output(7, GPIO.HIGH)
    time.sleep(0.1)
