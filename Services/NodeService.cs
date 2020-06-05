namespace distributed_chaos_csharp.Controllers
{
    internal class NodeService
    {
        private NodeService()
        {
        }

        public static NodeService Instance { get { return SingletonNested.instance; } }

        private class SingletonNested
        {
            // Explicit static constructor to tell C# compiler
            // not to mark type as beforefieldinit
            static SingletonNested()
            {
            }

            internal static readonly NodeService instance = new NodeService();
        }
    }
}