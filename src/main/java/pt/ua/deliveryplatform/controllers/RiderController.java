package pt.ua.deliveryplatform.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pt.ua.deliveryplatform.dto.CreateRiderDto;
import pt.ua.deliveryplatform.model.Rider;
import pt.ua.deliveryplatform.services.RiderService;

@Controller
@RequestMapping("riders")
@CrossOrigin
@RequiredArgsConstructor
public class RiderController {

    private final RiderService riderService;

    @GetMapping
    public ResponseEntity<Iterable<Rider>> getALlRiders(){
        return this.riderService.getAllRiders();
    }

    @PostMapping("create")
    public ResponseEntity<Rider> addNewRider(@RequestBody CreateRiderDto createRiderDto){
        return this.riderService.addNewRider(createRiderDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rider> getRiderById(@PathVariable Integer id){
        return this.riderService.getRiderById(id);
    }

}
