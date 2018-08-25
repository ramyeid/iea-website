import sys
sys.modules['time'] = __import__('time_mock')
import time
time.sleep('Need Exception!')
