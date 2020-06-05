using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace distributed_chaos_csharp.Controllers
{
    [Route("api/nodes")]
    [ApiController]
    public class NodeController : ControllerBase
    {

        [HttpGet("/info")]
        public Node info()
        {
            return NodeService.Instance.info();
        }

        [HttpGet("/allNodes")]
        public IEnumerable<Node> allNodes()
        {
            return NodeService.Instance.allNodes();
        }

        [HttpGet("/ping/{nodeID}")]
        public bool ping(string nodeID)
        {
            return NodeService.Instance.ping(nodeID);
        }

        [HttpGet("/quit")]
        public void quit()
        {
            NodeService.Instance.quit();
        }

        [HttpGet("/left/{nodeID}")]
        public void left(string nodeID)
        {
            NodeService.Instance.left(nodeID);
        }

        [HttpGet("/new/{nodeID}")]
        public void newNode(string nodeID)
        {
            NodeService.Instance.newNode(nodeID);
        }

        [HttpPost("/backup")]
        public void saveBackup(BackupInfo backupInfo)
        {
            NodeService.Instance.saveBackup(backupInfo);
        }

        [HttpGet("/backup/{jobID}/{regionID}")]
        public BackupInfo getBackup(string jobID, string regionID)
        {
            return NodeService.Instance.getBackup(NodeService.Instance.getDatabase().getInfo().getId(), jobID, regionID);
        }

        [HttpGet("/allJobs")]
        public Collection<Job> getAllJobs()
        {
            return NodeService.Instance.getAllJobs();
        }


        [HttpGet("/test/{nodeID}")]
        public IEnumerable<string> getTest(String nodeID)
        {
            return RestUtil.getBackupNodeIDForNode(nodeID);
        }
    }
}
