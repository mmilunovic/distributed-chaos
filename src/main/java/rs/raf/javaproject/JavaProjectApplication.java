package rs.raf.javaproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import rs.raf.javaproject.config.MyConfig;
import rs.raf.javaproject.model.Node;
import rs.raf.javaproject.repository.Database;
import rs.raf.javaproject.service.MessageService;
import rs.raf.javaproject.service.NodeService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@SpringBootApplication
//@EnableScheduling
public class JavaProjectApplication {
	@Autowired
	private MyConfig config;

	@Autowired
	private MessageService messageService;
	@Autowired
	private Database databese;

	@Autowired
	private NodeService nodeService;

	public static void main(String[] args) {
		SpringApplication.run(JavaProjectApplication.class, args);
	}

	@PostConstruct
	public void init() {

        Node me = new Node(config.getIp(), config.getPort());
        config.setMe(me);
        databese.getAllNodes().put(config.getMe().getId(), config.getMe());
        databese.setFractalMap(new HashMap<>());
        databese.setData(new ArrayList<>());

        Node node = messageService.sendBootstrapHail();

        if (node.getIp() == null) {
            messageService.sendBootstrapNew();
        } else {
            Collection<Node> allNodesInfo = messageService.sendGetAllNodes(node);
            for (Node otherNode : allNodesInfo)
                databese.getAllNodes().put(otherNode.getId(), otherNode);
            for (Node nodeInfo : allNodesInfo)
                databese.getAllNodes().put(nodeInfo.getId(), nodeInfo);

            // TODO: Treba da uzme sve jobove

            messageService.sendBootstrapNew();

            Node predecessor = databese.getPredecessor();
            System.out.println("Pre mene:" + predecessor.getId());
            // TODO: ako ima jedan cvor
            if (predecessor != null) {
                messageService.sendNewNode(predecessor, me);
            }


            System.out.println("Koristio sam " + node + " za ukljucenje u mrezu");

        }
        nodeService.restructure();
    }
}
