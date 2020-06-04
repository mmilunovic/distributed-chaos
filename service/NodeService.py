from repository.Database import Database
from model.Node import Node
from model.SuccessorTable import  SuccessorTable
from model.PredecessorTable import PredecessorTable
from service.MessageService import MessageService

import threading

class NodeService:

    __instance = None

    database = Database.getInstance()
    succesorTable = SuccessorTable.getInstance()
    predecessorTable = PredecessorTable.getInstance()
    messageService = MessageService.getInstance()


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

        if nodeID not in self.database.allNodes.keys():
            print("Node service radim new node")
            newNode = Node(nodeID)

            # TODO: take lock

            self.database.allNodes[newNode.getID()] = newNode

            self.succesorTable.reconstructTable()
            self.predecessorTable.reconstructTable()

            # TODO: release lock

            self.messageService.sendNewNode(newNode)

            self.restructure()


    def restructure(self):
        # TODO: take lock
        print("Doing restructuring...")


    def getAllJobs(self):
        return self.database.getAllJobs()





















