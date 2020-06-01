package rs.raf.bootstrap.service;

import org.springframework.stereotype.Service;
import rs.raf.bootstrap.model.Node;
import rs.raf.bootstrap.repository.IRepository;
import rs.raf.bootstrap.repository.InMemoryRepository;

@Service
public class BootstrapService {

    private IRepository repository;

    public BootstrapService() {
        this.repository = InMemoryRepository.getInstance();
    }

    public boolean save(Node node){
        return repository.save(node);
    }

    public boolean remove(Node node){
        return repository.remove(node);
    }

    public Node getConnection(){
        return repository.getRandomNode();
    }

}
