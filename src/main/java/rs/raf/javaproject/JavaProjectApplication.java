package rs.raf.javaproject;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import rs.raf.javaproject.config.MyConfig;
import rs.raf.javaproject.model.*;
import rs.raf.javaproject.repository.Database;
import rs.raf.javaproject.service.MessageService;
import rs.raf.javaproject.service.NodeService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
@EnableScheduling
public class JavaProjectApplication {
	@Autowired
	private MyConfig config;

	@Autowired
	private MessageService messageService;

	@Autowired
	private Database databese;

	@Autowired
    private SuccessorTable successorTable;

	@Autowired
    private PredecessorTable predecessorTable;

	@Autowired
	private NodeService nodeService;

	private static ConfigurableApplicationContext context;

	public static void main(String[] args) {
	    context = SpringApplication.run(JavaProjectApplication.class, args);
	}

    public static void exitThread() {
        SpringApplication.exit(context,() -> 0);
    }

    @PostConstruct
	public void init() {

        Node me = new Node(config.getIp(), config.getPort());

       config.setMe(me);
        databese.getAllNodes().put(config.getMe().getId(), config.getMe());
        databese.setData(new ArrayList<>());
        databese.setBackups(new ConcurrentHashMap<>());

        Node node = messageService.sendBootstrapHail();

        if (node.getIp() == null) {
            messageService.sendBootstrapNew();
        } else {
            Collection<Node> allNodesInfo = messageService.sendGetAllNodes(node);
            for (Node nodeInfo : allNodesInfo)
                databese.getAllNodes().put(nodeInfo.getId(), nodeInfo);

            Collection<Job> allJobsInfo = messageService.sendGetAllJobs(node);
            System.out.println(allJobsInfo);

            for(Job job : allJobsInfo){
                databese.getAllJobs().put(job.getId(), job);
            }

            predecessorTable.reconstructTable();
            successorTable.reconstructTable();

            messageService.sendBootstrapNew();

            messageService.sendNewNode(me);

        }


        Job job = new Job();
        job.setId("job1");
        job.setHeight(100);
        job.setWidth(100);
        job.setProportion(0.5);
        job.getStartingPoints().add(new Point(25.0,25.0));
        job.getStartingPoints().add(new Point(75.0,25.0));
        job.getStartingPoints().add(new Point(50.0,75.0));

        databese.getAllJobs().put(job.getId(), job);

        nodeService.restructure();

    }


}
