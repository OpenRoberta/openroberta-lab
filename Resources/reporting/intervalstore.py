'''
helper for log file analysis

@author: rbudde
'''

class IntervalStore:
    def __init__(self):
        self.intervals = {}
        self.totalCounter = 0
        self.openCounter = 0

    def show(self, header):
        print(header)
        for id, data in self.intervals.items():
            if data.state == 'active':
                print('{:10s} : {}'.format(id, str(data)))
        print(header)
        print("intervals opened: " + str(self.totalCounter))
        print("intervals not yet closed: " + str(self.openCounter))

    def open(self, key, date):
        self.intervals[key] = ValInStore(date)
        self.totalCounter += 1
        self.openCounter += 1
        
    def close(self, key):
        val = self.intervals.get(key)
        if val is not None:
            self.openCounter -= 1
            val.close()
        
    def inc(self, key):
        val = self.intervals.get(key)
        if oldVal is None:
            raise Exception('inc key {} with prior open'.format(key))
        else:
            val.incr(1)

class ValInStore:
    def __init__(self, date):
        self.state = 'active'
        self.date = date
        self.count = 1

    def __str__(self):
        return '{:19s} {:25s} {:5d}'.format(self.state,self.date, self.count)

    def close(self):
        self.state = 'passive'
        
    def incr(self, val):
        self.count += val
    