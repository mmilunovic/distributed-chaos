from flask import Flask
from service import NodeService

app = Flask(__name__, template_folder="./templates")

baseRoute = "/api/node"

@app.route(baseRoute + "/info", methods = ["GET"])
def getInfo():
    pass

@app.route(baseRoute + "/allNodes", methods = ["GET"])
def getAllNodes():
    pass

@app.route(baseRoute + "/ping/<string:nodeID>", methods = ["GET"])
def ping(nodeID):
    return "Evo me na: " + nodeID

@app.route(baseRoute + "/quit", methods = ["POST"])
def quit():
    pass

@app.route(baseRoute + "/left/<string:nodeID>", methods = ["GET"])
def left(nodeID):
    pass

@app.route(baseRoute + "/new/<string:nodeID>", methods = ["GET"])
def newNode(nodeID):
    pass

@app.route(baseRoute + "/backup", methods = ["POST"])
def saveBackup():
    pass

@app.route(baseRoute + "/backup/<string:jobID>/<string:regionID>", methods = ["GET"])
def getBackup(jobID, regionID):
    return "Evo me sad na: " + jobID + " " + regionID

@app.route(baseRoute + "/allJobs", methods = ["GET"])
def getAllJobs():
    pass