// Edited By Kaushal Patel
package com.aerotravel.flightticketbooking.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import com.aerotravel.flightticketbooking.model.Passenger;
import com.aerotravel.flightticketbooking.repository.AirportRepository;
import com.aerotravel.flightticketbooking.repository.PassengerRepository;
import com.aerotravel.flightticketbooking.services.PassengerService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PassengerServiceTest {

	@Autowired
	private PassengerService passengerService;

	 @MockBean
	    private PassengerRepository passengerRepository;
	    Passenger passenger = new Passenger(1,"Kaushal","Patel","9089527950","N323242","kp@gmail.com","100 Jersey City");

	   //Test for method called	`
	    @Test
	    public void isCalled(){

	        when(passengerRepository.save(passenger)).thenReturn(passenger);
	        passengerService.savePassenger(passenger);
	        verify(passengerRepository, times(1)).save(passenger);
	    }

	    //Test for save method
	    @Test
	    public void savePassengerTest(){

	        when(passengerRepository.save(passenger)).thenReturn(passenger);
	        assertEquals(passenger, passengerService.savePassenger(passenger));
	        passengerService.savePassenger(passenger);
	    }

	    // Test for getting all passengers as list
	    @Test
	    public void allPassengerTest() {
	    	when(passengerRepository.findAll()).thenReturn(Stream.of(passenger).collect(Collectors.toList()));
	    	assertEquals(1,passengerService.getAllPassengers().size());
	    }

	    //Test for getting empty list by get all passenger
	    @Test
	    public void allPassengerEmptyListTest() {
	    	List<Passenger> emptyPassenger = new ArrayList<Passenger>();
	    	when(passengerRepository.findAll()).thenReturn(emptyPassenger);
	    	assertEquals(0, passengerService.getAllPassengers().size());
	    }
	    //Test for finding passenger by ID
	    @Test
	    public void allPassengertByIDTest() {
	    	when(passengerRepository.findById((long) 1)).thenReturn(Optional.of(passenger));
	    	assertEquals(passenger,passengerService.getPassengerById((long) 1));
	    }

	    //Test for deleting by ID
	    @Test
	    public void deletePassengerTest() {
	    	passengerService.deletePassengerById((long) 1);
	    	verify(passengerRepository, times(1)).deleteById((long) 1);
	    }


	    //Test for null value
	    @Test(expected = IllegalArgumentException.class)
	    public void findPassengerByIDTestThrowsIllegalArgumentException(){
	    	passengerService.getPassengerById((long)-1);
	    }

	  //Test for invalid passenger by ID
	    @Test
	    public void invalidPassengertByIDTest() {
	    	when(passengerRepository.findById((long) 100)).thenReturn(Optional.empty());
	    	assertEquals(null,passengerService.getPassengerById((long) 100));
	    }

	  //Test for pagination
	    @Test
	    public void pageinationTest() {
	    	Page<Passenger> expectedPassengerpage = passengerService.getAllPassengersPaged(1);
	    	when(passengerRepository.findAll(PageRequest.of(1,5, Sort.by("firstName")))).thenReturn(expectedPassengerpage);
	    	assertEquals(expectedPassengerpage,passengerService.getAllPassengersPaged(1));
	    }
	    //Test for empty pagination
	    @Test
	    public void emptyPageinationTest() {
	    	when(passengerRepository.findAll(PageRequest.of(1,5, Sort.by("firstName")))).thenReturn(null);
	    	assertEquals(null,passengerService.getAllPassengersPaged(2));
	    }


}
