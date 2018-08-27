import sys
sys.modules['time'] = __import__('time_mock')
import time
while True:
	time.sleep(5)
