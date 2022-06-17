package pt.ua.deliveryplatform.repositories;

import org.springframework.data.repository.CrudRepository;
import pt.ua.deliveryplatform.model.Rider;

public interface RiderRepository extends CrudRepository<Rider, Integer> {
}
