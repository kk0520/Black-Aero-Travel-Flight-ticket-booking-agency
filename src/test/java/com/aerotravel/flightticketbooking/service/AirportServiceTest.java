// Aiport Service unit tests eduted by Karan Katyal, kk594@njit.edu
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

import com.aerotravel.flightticketbooking.model.Airport;
import com.aerotravel.flightticketbooking.repository.AirportRepository;
import com.aerotravel.flightticketbooking.services.AirportService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AirportServiceTest {
	
	@Autowired
	private AirportService airportService;
	
	 @MockBean
	    private AirportRepository airportRepository;
	    Airport airport = new Airport(1,"EWR","Newark Airport","Newark","New Jersey","United States");
	    
	   //Test for method called	 TEST ID-1`
	    @Test
	    public void isCalled(){
	    	
	        when(airportRepository.save(airport)).thenReturn(airport);
	        airportService.saveAirport(airport);
	        verify(airportRepository, times(1)).save(airport);
	    }
	    
	    //Test for save method TEST ID-2
	    @Test
	    public void saveAirportTest(){
	    	
	        when(airportRepository.save(airport)).thenReturn(airport);
	        assertEquals(airport, airportService.saveAirport(airport));
	        airportService.saveAirport(airport);
	    } 
	    
	    // Test for getting all airports as list TEST ID-3
	    @Test
	    public void allAirportTest() {
	    	when(airportRepository.findAll()).thenReturn(Stream.of(airport).collect(Collectors.toList()));
	    	List<Airport> outcomeAirport = airportService.getAllAirports();
	    	assertEquals(1, outcomeAirport.size());
	    	assertEquals("EWR", outcomeAirport.get(0).getAirportCode());
	    	assertEquals(new Integer(1), outcomeAirport.get(0).getAirportId());
	    	assertEquals("Newark Airport", outcomeAirport.get(0).getAirportName());
	    	assertEquals("Newark", outcomeAirport.get(0).getCity());
	    	assertEquals("New Jersey", outcomeAirport.get(0).getState());
	    	assertEquals("United States", outcomeAirport.get(0).getCountry());
	    }
	    
	    //Test for getting empty list by get all airport TEST ID-4
	    @Test
	    public void allAirportEmptyListTest() {
	    	List<Airport> emptyAirport = new ArrayList<Airport>();
	    	when(airportRepository.findAll()).thenReturn(emptyAirport);
	    	assertEquals(0, airportService.getAllAirports().size());
	    }
	    //Test for finding airport by ID TEST ID-5
	    @Test
	    public void allAirporttByIDTest() {
	    	when(airportRepository.findById(1)).thenReturn(Optional.of(airport));
	    	assertEquals(airport,airportService.getAirportById(1));
	    }
	    
	    //Test for deleting by ID TEST ID-6
	    @Test
	    public void deleteAirportTest() {
	    	airportService.deleteAirport(1);
	    	verify(airportRepository, times(1)).deleteById(1);
	    }
	    
	    
	    //Test for null value TEST ID-7
	    @Test(expected = IllegalArgumentException.class)
	    public void findAirportByIDTestThrowsIllegalArgumentException(){
	    	airportService.getAirportById(null);
	    }
	    
	  //Test for invalid airport by ID TEST ID-8
	    @Test
	    public void invalidAirporttByIDTest() {
	    	when(airportRepository.findById(100)).thenReturn(Optional.empty());
	    	assertEquals(null,airportService.getAirportById(100));
	    }
	    
	  //Test for pagination TEST-9
	    @Test
	    public void paginationTest() {
	    	Page<Airport> expectedAirportpage = airportService.getAllAirportsPaged(1);
	    	when(airportRepository.findAll(PageRequest.of(1,5, Sort.by("airportName")))).thenReturn(expectedAirportpage);
	    	assertEquals(expectedAirportpage,airportService.getAllAirportsPaged(1));
	    }
	    //Test for empty pagination TEST-10
	    @Test
	    public void emptyPaginationTest() {
	    	when(airportRepository.findAll(PageRequest.of(1,5, Sort.by("airportName")))).thenReturn(null);
	    	assertEquals(null,airportService.getAllAirportsPaged(2));
	    }


}

