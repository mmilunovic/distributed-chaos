from repository.Database import Database


class PredecessorTable:
    pass

    __instance = None

    database = Database.getInstance()

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

    def reconstructTable(self):
        self.table = []

        tmpList = list(self.database.allNodes.values())

        myPos = tmpList.index(self.database.getInfo())

        size = len(tmpList)

        for step in range(1, size):
            succPos = ((myPos - step) + size) % size
            self.table.append(tmpList[succPos])
            step *= 2

        print("Predecessor table: ", [e.getID() for e in self.table])


    def getBroadcastingNodes(self):
        broadcastingNodes = []

        try:
            broadcastingNodes.append(self.table[0])
            broadcastingNodes.append(self.table[1])
            broadcastingNodes.append(self.table[2])
        except IndexError:
            pass

        return broadcastingNodes

