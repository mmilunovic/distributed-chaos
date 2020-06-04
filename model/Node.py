import json
from json import JSONEncoder

class Node:

    ip = ""
    port = 0
    region = None

    def __init__(self, ip, port):
        self.ip = ip
        self.port = port

    def __str__(self):
        return self.ip + ":" + self.port

    def getID(self):
        return self.ip + ":" + self.port

class NodeEncoder(JSONEncoder):
    def default(self, o):
        return o.__dict__
