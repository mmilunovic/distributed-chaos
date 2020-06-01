package rs.raf.javaproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import rs.raf.javaproject.config.MyConfig;
import rs.raf.javaproject.model.Node;
import rs.raf.javaproject.repository.Database;
import rs.raf.javaproject.service.MessageService;

import javax.annotation.PostConstruct;
import java.util.Collection;

@SpringBootApplication
//@EnableScheduling
public class JavaProjectApplication {
	@Autowired
	private MyConfig config;

	@Autowired
	private MessageService messageService;
	@Autowired
	private Database databese;

	public static void main(String[] args) {
		SpringApplication.run(JavaProjectApplication.class, args);
	}

	@PostConstruct
	public void init(){

		Node me = new Node(config.getIp(), config.getPort());
		config.setMe(me);

		databese.addNode(config.getMe());

		Node node = messageService.sendBootstrapHail();

		if(node.getIP() == null){
			messageService.sendBootstrapNew();
		}else{
			Collection<Node> allNodesInfo = messageService.sendGetAllNodes(node);
			databese.addNodes(allNodesInfo);

			// TODO: Treba da uzme sve jobove

			messageService.sendBootstrapNew();

			Node predecessor = databese.getPredecessor();
			System.out.println("Pre mene:" + predecessor.getID());
			// TODO: ako ima jedan cvor
			if(predecessor != null){
				messageService.sendNewNode(predecessor, me);
			}



		}


	}

}
