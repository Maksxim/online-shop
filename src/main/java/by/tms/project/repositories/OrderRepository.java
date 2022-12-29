package by.tms.project.repositories;

import by.tms.project.entities.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository <Order,Integer> {


}
