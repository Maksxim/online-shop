package by.tms.project.repositories;

import by.tms.project.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product,Integer>, PagingAndSortingRepository<Product,Integer> {

    List<Product> findAll();

    Page<Product> findAll(Pageable pageable);

}
