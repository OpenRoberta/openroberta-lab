import Ed
Ed.EdisonVersion = Ed.V2
Ed.DistanceUnits = Ed.CM
Ed.Tempo = Ed.TEMPO_SLOW
obstacleDetectionOn = False
Ed.LineTrackerLed(Ed.ON)
Ed.ReadClapSensor()
Ed.ReadLineState()
Ed.TimeWait(250, Ed.TIME_MILLISECONDS)

___n = 1000
___b = True
___nl = Ed.List(3, [0,0,0])

def ____drive():
    global ___n, ___b, ___nl
    _diffDrive(Ed.FORWARD, ___n, ___n)
    Ed.ReadClapSensor()
    _diffDrive(Ed.BACKWARD, ___n, ___n)
    Ed.ReadClapSensor()
    _diffDrive(Ed.FORWARD, ___n, Ed.DISTANCE_UNLIMITED)
    _diffDrive(Ed.BACKWARD, ___n, Ed.DISTANCE_UNLIMITED)
    Ed.Drive(Ed.STOP, Ed.SPEED_1, 1)
    Ed.ReadClapSensor()
    _diffTurn(Ed.SPIN_RIGHT, ___n, ___n)
    Ed.ReadClapSensor()
    _diffTurn(Ed.SPIN_LEFT, ___n, ___n)
    Ed.ReadClapSensor()
    _diffTurn(Ed.SPIN_RIGHT, ___n, Ed.DISTANCE_UNLIMITED)
    _diffTurn(Ed.SPIN_LEFT, ___n, Ed.DISTANCE_UNLIMITED)
    _diffCurve(Ed.FORWARD, ___n, ___n, ___n)
    Ed.ReadClapSensor()
    _diffCurve(Ed.BACKWARD, ___n, ___n, ___n)
    Ed.ReadClapSensor()
    _diffCurve(Ed.FORWARD, ___n, ___n, Ed.DISTANCE_UNLIMITED)
    _diffCurve(Ed.BACKWARD, ___n, ___n, Ed.DISTANCE_UNLIMITED)

def ____sounds():
    global ___n, ___b, ___nl
    Ed.PlayTone(8000000/1000, ___n)
    Ed.TimeWait(___n, Ed.TIME_MILLISECONDS)
    Ed.ReadClapSensor()
    Ed.PlayTone(4000000/261, 2000)
    Ed.TimeWait(2000, Ed.TIME_MILLISECONDS)
    Ed.ReadClapSensor()
    Ed.PlayTone(4000000/293, 1000)
    Ed.TimeWait(1000, Ed.TIME_MILLISECONDS)
    Ed.ReadClapSensor()
    Ed.PlayTone(4000000/329, 500)
    Ed.TimeWait(500, Ed.TIME_MILLISECONDS)
    Ed.ReadClapSensor()
    Ed.PlayTone(4000000/349, 250)
    Ed.TimeWait(250, Ed.TIME_MILLISECONDS)
    Ed.ReadClapSensor()
    Ed.PlayTone(4000000/391, 125)
    Ed.TimeWait(125, Ed.TIME_MILLISECONDS)
    Ed.ReadClapSensor()

def ____lights():
    global ___n, ___b, ___nl
    Ed.LeftLed(Ed.ON)
    Ed.RightLed(Ed.OFF)

def ____action():
    global ___n, ___b, ___nl
    ____move()
    ____drive()
    ____sounds()
    ____lights()

def ____move():
    global ___n, ___b, ___nl
    _motorOn(0, ___n, Ed.DISTANCE_UNLIMITED)
    _motorOn(1, ___n, Ed.DISTANCE_UNLIMITED)
    Ed.DriveLeftMotor(Ed.STOP, Ed.SPEED_1, 1)
    Ed.ReadClapSensor()
    Ed.DriveRightMotor(Ed.STOP, Ed.SPEED_1, 1)
    Ed.ReadClapSensor()

____action()
___soundfile1 = Ed.TuneString(7,"c8e8g8z")
Ed.PlayTune(___soundfile1)
while (Ed.ReadMusicEnd() == Ed.MUSIC_NOT_FINISHED):
    pass
Ed.ReadClapSensor()
___soundfile2 = Ed.TuneString(7,"g8e8c8z")
Ed.PlayTune(___soundfile2)
while (Ed.ReadMusicEnd() == Ed.MUSIC_NOT_FINISHED):
    pass
Ed.ReadClapSensor()


def _diffCurve(direction, leftSpeed, rightSpeed, distance):
    if (leftSpeed < 0): 
        _leftSpeed = _shorten(-leftSpeed)
        _reverseLeft = True
    else: 
        _leftSpeed = _shorten(leftSpeed)
        _reverseLeft = False
    if (rightSpeed < 0): 
        _rightSpeed = _shorten(-rightSpeed)
        _reverseRight = True
    else: 
        _rightSpeed = _shorten(rightSpeed)
        _reverseRight = False
    if (_leftSpeed > _rightSpeed):
        if (_rightSpeed == 0):
            Ed.DriveRightMotor(Ed.STOP, 0, 0)
        else:
            Ed.DriveLeftMotor(_getDirection(direction, _reverseLeft), _leftSpeed, Ed.DISTANCE_UNLIMITED)
            Ed.DriveRightMotor(_getDirection(direction, _reverseRight), _rightSpeed, Ed.DISTANCE_UNLIMITED)
        Ed.DriveLeftMotor(_getDirection(direction, _reverseLeft), _leftSpeed, distance)
        if (distance != Ed.DISTANCE_UNLIMITED): 
            Ed.Drive(Ed.STOP, 1, 1)
    elif (_leftSpeed < _rightSpeed):
        if (_leftSpeed == 0):
            Ed.DriveLeftMotor(Ed.STOP, 0, 0)
        else:
            Ed.DriveRightMotor(_getDirection(direction, _reverseRight), _rightSpeed, Ed.DISTANCE_UNLIMITED)
            Ed.DriveLeftMotor(_getDirection(direction, _reverseLeft), _leftSpeed, Ed.DISTANCE_UNLIMITED)
        Ed.DriveRightMotor(_getDirection(direction, _reverseRight), _rightSpeed, distance)
        if (distance != Ed.DISTANCE_UNLIMITED): 
            Ed.Drive(Ed.STOP, 1, 1)
    else:
        if (_leftSpeed == 0):
            Ed.Drive(Ed.STOP, 1, 1)
        else:
            Ed.Drive(_getDirection(direction, _reverseLeft), _leftSpeed, distance)

def _diffDrive(direction, speed, distance):
    _reverse = False
    if speed < 0:
        _reverse = True
        _speed = _shorten(-speed)
    else: 
        _speed = _shorten(speed)
    if (_speed == 0): 
        Ed.Drive(Ed.STOP, 1, 1)
    else: 
        Ed.Drive(_getDirection(direction, _reverse), _speed, distance)

def _diffTurn(direction, speed, degree):
    _reverse = False
    if speed < 0:
        _reverse = True
        _speed = _shorten(-speed)
    else: 
        _speed = _shorten(speed)
    if (_speed == 0): 
        Ed.Drive(Ed.STOP, 1, 1)
    elif _reverse:
        if direction == Ed.SPIN_RIGHT: 
            Ed.Drive(Ed.SPIN_LEFT, _speed, degree)
        else: 
            Ed.Drive(Ed.SPIN_RIGHT, _speed, degree)
    else: 
        Ed.Drive(direction, _speed, degree)

def _getDirection(dir, reverse):
    if reverse:
        if (dir == Ed.FORWARD): 
            return Ed.BACKWARD
        else: 
            return Ed.FORWARD
    else: 
        return dir

def _motorOn(motor, power, distance):
    _dir = Ed.FORWARD
    _reverse = False
    if (power < 0):
        _power = _shorten(-power)
        _reverse = True
    else: _power = _shorten(power)
    if (motor == Ed.MOTOR_LEFT):
        if (_power == 0): 
            Ed.DriveLeftMotor(Ed.STOP, 0, 0)
        else: 
            Ed.DriveLeftMotor(_getDirection(_dir, _reverse), _power, distance)
    if (motor == Ed.MOTOR_RIGHT):
        if (_power == 0): 
            Ed.DriveRightMotor(Ed.STOP, 0, 0)
        else: 
            Ed.DriveRightMotor(_getDirection(_dir, _reverse), _power, distance)

def _shorten(num): 
    return ((num+5)/10)
