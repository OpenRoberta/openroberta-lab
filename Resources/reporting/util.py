'''
helper for log file analysis

@author: rbudde
'''

import sys

class SessionStore:
    def __init__(self):
        self.sessions = {}
        self.sessionTotalCounter = 0
        self.sessionCounter = 0

    def show(self, header):
        print(header)
        for sessionId, data in self.sessions.items(): 
            print(sessionId, " : ", data) 
        print(header)
        print("sessions opened: " + str(self.sessionTotalCounter))
        print("sessions not yet closed: " + str(self.sessionCounter))

    def add(self, key, value):
        self.sessionTotalCounter += 1
        self.sessionCounter += 1
        self.sessions[key] = value
        
    def remove(self, key):
        self.sessions.pop(key, None)
        self.sessionCounter -= 1
        
    def increment(self, key):
        oldCounter = self.sessions.get(key)
        if oldCounter is None:
            self.sessions[key] = 1
            self.sessionTotalCounter += 1
            self.sessionCounter = -1
        else:
            self.sessions[key] = oldCounter + 1
        
def getReader(fileName):
    actualEncoding = 'latin-1'
    lineReader = open(fileName, "r", encoding=actualEncoding)
    return lineReader
