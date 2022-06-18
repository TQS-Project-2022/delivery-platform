package pt.ua.deliveryplatform.repositories;

import org.springframework.data.repository.CrudRepository;
import pt.ua.deliveryplatform.model.ProductOrder;

public interface ProductOrderRepository extends CrudRepository<ProductOrder, Integer> {
}
