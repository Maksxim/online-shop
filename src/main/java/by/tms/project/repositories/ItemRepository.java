package by.tms.project.repositories;

import by.tms.project.entities.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends CrudRepository <Item,Integer> {

}
