from model.Node import Node

class Config:

    __instance = None

    bootstrap = ""
    ip = ""
    port = ""
    me = None

    @staticmethod
    def getInstance():
        if Config.__instance == None:
            Config("localhost:8080", "localhost", 9090)
        return Config.__instance

    def __init__(self, bootstrap, ip, port):
        if Config.__instance != None:
            raise Exception("This class is singleton!")
        else:
            Config.__instance = self
            self.bootstrap = bootstrap
            self.ip = ip
            self.port = port
            self.me = Node(ip + ":" + str(port))

    def __str__(self):
        return self.ip + ":" + self.port


