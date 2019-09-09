"""
building block for log and stat file analysis. A simple map-reduce functionality.
Both files contains logging lines. From these logging entries are build. An 'entry' is implemented as a dict.
Usually at least two keys are available:
- 'time': the time, the logging event occured, e.g. '2019-05-13 08:00:00.111'
- 'event' the line without the time (set by the 'from*' functions in 'util.py')

In the following these logging line dictioanries are called entries everywhere.

@author: rbudde
"""

import sys
import re
import json

class Entry:
    # reset in 'getReader from util.py'!
    serverRestartNumber = 0 # used to count the server restarts. This number is needed to de-duplicate the session-id
    unique = {}             # for checking uniqqueness of a key
    
    def __init__(self, entry): 
        self.entry = entry
        self.original = entry
        self.assembled = None
        if isinstance(self.entry,dict):
            message = self.entry['message']
            if message is not None:
                action = message['action']
                if action is not None:
                    if action == 'ServerStart':
                        Entry.serverRestartNumber += 1
            sessionId = self.entry['sessionId']
            if sessionId is not None:
                self.entry['sessionId'] = str(Entry.serverRestartNumber) + '-' + sessionId

    def filter(self, lambdaFct):
        """
        FILTER: retain entry if lambda returns True, discard otherwise
        
        :param lambdaFct to be called as lambdaFct(self.entry)
        :keep the entry, if lambda returns True; set None otherwise
        """
        if self.entry is not None:
            if not lambdaFct(self.entry):
                self.entry = None
        return self
    
    def filterRegex(self, key, *regexes):
        """
        FILTER: decides whether a regex matches the value of a key of an entry 
        
        :param key to be used for filtering
        :param *regexes matches one of the regex the value of the key (the regex are OR-ed)?
        :keep the entry, if one of the regex matched; set None otherwise
        """
        def filterLambda(entry): 
            val = entry[key]
            if val is not None:
                for regex in regexes:
                    matcher = re.search(regex, val)
                    if not matcher is None:
                        return True
            return False
        return self.filter(filterLambda)

    def filterVal(self, key, *vals, substring=True):
        """
        FILTER: decides whether a string is a substring of the value of a key of an entry 
        
        :param key to be used for filtering
        :param *vals is one of these strings a substring of the key's value (the vals are OR-ed)?
        :keep the entry, if one of the strings is a substring; set None otherwise
        """
        def filterLambda(entry):
            keyVal = entry[key]
            if keyVal is not None:
                for val in vals:
                    if substring and val in keyVal or val == keyVal:
                        return True
            return False
        return self.filter(filterLambda)

    def map(self, lambdaFct):
        """
        MAPPER: replace the entry by the value returned by the lambda applied to the lambda.
        In any case keep the value of the key 'time'
        
        :param lambdaFct to be called as lambdaFct(self.entry)
        :replace the entry by the return value of the lambda; keep None, if the entry was None already
        """
        if self.entry is not None:
            oldEntry = self.entry
            newEntry = lambdaFct(oldEntry)
            if newEntry is not None:
                newEntry['time'] = oldEntry['time']
            self.entry = newEntry
        return self

    def mapKeyRegexToMapped(self, key, regex):
        """
        MAPPER: runs a regex on the value of a key of an entry
        Replace the value of key 'mapped' of the entry by the join of all match groups
        
        :param key to be used for mapping
        :param regex applied to the value of the key
        :keep the updated entry; set None, if the key is not found, or the regex failed
        """
        def mapLambda(entry):
            val = entry[key]
            if val is not None:
                matcher = re.search(regex, val)
                if matcher is not None:
                    match = matcher.group(0)
                    if match is not None:
                        entry['mapped'] = match
                        return entry
            return None
        return self.map(mapLambda)

    def mapKey(self, key):
        """
        MAPPER: return the value of the supplied key as the entry. If the value is NO dict, it is stored in a new dict with the key 'mapped'
        
        :param key to be used for mapping
        :keep a new entry with the value of the supplied key; set None, if the key is not found
        """
        def mapLambda(entry):
            newitem = None
            val = entry[key]
            if val is not None:
                if isinstance(val,dict):
                    newitem = dict(val)
                else:
                    newitem = {}
                    newitem['mapped'] = val
                newitem['event'] = entry['event']
            return newitem
        return self.map(mapLambda)

    def mapList(self, index, lazy=False):
        """
        MAPPER: expect an entry with key 'mapped' and replace the entry with the val at the index given.
        If the value is NO dict, it is stored in a new dict with the key 'mapped'
        
        :param index to be accessed
        :param lazy True: leave entry as it is, if key 'mapped' is not found; False: set entry to None
        :keep the entry from the array; set None, if not found
        """
        def mapLambda(entry):
            newitem = None
            val = entry.get('mapped',None)
            if val is None:
                if lazy:
                    newitem = entry
            else:
                val = val[index]
                if val is not None:
                    if isinstance(val,dict):
                        newitem = dict(val)
                    else:
                        newitem = {}
                        newitem['mapped'] = val
                    newitem['event'] = entry['event']
            return newitem
        return self.map(mapLambda) 

    def before(self, beforeTime):
        """
        FILTER: decides whether the entry was created BEFORE the given beforeTime. Done by a string compare.
        beforeTime may be any prefix of a valid time, e.g. '2019-05-13' or '2019-05-13 08:'
        
        :param beforeTime we want to get look at entries created before that time
        :keep the entry, if condition applies; set to None otherwise
        """
        if self.entry is not None:
            if self.entry['time'] > beforeTime:
                self.entry = None
        return self

    def after(self, afterTime):
        """
        FILTER: decides whether the entry was created AFTER the given afterTime. Done by a string compare.
        afterTime may be any prefix of a valid time, e.g. '2019-05-13' or '2019-05-13 08:'
        
        :param afterTime we want to get look at entries created after that time
        :keep the entry, if condition applies; set to None otherwise
        """
        if self.entry is not None:
            if self.entry['time'] < afterTime:
                self.entry = None
        return self

    def reset(self):
        """
        FILTER: resets the entry to the original event (usually a dict)
        
        :keep the entry as it was before any mapping
        """
        if self.entry is not None:
            self.entry = self.original
        return self
    
    def uniqueKey(self, key):
        """
        FILTER: Block all entries whose key's value has been encountered earlier
        MAY BE USED IN A CHAIN ONLY ONCE!
        
        :keep the entry if it is the first one; set to None otherwise
        """
        if self.entry is not None:
            val = self.entry[key]
            alreadyUsed = Entry.unique.get(val, None)
            if alreadyUsed is None:
                Entry.unique[val] = True
            else:
                self.entry = None
        return self
    
    def assemble(self, key):
        """
        COLLECT: save key-value pair in the list assembled
        
        :keep the entry as it is
        """
        if self.entry is not None:
            val = self.entry[key]
            if val is not None:
                if self.assembled is None:
                    self.assembled = list()
                self.assembled.append((key,val))
        return self

    groupTimePrefix = {
            'm': 7,
            'd': 10,
            'h': 13,
            'm': 16,
            's': 19
        }

    '''
    def group(granularity, event, groupStore):
        prefix = Entry.groupTimePrefix.get(granularity, 10)
        key = event['time'][:prefix]
        val = groupStore.get(key)
        if val is None:
            val = list()
            groupStore[key] = val
        val.append(event)
        
    def count(groupStore):
        countStore = {}
        for key, val in groupStore.items():
            countStore[key] = len(val)
        return countStore
    '''
    
    def groupCount(self, granularity, groupStore):
        """
        REDUCE: counts the number of entries grouped by a given granularity
        
        :param granularity of grouping: 'd' (day), 'h' (hour), 'm' (minute), 's' (second)
        :param groupStore where to save the counts. This is a dict, whose keys are the used times (reduced to the given granularity)
        """
        if self.entry is not None:
            prefix = Entry.groupTimePrefix.get(granularity, 10)
            key = self.entry['time'][:prefix]
            val = groupStore.get(key)
            if val is None:
                val = 1
            else:
                val = val + 1
            groupStore[key] = val
        return self

    def keyCount(self, key, groupStore, pattern=None):
        """
        REDUCE: counts the number of entries grouped by the value of a given key
        
        :param the key, whose value is used for grouping
        :param groupStore where to save the counts.
        """
        if self.entry is None:
            self.entry = None
        else:
            keyVal = self.entry[key]
            if pattern is not None:
                matcher = re.search(pattern, keyVal)
                if matcher is not None:
                    match = matcher.group(1)
                    if match is not None:
                        keyVal = match
            countVal = groupStore.get(keyVal)
            if countVal is None:
                countVal = 1
            else:
                countVal = countVal + 1
            groupStore[keyVal] = countVal
        return self

    def showEvent(self):
        """
        REDUCE: show the values keys 'time' and the original event of an entry 
        """
        if self.entry is not None:
            print('{:25} {}'.format(self.entry['time'], self.entry['event']))
        return self
    
    def showKey(self, key):
        """
        REDUCE: show the values of the key 'time' and a user-supplied key
        
        :param key whose value should be shown
        """
        if self.entry is not None:
            print('{:25} {}'.format(self.entry['time'], self.entry[key]))
        return self
    
    def showEntry(self):
        """
        REDUCE: show the complete entry with all of its keys
        
        :param entry to be used
        """
        if self.entry is not None:
            print('{:25} {}'.format(self.entry['time'], str(self.entry)))
        return self
    
    def showAssembled(self):
        """
        REDUCE: show all assembled key-value pairs
        
        :keep the entry as it is
        """
        if self.entry is not None and self.assembled is not None:
            print('{:25} {}'.format(self.entry['time'], str(self.assembled)))
            self.assembled = None
        return self

