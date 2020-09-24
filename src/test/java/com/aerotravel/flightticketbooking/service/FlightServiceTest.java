//Raunak Kharbanda - rk776

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.aerotravel.flightticketbooking.model.Airport;
import com.aerotravel.flightticketbooking.model.Flight;
import com.aerotravel.flightticketbooking.repository.FlightRepository;
import com.aerotravel.flightticketbooking.services.FlightService;
//import com.aerotravel.flightticketbooking.services.FlightServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest

public class FlightServiceTest {

	@Autowired
	private FlightService flightService;
	
	@MockBean
	private FlightRepository flightRepository;
	Airport airportDept = new Airport("EWR","Newark Airport","Newark","United States","New Jersey");
	Airport airportArrival = new Airport("LV","Las Vegas Airport","Las Vegas","United States","Nevada");
	Flight flight = new Flight(7,"NJ-LV11", airportDept, airportArrival, 500.00, "2020-03-30", "2020-03-30");
	
	@Test
	public void testing_findAllFlights()
	{
		when(flightRepository.findAll()).thenReturn(Stream.of(flight).collect(Collectors.toList()));
		assertEquals(1,flightService.getAllFlights().size());
	}
	
	@Test
	public void testing_callFunction()
	{
		when(flightRepository.save(flight)).thenReturn(flight);
		flightService.saveFlight(flight);
		verify(flightRepository, times(1)).save(flight);
	}
	
	@Test
	public void testing_findFlightById()
	{
		when(flightRepository.findById((long) 7)).thenReturn(Optional.of(flight));
		assertEquals(flight,flightService.getFlightById(7));
	}
	
	@Test
	public void testing_saveFlight()
	{
		when(flightRepository.save(flight)).thenReturn(flight);
		assertEquals(flight, flightService.saveFlight(flight));
		flightService.saveFlight(flight);
	}
	
	@Test
	public void testing_deleteFlight()
	{
		flightService.deleteFlightById(7);
		verify(flightRepository, times(1)).deleteById((long) 7);
	}
	
	@Test
	public void testing_findFlightById_invalid()
	{
		when(flightRepository.findById((long) 45)).thenReturn(Optional.empty());
		assertEquals(null, flightService.getFlightById(45));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testing_findFlighttByIdThrowsIllegalArgumentException()
	{
		flightService.getFlightById(-1);
	}
	
	@Test
    public void testing_emptyFlightList() {
    	List<Flight> emptyFlight = new ArrayList<Flight>();
    	when(flightRepository.findAll()).thenReturn(emptyFlight);
    	assertEquals(0, flightService.getAllFlights().size());
    }
	
	@Test
    public void testing_pagination() {
    	Page<Flight> expectedFlightpage = flightService.getAllFlightsPaged(1);
    	when(flightRepository.findAll(PageRequest.of(1,5, Sort.by("flightName")))).thenReturn(expectedFlightpage);
    	assertEquals(expectedFlightpage,flightService.getAllFlightsPaged(1));
    }
	
	@Test
    public void testing_emptyPage() {
    	when(flightRepository.findAll(PageRequest.of(1,5, Sort.by("flightName")))).thenReturn(null);
    	assertEquals(null, flightService.getAllFlightsPaged(2));
    }
	
}
