class BackupInfo:

    def __init__(self):
        self.jobID = None
        self.regionID = None
        self.timestamp = None
        self.data = None

    def __init__(self, jobID, regionID, timestamp, data):
        self.jobID = jobID
        self.regionID = regionID
        self.timestamp = timestamp
        self.data = data
        
    def getID(self):
        return self.jobID + ":" + self.regionID
