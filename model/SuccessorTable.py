from repository.Database import Database


class SuccessorTable:
    pass

    __instance = None

    database = Database.getInstance()
    table = []

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


    def reconstructTable(self):
        self.table = []

        tmpList = list(self.database.allNodes.values())

        myPos = tmpList.index(self.database.getInfo())

        size = len(tmpList)

        for step in range(1, size):
            succPos = (myPos + step) % size
            self.table.append(tmpList[succPos])
            step *= 2

        print("Successor table: ", [e.getID() for e in self.table])

    def getBroadcastingNodes(self):
        broadcastingNodes = []

        try:
            broadcastingNodes.append(self.table[0])
            broadcastingNodes.append(self.table[1])
            broadcastingNodes.append(self.table[2])
        except IndexError:
            pass

        return broadcastingNodes