#!/usr/bin/env python3

import argparse
import os
import queue
import sounddevice as sd
import vosk
import sys
import json

class RobertaSpeechRecognition:
  def __init__(self, lang = "de"):
    self.lang = lang
    path = os.path.join('voskModels', 'vosk-model-' + lang)
    if not os.path.exists(path):
        print ("Please download a model for your language from https://alphacephei.com/vosk/models")
        print ("and unpack as 'model' in the current folder.")
        exit(0)
    device_info = sd.query_devices(None, 'input')
    print(device_info)
    self.samplerate = int(device_info['default_samplerate'])
    self.model = vosk.Model(path)
    self.q = queue.Queue()


  def callback(self, indata, frames, time, status):
    """This is called (from a separate thread) for each audio block."""
    if status:
      print(status, file=sys.stderr)
    self.q.put(bytes(indata))


  def getRecognizedSpeech(self):
    with sd.RawInputStream(samplerate=self.samplerate, blocksize = 8000, device='default', dtype='int16',
                  channels=1, callback=self.callback):
       rec = vosk.KaldiRecognizer(self.model, self.samplerate)
       while True:
         data = self.q.get()
         if rec.AcceptWaveform(data):
           res = json.loads(rec.Result())
           return res.get('text').strip()


if __name__ == '__main__':
  args = sys.argv[1:]
  times = 1
  if len(args) > 0:
    times = int(args[0])
  if times < 1:
    times = 1
  rSR = RobertaSpeechRecognition()
  for x in range(times):
    result = rSR.getRecognizedSpeech()
    if result:
      print(result)
    else:
      print('nothing recognized!')