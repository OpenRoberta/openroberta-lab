'''
keeps track of key-value pairs in different ways:
- counts them (to count the number of logins, e.g.)
- optionally stores a set of values (the used robots, e.g.)
- optionally stores a list of values (the sequence of actions executed, e.g.)
- optionally closes a key (makes sense to collect still opened sessions, e.g.) 

- optionally the data can be grouped by a time interval: month, day, hour, minute
  grouping implies, that occurrences are counted only

@author: rbudde
'''

__groupTimePrefix = {
        'm': 7,
        'd': 10,
        'h': 13,
        'm': 16,
        's': 19
    }

def getPrefix(granularity, time):
    prefix = __groupTimePrefix.get(granularity, 10)
    return time[:prefix]

class Item:
    def __init__(self, storeSet=False, storeList=False):
        self.counter = 0
        self.storeSet = set() if storeSet else None
        self.storeList = list() if storeList else None
        self.state = 'open'
        
    def put(self, val):
        self.counter += 1
        if self.storeSet is not None:
            self.storeSet.add(val)
        if self.storeList is not None:
            self.storeList.append(val) # or (key,val)?

class Store:
    def __init__(self, storeSet=False, storeList=False, groupBy=None):
        self.data = {}
        self.storeSet = storeSet
        self.storeList = storeList
        self.groupBy = groupBy
        self.totalKeyCounter = 0
        self.openKeyCounter = 0

    def show(self, header):
        print(header)
        for id, data in self.data.items():
            if data.state == 'open':
                print('{:10s} : {}'.format(id, str(data)))
        print(header)
        print('opened: ' + str(self.totalKeyCounter))
        print('open:   ' + str(self.openKeyCounter))

    def put(self, key, val):
        if self.groupBy is not None:
            raise Exception('no grouping with a put-store')
        item = self.data.get(key,None)
        if item is None:
            item = Item(storeSet=self.storeSet, storeList=self.storeList)
            self.data[key] = item
            self.totalKeyCounter += 1
            self.openKeyCounter += 1
        item.put(val)   

    def group(self, time):
        if self.groupBy is None:
            raise Exception('grouping needs a groupBy-store')
        prefix = getPrefix(self.groupBy, time)
        item = self.data.get(prefix,None)
        if item is None:
            item = Item(storeSet=None, storeList=None)
            self.data[prefix] = item
            self.totalKeyCounter += 1
            self.openKeyCounter += 1
        item.put(None) # only count 

    def has(self, key):
        item = self.data.get(key,None)
        return item is not None

    def close(self, key):
        item = self.data.get(key)
        if item is not None:
            self.openKeyCounter -= 1
            item.state = 'close'