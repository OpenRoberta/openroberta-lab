"""
utilities for log and stat file analysis. See file entry.py

@author: rbudde
"""

import sys
import re
import json

import numpy as np
import matplotlib.pyplot as plt

from entry import *

def getReader(fileName, encoding='latin-1'):
    """
    return an iterator, that produces line by line read from a file
    
    :param fileName path to the file to be used in the iterator
    :param encoding encoding of the file, defaults to 'latin'
    :return the iterator enumerating the lines from the file
    """
    Entry.serverRestartNumber = 0
    Entry.unique = {}
    lineReader = open(fileName, "r", encoding=encoding)
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

def showStore(store, fmt = '{:25} {}', title = None):
    """
    REDUCE: show the content of a store
    
    :param store to be used
    :param fmt to be used for printing, default '{:25} {}'. E.g. '{},{}'
    """
    if title is not None:
        print('\n' + str(title))
    if not store.storeList and not store.storeSet:
        for key, val in store.data.items():
            print(fmt.format(key, val.counter))
    if store.storeList:
        for key, val in store.data.items():
            print(fmt.format(key, val.counter, val.storeList))
    if store.storeSet:
        for key, val in store.data.items():
            print(fmt.format(key, val.counter, val.storeSet))

def showPie(store, title='pie plot', file='D:/downloads/pie.png'):
    """
    REDUCE: show the content of a store as a pie chart
    
    :param store to be used for plotting
    :param title title to be used
    :param file for the plot output
    """
    plt.close()
    keys = np.char.array(list(store.data.keys()))
    counters = np.array(list(map(lambda v: v.counter, store.data.values())))
    percent = 100.*counters/counters.sum()
    patches, texts = plt.pie(counters, startangle=90, radius=1.2)
    labels = ['{0} - {1:1.2f} %'.format(i,j) for i,j in zip(keys, percent)]
    patches, labels, dummy =  zip(*sorted(zip(patches, labels, counters), key=lambda x: x[2], reverse=True))
    plt.legend(patches, labels, loc='best', bbox_to_anchor=(-0.1, 1.), fontsize=8)
    #plt.title(title)
    plt.savefig(file, bbox_inches='tight')
    
def showBar(store, title='bar plot', legend='best', file='D:/downloads/bar.png'):
    """
    REDUCE: show the content of a store as a bar chart
    
    :param store to be used for plotting
    :param title title to be used
    :param file for the plot output
    """
    plt.close()
    keys = list(store.data.keys())
    counters = list(map(lambda v: v.counter, store.data.values()))
    bar = plt.bar(keys, counters, color='rgbkymc')
    plt.xticks(keys, rotation='vertical')
    plt.title(title)
    if legend is not None:
        labels = ['{0} - {1:6d}'.format(i,j) for i,j in zip(keys, counters)]
        plt.legend(bar, labels, loc=legend)
    plt.savefig(file, bbox_inches='tight')

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