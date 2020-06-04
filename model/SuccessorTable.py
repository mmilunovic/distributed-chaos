

class SuccessorTable:
    pass

    __instance = None

    @staticmethod
    def getInstance():
        if SuccessorTable.__instance == None:
            SuccessorTable()
        return SuccessorTable.__instance

    def __init__(self):
        if SuccessorTable.__instance != None:
            raise  Exception("This class is singleton!")
        else:
            SuccessorTable.__instance = self


    def getBroadcastingNodes(self):
        pass