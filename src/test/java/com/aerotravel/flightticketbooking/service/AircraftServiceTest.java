//Commented by Aniket Panhale
package com.aerotravel.flightticketbooking.service;

import com.aerotravel.flightticketbooking.model.Aircraft;
import com.aerotravel.flightticketbooking.repository.AircraftRepository;
import com.aerotravel.flightticketbooking.services.AircraftService;
import com.aerotravel.flightticketbooking.services.servicesimpl.AircraftServiceImpl;



import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.AssertionErrors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AircraftServiceTest {

    @Autowired
    private AircraftService aircraftService;

    @MockBean
    private AircraftRepository aircraftRepository;

    Aircraft aircraft = new Aircraft("Boing", "737", 700);


    //test if the saveAircraft() method.
    @Test
    public void saveAircraftTest(){
        when(aircraftRepository.save(aircraft)).thenReturn(aircraft);
        assertEquals(aircraft, aircraftService.saveAircraft(aircraft));
        aircraftService.saveAircraft(aircraft);
    }

    
    //test if the aircraftRepository.save() is called
    @Test
    public void isCalled(){
        Aircraft aircraft = new Aircraft("Boing", "737", 700);
        when(aircraftRepository.save(aircraft)).thenReturn(aircraft);
        aircraftService.saveAircraft(aircraft);
        verify(aircraftRepository, times(1)).save(aircraft);
    }

//test if the getAircraftIDTest() method.
    @Test
    public void getAircraftIDTest() {
    Aircraft aircraft = new Aircraft(111, "Boing","A380", 500);
   when(aircraftRepository.findById((long) 111)).thenReturn(Optional.of(aircraft));
   assertEquals(aircraft,aircraftService.getAircraftById((long) 111));
    }

//test if the getAllAircraftsTest() method.
    @Test
    public void getAllAircraftTest() {
    	when(aircraftRepository.findAll()).thenReturn(Stream.of(aircraft).collect(Collectors.toList()));
    	assertEquals(1,aircraftService.getAllAircrafts().size());
    }
    
//test for getting empty list by get all aircraft
    @Test
    public void allAircarftEmptyListTest() {
    	List<Aircraft> emptyAircraft = new ArrayList<Aircraft>();
    	when(aircraftRepository.findAll()).thenReturn(emptyAircraft);
    	assertEquals(0,aircraftService.getAllAircrafts().size());
    	}
    
//test if the deleteAircraftById() method.
    @Test
    public void deleteAircraftByIdTest() {
    	aircraftService.deleteAircraftById((long) 111);
    	verify(aircraftRepository, times(1)).deleteById((long) 111);
    
    }
   
//test for null value
    @Test(expected = IllegalArgumentException.class)
    public void findAircraftByIDTestThrowsIllegalArgumentException() {
    	aircraftService.getAircraftById((long) -1);
    }
    
//test for invalid aircraft by ID
    @Test
    public void invalidAircraftByIDTest() { 
    	when(aircraftRepository.findById((long) 222)).thenReturn(Optional.empty());
    	assertEquals(null,aircraftService.getAircraftById((long) 222));
    }
    
//test for pagination
    @Test
    public void pageinationTest() {
    	Page<Aircraft> expectedAircraftpage = aircraftService.getAllAircraftsPaged(1);
    	when(aircraftRepository.findAll(PageRequest.of(1,5, Sort.by("manufacturer")))).thenReturn(expectedAircraftpage);
    	assertEquals(expectedAircraftpage,aircraftService.getAllAircraftsPaged(1));
    
    }
    
//test for empty pagination
    @Test
    public void emptypaginationTest() {
    	when(aircraftRepository.findAll(PageRequest.of(1,5, Sort.by("manufacturer")))).thenReturn(null);
    	assertEquals(null,aircraftService.getAllAircraftsPaged(2));
    }
    
    }
    

