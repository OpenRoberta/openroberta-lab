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

import numpy as np
import matplotlib.pyplot as plt

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
    printer = print
    
    def __init__(self, storeSet=False, storeList=False, groupBy=None):
        self.data = {}
        self.storeSet = storeSet
        self.storeList = storeList
        self.groupBy = groupBy
        self.totalKeyCounter = 0
        self.openKeyCounter = 0

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
        prefix = self.__getPrefix(self.groupBy, time)
        item = self.data.get(prefix,None)
        if item is None:
            item = Item(storeSet=None, storeList=None)
            self.data[prefix] = item
            self.totalKeyCounter += 1
            self.openKeyCounter += 1
        item.put(None) # only count
    
    # time structure is: e.g. 2020-08-01 00:00:09,188
    __groupStartIndex = {
        'm': 0,
        'd': 0,
        'h': 11,
        'm': 14,
        's': 17
    }

    __groupEndIndex = {
        'm': 7,
        'd': 10,
        'h': 13,
        'm': 16,
        's': 19
    }

    def __getPrefix(self, granularity, time):
        startIndex = self.__groupStartIndex.get(granularity, 0)
        endIndex = self.__groupEndIndex.get(granularity, 10)
        return time[startIndex:endIndex]

    def has(self, key):
        item = self.data.get(key,None)
        return item is not None

    def close(self, key):
        item = self.data.get(key, None)
        if item is not None:
            self.openKeyCounter -= 1
            item.state = 'close'

    def showOpen(self, header):
        Store.printer(header)
        for id, data in self.data.items():
            if data.state == 'open':
                Store.printer('{:10s} : {}'.format(id, str(data)))
        Store.printer(header)
        Store.printer('opened: ' + str(self.totalKeyCounter))
        Store.printer('open:   ' + str(self.openKeyCounter))
    
    def show(self, fmt = '{:25} {}', title = None):
        """
        show the content of a store
        
        :param fmt to be used for printing, default '{:25} {}'. E.g. '{},{}'
        """
        if title is not None:
            Store.printer('\n' + str(title))
        if not self.storeList and not self.storeSet:
            for key, val in self.data.items():
                Store.printer(fmt.format(key, val.counter))
        if self.storeList:
            for key, val in self.data.items():
                Store.printer(fmt.format(key, val.counter, val.storeList))
        if self.storeSet:
            for key, val in self.data.items():
                Store.printer(fmt.format(key, val.counter, val.storeSet))
    
    def showPie(self, title='pie plot', file='D:/downloads/pie.png'):
        """
        REDUCE: show the content of a store as a pie chart
        
        :param title title to be used
        :param file for the plot output
        """
        plt.close()
        keys = np.char.array(list(self.data.keys()))
        counters = np.array(list(map(lambda v: v.counter, self.data.values())))
        percent = 100.*counters/counters.sum()
        patches, texts = plt.pie(counters, startangle=90, radius=1.2)
        labels = ['{0} - {1:1.2f} %'.format(i,j) for i,j in zip(keys, percent)]
        patches, labels, dummy =  zip(*sorted(zip(patches, labels, counters), key=lambda x: x[2], reverse=True))
        plt.legend(patches, labels, loc='best', bbox_to_anchor=(-0.1, 1.), fontsize=8)
        #plt.title(title)
        plt.savefig(file, bbox_inches='tight')
        
    def showBar(self, title='bar plot', legend='best', file='D:/downloads/bar.png', xAxisNbins=None, type='bar'):
        """
        REDUCE: show the content of a store as a bar chart
        
        :param title title to be used
        :param file for the plot output
        :param type bar, scatter, plot
        """
        plt.close()
        keys = list(self.data.keys())
        counters = list(map(lambda v: v.counter, self.data.values()))
        if type == 'bar':
            bar = plt.bar(keys, counters, color='b')
        elif type == 'scatter':
            bar = plt.scatter(keys, counters)
        else:
            bar = plt.plot(keys, counters)
            
        plt.title(title)
        plt.xticks(keys)
        plt.tick_params(axis ='x', rotation = 90) 
        if xAxisNbins is not None:
            plt.locator_params(axis='x', nbins=xAxisNbins)
        if legend is not None:
            labels = ['{0} - {1:6d}'.format(i,j) for i,j in zip(keys, counters)]
            plt.legend(bar, labels, loc=legend)
        #plt.show()
        plt.savefig(file, bbox_inches='tight')
