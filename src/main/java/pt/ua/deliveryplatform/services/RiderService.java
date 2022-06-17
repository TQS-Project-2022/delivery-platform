package pt.ua.deliveryplatform.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pt.ua.deliveryplatform.dto.CreateRiderDto;
import pt.ua.deliveryplatform.model.Rider;
import pt.ua.deliveryplatform.repositories.RiderRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RiderService {

    private final RiderRepository riderRepository;

    public ResponseEntity<Iterable<Rider>> getAllRiders(){
        return new ResponseEntity<>(this.riderRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<Rider> addNewRider(CreateRiderDto createRiderDto){
        Rider rider = new Rider();
        rider.setName(createRiderDto.getName());

        this.riderRepository.save(rider);

        return new ResponseEntity<>(rider, HttpStatus.CREATED);
    }

    public ResponseEntity<Rider> getRiderById(Integer id){
        Optional<Rider> findRider = this.riderRepository.findById(id);

        if(findRider.isEmpty()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(findRider.get(), HttpStatus.OK);
        }
    }

}
