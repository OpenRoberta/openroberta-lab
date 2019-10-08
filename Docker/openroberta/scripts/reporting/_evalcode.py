'''
for testing Python snippets
'''

from datetime import datetime

currentSecond= datetime.now().second
currentMinute = datetime.now().minute
currentHour = datetime.now().hour

currentDay = datetime.now().day
currentMonth = datetime.now().month
currentYear = datetime.now().year

f = lambda : (print('hello'),'world')[1]
print(f())