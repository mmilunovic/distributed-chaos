using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using distributed_chaos_csharp.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace distributed_chaos_csharp.Controllers
{
    [Route("api/jobs")]
    [ApiController]
    public class JobController : ControllerBase
    {

        [HttpGet("/status")]
        public Collection<StatusResponse> status()
        {
            return JobService.Instance.status();
        }

        [HttpGet("/status/{jobID}")]
        public StatusResponse status(string jobID)
        {
            return JobService.Instance.status(jobID);
        }

        [HttpGet("/status/{jobID}/{regionID}")]
        public StatusResponse status(string jobID, string regionID)
        {
            return JobService.Instance.status(jobID, regionID);
        }

        [HttpPut("/start")]
        public void start(Job job)
        {
            JobService.Instance.start(job);
        }

        [HttpGet("/result/{jobID}")]
        public ResultResponse result(string jobID)
        {
            return JobService.Instance.result(jobID);
        }

        [HttpGet("/result/{jobID}/{regionID}")]
        public ResultResponse result(string jobID, string regionID)
        {
            return JobService.Instance.result(jobID, regionID);
        }


        [HttpGet("/stopAll/{jobID}")]
        public void stopAll(string jobID)
        {
            JobService.Instance.stopAll(jobID);
        }

        [HttpDelete("/{jobID}")]
        public void deleteJob(String jobID)
        {
            JobService.Instance.deleteJob(jobID);
        }
    }
}
