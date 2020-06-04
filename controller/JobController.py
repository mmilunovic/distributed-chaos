from flask import Flask
from controller.NodeController import app
from service import JobService

baseRoute = "/api/jobs"

@app.route(baseRoute + "/status", methods = ["GET"])
def status():
    return "Jobs endpoint"

@app.route(baseRoute + "/status/<string:jobID>", methods = ["GET"])
def status(jobID):
    pass

@app.route(baseRoute + "/status/<string:jobID>/<string:regionID>", methods = ["GET"])
def status(jobID, regionID):
    return "Evo me sad na: " + jobID + " " + regionID


@app.route(baseRoute + "/start", methods = ["PUT"])
def start():
    pass

@app.route(baseRoute + "/result/<string:jobID>", methods = ["GET"])
def result(jobID):
    pass

@app.route(baseRoute + "/<string:jobID>", methods = ["GET"])
def myWork(jobID):
    pass

@app.route(baseRoute + "/stopAll/<string:jobID>", methods = ["POST"])
def stopAll(jobID):
    pass

@app.route(baseRoute + "/<string:jobID>", methods = ["DELETE"])
def deleteJob(jobID):
    pass



