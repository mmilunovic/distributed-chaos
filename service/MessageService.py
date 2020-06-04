import json

from config.Config import Config
import requests
from model.Node import NodeEncoder
from model.SuccessorTable import SuccessorTable
from model.PredecessorTable import PredecessorTable


class MessageService:

    __instance = None

    myConfig = Config.getInstance()
    successorTable = SuccessorTable.getInstance()
    predecessorTable = PredecessorTable.getInstance()

    @staticmethod
    def getInstance():
        if MessageService.__instance == None:
            MessageService()
        return MessageService.__instance

    def __init__(self):
        if MessageService.__instance != None:
            raise  Exception("This class is singleton!")
        else:
            MessageService.__instance = self


    def sendBootstrapHail(self):
        try:
            response = requests.get("http://localhost:8080/api/bootstrap/hail")
            return response.json()
        except Exception as e:
            print(e)
            return e

    def sendBootstrapNew(self, node):
        try:
            body = NodeEncoder().encode(node)
            response = requests.put("http://localhost:8080/api/bootstrap/new",
                                    data=body,
                                    headers={'content-type': 'application/json'})
            return response.json()
        except Exception as e:
            print(e)
            return e

    def sendGetAllNodes(self, node):
        try:
            response = requests.get("http://" + str(node.get("ip")) + ":" +
                                    str(node.get("port")) + "/api/node/allNodes")
            return response.json()

        except Exception as e:
            print("sendGetAllNodes error: ", e)
            return e


    def sendNewNode(self, newNode):

        for node in self.successorTable.getBroadcastingNodes():
            # TODO: Thread jebem ti lebac Milose
            notifyNewNodeResponse = requests.get("http://" + node.getID() +
                                                 "/api/node/new/" + newNode.getID())

        for node in self.predecessorTable.getBroadcastingNodes():
            notifyNewNodeResponse = requests.get("http://" + node.getID() +
                                                 "/api/node/new/" + newNode.getID())
