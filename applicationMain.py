
from controller.NodeController import app
from model.Node import *
from config.Config import Config
from service.NodeService import NodeService
from repository.Database import Database
from service.MessageService import MessageService


if __name__ == "__main__":

    myConfig = Config.getInstance()

    database = Database.getInstance()

    nodeService = NodeService.getInstance()

    messageService = MessageService.getInstance()

    node = messageService.sendBootstrapHail()

    if node.get("ip") is None:
        messageService.sendBootstrapNew(myConfig.me)
    else:
        print("I got: ", node)
        allNodes = messageService.sendGetAllNodes(node)
        for node in allNodes:
            database.allNodes.append(node)


        #messageService.sendNewNode(myConfig.me)

        messageService.sendBootstrapNew(myConfig.me)


    app.run(port=myConfig.port)





























