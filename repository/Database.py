from config.Config import  *


class Database:

    __instance = None

    allNodes = {}
    allJobs = {}
    data = []

    myConfig = Config.getInstance()

    @staticmethod
    def getInstance():
        if Database.__instance == None:
            Database()
        return Database.__instance

    def __init__(self):
        if Database.__instance != None:
            raise  Exception("This class is singleton!")
        else:
            Database.__instance = self

    def getInfo(self):
        return self.myConfig.me

    def getAllNodes(self):
        return self.allNodes.values()

    def getAllJobs(self):
        return self.allJobs.values()

