package by.tms.project.repositories;

import by.tms.project.entities.Order;
import by.tms.project.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository <Order,Integer>, PagingAndSortingRepository<Order,Integer> {

    Page<Order> findAll(Pageable pageable);

}
