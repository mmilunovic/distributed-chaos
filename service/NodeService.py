from repository.Database import Database


class NodeService:

    __instance = None

    database = Database.getInstance()

    @staticmethod
    def getInstance():
        if NodeService.__instance == None:
            NodeService()
        return NodeService.__instance

    def __init__(self):
        if NodeService.__instance != None:
            raise Exception("This class is singleton!")
        else:
            NodeService.__instance = self

    def getInfo(self):
        return self.database.getInfo()

    def getAllNodes(self):
        return self.database.getAllNodes()

    def newNode(self, nodeID):

        if nodeID not in self.database.allNodes:
            pass























