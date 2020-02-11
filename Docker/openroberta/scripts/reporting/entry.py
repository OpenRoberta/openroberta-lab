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
    
    def __init__(self, entry, printer=None):
        normalize(entry)
        self.entry = entry
        self.original = entry
        self.printer = printer

    def filter(self, lambdaFct, negate=False):
        """
        FILTER: retain entry if lambda returns True, discard otherwise
        
        :param lambdaFct to be called as lambdaFct(self.entry)
        :param negate True: block entry if lambda is False; and vice versa
        :keep the entry, if lambda returns True; set None otherwise
        """
        if self.entry is not None:
            if lambdaFct(self.entry) == negate:
                self.entry = None
        return self
    
    def filterRegex(self, key, *regexes, negate=False):
        """
        FILTER: decides whether a regex matches the value of a key of an entry 
        
        :param key to be used for filtering
        :param *regexes matches one of the regex the value of the key (the regex are OR-ed)?
        :param negate True: block entry if regexes match; and vice versa
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
        if key is None:
            return self
        else:
            return self.filter(filterLambda,negate=negate)

    def filterVal(self, key, *vals, substring=True, negate=False):
        """
        FILTER: decides whether a string is a substring of the value of a key of an entry 
        
        :param key to be used for filtering
        :param *vals is one of these strings a substring of the key's value (the vals are OR-ed)?
        :param substring True: block entry if one of the vals is a substring if the key's value; False: must be equel
        :param negate True: block entry if one of the vals match, as described above; and vice versa
        :keep the entry, if one of the strings is a substring; set None otherwise
        """
        def filterLambda(entry):
            keyVal = entry[key]
            if keyVal is not None:
                for val in vals:
                    if substring and val in keyVal or val == keyVal:
                        return True
            return False
        if key is None:
            return self
        else:
            return self.filter(filterLambda,negate=negate)

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

    def reset(self, strong=True):
        """
        FILTER: resets the entry to the original event (usually a dict)
        
        :param strong if True, reset the entry even if it was blocked by a preceding filter; if False, reset only if not blocked before
        :keep the entry as it was before any mapping depending on 'strong'
        """
        if strong or self.entry is not None:
            self.entry = self.original
        return self
    
    def exec(self, lambdaFct):
        """
        SIDE EFFECT: execute a parameterless lambda (e.g. to reset a store)
        
        :param lambdaFct to be called
        :keep the entry as it is
        """
        if self.entry is not None:
            lambdaFct()
        return self
    
    def uniqueKey(self, key, keyStore):
        """
        FILTER: Block all entries whose key's value has been encountered earlier
        
        :keep the entry if it is the first one; set to None otherwise
        """
        if self.entry is not None:
            val = self.entry[key]
            if keyStore.has(val):
                self.entry = None
            else:
                keyStore.put(val, val)
        return self
    
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
    
    def groupStore(self, store):
        """
        REDUCE: counts the number of entries grouped by a given time granularity (min, h, d, m)
        
        :param groupStore where to save the counts. This is a Store object, that only counts values (given as 'keys')
        """
        if self.entry is not None:
            time = self.entry['time']
            store.group(time)
        return self

    def keyStore(self, keyUsedForGrouping, store, pattern=None):
        return self.keyValStore(keyUsedForGrouping, keyUsedForGrouping, store, pattern=pattern)
        
    def keyValStore(self, keyUsedForGrouping, keyWhoseValueIsStored, store, pattern=None):
        """
        REDUCE: counts the number of entries grouped by the value of a given key
        
        :param the key, whose value is used for grouping
        :param store where to save the counts.
        """
        if self.entry is not None:
            key = self.entry[keyUsedForGrouping]
            val = self.entry.get(keyWhoseValueIsStored, None)
            if pattern is not None:
                matcher = re.search(pattern, key)
                if matcher is not None:
                    match = matcher.group(1)
                    if match is not None:
                        key = match
            store.put(key, val)
        return self

    def closeKey(self, key, store):
        if self.entry is not None:
            val = self.entry.get(key, None)
            if val is not None:
                store.close(str(val))
        return self
        
    def showEvent(self):
        """
        REDUCE: show the values keys 'time' and the original event of an entry 
        """
        if self.entry is not None:
            self.printer('{:25} {}'.format(self.entry['time'], self.entry['event']))
        return self
    
    def showKey(self, key):
        """
        REDUCE: show the values of the key 'time' and a user-supplied key
        
        :param key whose value should be shown
        """
        if self.entry is not None:
            self.printer('{:25} {}'.format(self.entry['time'], self.entry[key]))
        return self
    
    def showEntry(self, printer=None):
        """
        REDUCE: show the complete entry with all of its keys
        
        :param entry to be used
        :param printer (optional printer to use
        """
        if self.entry is not None:
            if printer is not None:
                printer('{:25} {}'.format(self.entry['time'], str(self.entry)))
            else:
                self.printer('{:25} {}'.format(self.entry['time'], str(self.entry)))
        return self
    
def normalize(entry):
    if isinstance(entry,dict):
        flattenMessage(entry)
        simplifyArg(entry)
        deduplicateSessionId(entry)
        mapHeaderFields(entry)

def flattenMessage(entry):
    message = entry.get('message', None)
    if message is None or not isinstance(entry,dict):
        raise Exception('invalid entry: ' + str(entry))
    del entry['message']
    entry.update(message)

def simplifyArg(entry):
    args = entry.get('args', None)
    if args is None:
        raise Exception('invalid entry: ' + str(entry))
    if isinstance(args,list):
        if len(args) != 1:
            raise Exception('invalid entry: ' + str(entry))
        entry['args'] = args[0]

def deduplicateSessionId(entry):
    action = entry.get('action', None)
    if action is not None:
        if action == 'ServerStart':
            Entry.serverRestartNumber += 1
        sessionId = entry['sessionId']
        if sessionId is not None:
            entry['sessionId'] = str(Entry.serverRestartNumber) + '-' + sessionId
        if action == 'SessionDestroy':
            entry['sessionId'] = str(Entry.serverRestartNumber) + '-' + str(entry['args']['sessionId'])

def mapHeaderFields(entry):
    args = entry.get('args', None)
    if args is not None:
        browser = args.get('Browser', None)
        if browser is not None:
            args['Browser'] = mapBrowser(browser)
        os = args.get('OS', None)
        if os is not None:
            args['OS'] = mapOS(os)

def mapBrowser(browser):
    if bool(re.match('CHROME', browser, re.I)):
        return 'chrome'
    if bool(re.match('APPLE_WEB_KIT', browser, re.I)):
        return 'appleWebKit'
    if bool(re.match('FIREFOX', browser, re.I)):
        return 'firefox'
    if bool(re.match('SAFARI', browser, re.I)):
        return 'safari'
    if bool(re.match('EDGE', browser, re.I)):
        return 'edge'
    if bool(re.match('OPERA', browser, re.I)):
        return 'opera'
    if bool(re.match('OPERA_MOBILE', browser, re.I)):
        return 'opera'
    if bool(re.match('MOBILE_SAFARI', browser, re.I)):
        return 'safari'
    if bool(re.match('VIVALDI', browser, re.I)):
        return 'vivaldi'
    if bool(re.match('BOT', browser, re.I)):
        return 'bot'
    if bool(re.match('IE.*11', browser, re.I)):
        return 'ie11'
    return browser

def mapOS(os):
    return os