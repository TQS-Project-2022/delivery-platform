package pt.ua.deliveryplatform.services;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pt.ua.deliveryplatform.dto.CreateOrderDto;
import pt.ua.deliveryplatform.model.ProductOrder;
import pt.ua.deliveryplatform.model.Rider;
import pt.ua.deliveryplatform.repositories.ProductOrderRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductOrderService {

    private final ProductOrderRepository productOrderRepository;

    private final RiderService riderService;


    public ResponseEntity<ProductOrder> createOrder(CreateOrderDto createOrderDto){
        List<Rider> availableRiders = riderService.getAllAvailableRiders();
        if(availableRiders.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ProductOrder productOrder = new ProductOrder();
        productOrder.setProductId(createOrderDto.getProductId());
        riderService.toggleRider(availableRiders.get(0).getId());
        productOrder.setRider(availableRiders.get(0));
        productOrderRepository.save(productOrder);

        return new ResponseEntity<>(productOrder, HttpStatus.CREATED);
    }

    public ResponseEntity<ProductOrder> getOrderById(Integer id){
        Optional<ProductOrder> findOrder = productOrderRepository.findById(id);
        if(findOrder.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(findOrder.get(), HttpStatus.OK);
    }

    public ResponseEntity<ProductOrder> confirmOrderDelivery(Integer id){
        Optional<ProductOrder> findOrder = productOrderRepository.findById(id);
        if(findOrder.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ProductOrder order = findOrder.get();
        order.setDelivered(true);
        riderService.toggleRider(order.getRider().getId());
        productOrderRepository.save(order);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
