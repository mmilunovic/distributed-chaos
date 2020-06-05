using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace distributed_chaos_csharp.Services
{
    [Route("api/delegate")]
    [ApiController]
    public class DelegateController : ControllerBase
    {
        [HttpGet("/{nodeID}/jobs/{jobID}")]
        public IEnumerable<Point> getJobResultFromNode(@PathVariable String nodeID, @PathVariable String jobID)
        {
            return JobService.Instance.getJobResultFromNode(nodeID, jobID);
        }

        [HttpGet("/{nodeID}/jobs/{jobID}/{regionID}")]
        public IEnumerable<Point> getRegionResultFromNode(@PathVariable String nodeID, @PathVariable String jobID, @PathVariable String regionID)
        {
            return JobService.Instance.getRegionResultFromNode(nodeID, jobID, regionID);
        }

        [HttpGet("/{nodeID}/status/{jobID}")]
        public RegionStatusResponse myStatus(@PathVariable String nodeID, @PathVariable String jobID)
        {
            return JobService.Instance.myStatus(nodeID, jobID);
        }

        [HttpGet("/{nodeID}/status/{jobID}/{regionID}")]
        public RegionStatusResponse status(@PathVariable String nodeID, @PathVariable String jobID, @PathVariable String regionID)
        {
            return JobService.Instance.myStatus(nodeID, jobID, regionID);
        }

        [HttpGet("/{nodeID}/backup/{jobID}/{regionID}")]
        public BackupInfo getBackup(@PathVariable String nodeID, @PathVariable String jobID, @PathVariable String regionID)
        {
            return JobService.Instance.getBackup(nodeID, jobID, regionID);
        }
    }
}
