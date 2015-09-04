#!/usr/bin/env python

from distutils.core import setup
from xml.etree.ElementTree import ElementTree

et=ElementTree(file='../OpenRobertaParent/pom.xml')

# TODO: convert README.md to long_desc
# https://gist.github.com/aubricus/9184003#file-setup_snippet-py

setup(name='robertalab',
      version=et.findtext("{http://maven.apache.org/POM/4.0.0}version"),
      description='lab.open-roberta.org connector for ev3dev.org',
      author='Stefan Sauer',
      author_email='ensonic@google.com',
      url='https://www.open-roberta.org/',
      scripts=['robertalab.py'],
      packages=['roberta'],
      package_data={'roberta': ['ter-*.p??']},
      install_requires=['python-bluez', 'python-dbus', 'python-ev3dev']
     )
