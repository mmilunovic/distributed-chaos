
from controller.NodeController import app
from model.SuccessorTable import SuccessorTable
from model.PredecessorTable import PredecessorTable
from model.Node import Node
from model.Job import Job

from repository.Database import Database
from config.Config import Config

from service.NodeService import NodeService
from service.MessageService import MessageService


import json

if __name__ == "__main__":

    myConfig = Config.getInstance()

    database = Database.getInstance()

    nodeService = NodeService.getInstance()

    messageService = MessageService.getInstance()

    successorTable = SuccessorTable.getInstance()

    predecessorTable = PredecessorTable.getInstance()

    node = messageService.sendBootstrapHail()

    if node.get("ip") is None:
        messageService.sendBootstrapNew(myConfig.me)
        database.allNodes[myConfig.me.getID] = myConfig.me

    else:
        print("I got: ", node)
        allNodes = messageService.sendGetAllNodes(node)

        for node in allNodes:
            node = Node(node['ip'] + ":" + str(node['port']))
            database.allNodes[node.getID()] = node

        # TODO: get all jobs
        database.allNodes[myConfig.me.getID] = myConfig.me

        predecessorTable.reconstructTable()
        successorTable.reconstructTable()

        messageService.sendBootstrapNew(myConfig.me)

        messageService.sendNewNode(myConfig.me)


    job = Job()
    job.id = "job2"
    job.proportion = 0.5
    job.height = 100
    job.width  = 100

    database.allNodes[job.id] = job

    nodeService.restructure()



    app.run(port=myConfig.port)





























