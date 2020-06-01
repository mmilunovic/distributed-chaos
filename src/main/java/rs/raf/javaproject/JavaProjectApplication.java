package rs.raf.javaproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rs.raf.javaproject.config.MyConfig;
import rs.raf.javaproject.model.Node;
import rs.raf.javaproject.requests.ARequest;
import rs.raf.javaproject.requests.bootstrap.Hail;
import rs.raf.javaproject.requests.bootstrap.New;
import rs.raf.javaproject.requests.node.AllNodes;

import java.util.Collection;

@SpringBootApplication
public class JavaProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaProjectApplication.class, args);

		Hail request = new Hail();
		Node node = request.execute();

		if(node.getIP() == null){
			New newRequest = new New();
			newRequest.execute();

		}else{
			AllNodes allNodesRequest = new AllNodes(node);
			Collection<Node> allNodes = allNodesRequest.execute();

			System.out.println(allNodes);
		}


	}

}
