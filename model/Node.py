
class Node:

    def __init__(self):
        self.ip = None
        self.port = None
        self.region = None

    def __int__(self, ip, port):
        self.ip = ip
        self.port = port

    def __init__(self, id):
        self.ip, self.port = id.split(":")

    def getID(self):
        return self.ip + ":" + self.port

    