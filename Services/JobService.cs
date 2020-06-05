using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace distributed_chaos_csharp.Services
{
    public class JobService
    {
        private JobService()
        {
        }

        public static JobService Instance { get { return SingletonNested.instance; } }

        private class SingletonNested
        {
            // Explicit static constructor to tell C# compiler
            // not to mark type as beforefieldinit
            static SingletonNested()
            {
            }

            internal static readonly JobService instance = new JobService();
        }
    }
}
}
