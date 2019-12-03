"""
utilities for log and stat file analysis. See file entry.py

@author: rbudde
"""

import sys
import re
import json
import zipfile

from entry import *

class ZipFileIterator:
    def __init__(self, zipFile, zipEntry, encoding='latin-1'):
        zipFileRef = zipfile.ZipFile(zipFile)
        self.byteReader = zipFileRef.open(zipEntry, mode='r')
        self.encoding = encoding

    def __iter__(self):
        for byteLine in self.byteReader:
            yield(byteLine.decode(self.encoding))

def getReader(baseDir, fileName, encoding='latin-1'):
    """
    return an iterator, that produces line by line read from a file
    
    :param fileName path to the file to be used in the iterator
    :param encoding encoding of the file, defaults to 'latin'
    :return the iterator enumerating the lines from the file
    """
    Entry.serverRestartNumber = 0
    Entry.unique = {}
    if fileName.endswith('.zip'):
        return ZipFileIterator(baseDir + '/' + fileName, fileName[:-4])
    else:
        lineReader = open(baseDir + '/' + fileName, mode='r', encoding=encoding)
        return lineReader

def fromLog(line):
    """
    GENERATOR: takes a string, which must be a line from a log file, extracts the timestamp, and creates a dict with keys 'time' and 'event'.
    The value of event is the remainder of the log line, when the timestamp has been removed

    Such a dict is called an 'entry'
    
    This mapper should be used immediately after a generator
    
    :param line from a log file
    :return an entry with keys 'time' and 'event'; return an entry with None if the regex failed, that selects the timestamp
    """
    matcher = re.search('^([^ ]*) ([^ ]*)(.*)$', line)
    if matcher is None:
        return Entry(None)
    date = matcher.group(1)
    time = matcher.group(2)
    text = matcher.group(3)
    entry = {}
    entry['event'] = text
    entry['time'] = date + ' ' + time
    return Entry(entry)

def fromStat(line):
    """
    GENERATOR: takes a string, which must be a line from a stat file, which starts with 'STATISTICS' followed by a valid json object.
    It creates a dict with the data from the json object. In any case the two keys 'time' and 'event' are created.
    The value of key 'event' is the json object as a string
    
    Such a dict is called an 'entry'
    
    This mapper should be used immediately after a generator
    
    :param line from a stat file
    :return an entry with the keys from the json object; return an entry with None if the regex or the json parse failed
    """
    matcher = re.search('STATISTICS (.*)', line)
    if matcher is None:
        return Entry(None)      
    jsonAsString = matcher.group(1)
    if jsonAsString:
        entry = json.loads(jsonAsString)
        entry['event'] = jsonAsString
        return Entry(entry)
    else:
        return Entry(None)

# prototype of a apache log line:
# 123.4.567.89 - - [11/Apr/2019:07:10:02 +0200] "GET /js/main.js HTTP/1.1" 200 2819
def fromApache(line):
    matcher = re.search('^[^:]*:(..):(..):(..)[^ \t]*[^"]*"(.*)$', line)
    if matcher is None:
        return None
    hh = matcher.group(1)
    mm = matcher.group(2)
    ss = matcher.group(3)
    rest = matcher.group(4)
    return None # (hh,mm,ss,rest,line)

def fromNginx(line):
    return None

def invertStore(storeInput, storeOutput):
    """
    accept an input store with an internal list or set and take its values, generate the keys of the output store from them
    and store the keys of the input store as the values of the output store
    
    :param storeInput to take key-val pairs from this store
    :param storeOutput to store the values from the input store as val-key pairs
    """
    for key,item in storeInput.data.items():
        if storeInput.storeSet:
            for val in item.storeSet:
                storeOutput.put(val,key)
        if storeInput.storeList:
            for val in item.storeList:
                storeOutput.put(val,key)
    
"""
the following declarations allow a classificatio of action:
- first the usable actions are declared and a class numer is associated
- then the classes are declared as a association between their number and their name (a 1:1 relation)
- last the classes are grouped w.r.t. their importance
"""  
classifyAction = {
    "GalleryView" : 0,
    "ProgramImport" : 5,
    "ChangeRobot" : 0,
    "ProgramShareDelete" : 0,
    "SimulationRun" : 1,
    "ConnectRobot" : 0,
    "ServerStart" : 3,
    "ProgramRun" : 1,
    "Initialization" : 0,
    "HelpClicked" : 0,
    "SimulationBackgroundUploaded" : 0,
    "ProgramRunBack" : 1,
    "ProgramSource" : 4,
    "ProgramDelete" : 5,
    "ProgramLoad" : 5,
    "ProgramLinkShare" : 0,
    "ProgramSave" : 5,
    "ProgramShare" : 0,
    "UserDelete" : 2,
    "UserLogout" : 2,
    "UserLogin" : 2,
    "ProgramExport" : 5,
    "GalleryShare" : 0,
    "GalleryLike" : 0,
    "ProgramNew" : 0,
    "UserCreate" : 2,
    "LanguageChanged" : 0
}

nameClasses = {
    0 : "misc",
    1 : "run",
    2 : "user",
    3 : "admin",
    4 : "src",
    5 : "prog"
}

classGroups = {
    "relevant" : [1,2,4,5],
    "starts" : [3],
    "all" : [0,1,2,3,4,5]
}