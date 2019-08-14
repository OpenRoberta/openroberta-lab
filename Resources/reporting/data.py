'''
helper for log file analysis

@author: rbudde
'''
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