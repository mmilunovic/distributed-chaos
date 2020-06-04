from json import JSONEncoder

from flask import Flask, jsonify
from service.NodeService import NodeService
import json

app = Flask(__name__)

baseRoute = "/api/node"
nodeService = NodeService.getInstance()

@app.route(baseRoute + "/info", methods = ["GET"])
def getInfo():
    try:
        ret = nodeService.getInfo()
    except Exception as e:
        return e
    return jsonify(ip=ret.ip, port=ret.port)

@app.route(baseRoute + "/allNodes", methods = ["GET"])
def getAllNodes():
    try:
        ret = list(nodeService.getAllNodes())

        return jsonify([e.serialize() for e in ret])

    except Exception as e:
        return e

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
    nodeService.newNode(nodeID)
    return "Valjda je dobro"

@app.route(baseRoute + "/backup", methods = ["POST"])
def saveBackup():
    pass

@app.route(baseRoute + "/backup/<string:jobID>/<string:regionID>", methods = ["GET"])
def getBackup(jobID, regionID):
    return "Evo me sad na: " + jobID + " " + regionID

@app.route(baseRoute + "/allJobs", methods = ["GET"])
def getAllJobs():
    try:
        ret =  list(nodeService.getAlljobs())
        return jsonify([e.serialize() for e in ret])
    except Exception as e:
        return str(e)