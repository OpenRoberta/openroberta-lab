import random

intentHelp = ['Sie konnen das Licht ein oder ausschalten!']
slotOn = ['licht an', 'lampe an', 'mache das licht an']
slotOff = ['licht aus', 'lampe aus', 'stelle das licht aus']
answerIntentWelcome = ['Hallo, mochten Sie das Licht ein oder ausschalten?', 'Guten Tag']
answerSlotOn = ['Das Licht wurde eingeschaltet', 'Das Licht ist an']
answerSlotOff = ['Das Licht wurde ausgeschaltet', 'Das Licht ist aus']
answerIntentStop = ['Tscho', 'Auf Wiedesehen']
intentStop = ['Beenden', 'Stop', 'Aus']
intentWelcome = []
intentLight = ['Licht', 'Lampe']
	
	
def main():
	while True:   
		myinput = input()
		if not myinput:
			print(random.choice(answerIntentWelcome))
		elif any(s.lower() in myinput.lower() for s in intentLight):
			if myinput.lower() in slotOn:
				print(random.choice(answerSlotOn))
			elif myinput in slotOff:
				print(random.choice(answerSlotOff))
			else:
				print("wrong slot")        
		elif any(s in myinput for s in intentStop):
			print(random.choice(answerIntentStop))
			exit()
		else: #generalHelp
			print(random.choice(intentHelp))
        
if __name__ == "__main__":
	main()
	