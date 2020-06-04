
class PredecessorTable:
    pass

    __instance = None

    @staticmethod
    def getInstance():
        if PredecessorTable.__instance == None:
            PredecessorTable()
        return PredecessorTable.__instance

    def __init__(self):
        if PredecessorTable.__instance != None:
            raise  Exception("This class is singleton!")
        else:
            PredecessorTable.__instance = self


    def getBroadcastingNodes(self):
        pass