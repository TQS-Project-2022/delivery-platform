package pt.ua.deliveryplatform.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pt.ua.deliveryplatform.dto.CreateOrderDto;
import pt.ua.deliveryplatform.model.ProductOrder;
import pt.ua.deliveryplatform.services.ProductOrderService;

@Controller
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("orders")
public class ProductOrderController {

    private final ProductOrderService productOrderService;

    @PostMapping("create")
    public ResponseEntity<ProductOrder> createOrder(@RequestBody CreateOrderDto createOrderDto) {
        return productOrderService.createOrder(createOrderDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductOrder> getOrderById(@PathVariable Integer id){
        return productOrderService.getOrderById(id);
    }

    @PatchMapping("/confirm/{id}")
    public ResponseEntity<ProductOrder> confirmDelivery(@PathVariable Integer id){
        return this.productOrderService.confirmOrderDelivery(id);
    }

}
