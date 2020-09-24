// Controller Test edited by Karan Katyal, Raunak Kharbanda, Aniket Panhale, Kaushal Patel
package com.aerotravel.flightticketbooking.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import com.aerotravel.flightticketbooking.model.Aircraft;
import com.aerotravel.flightticketbooking.model.Airport;
import com.aerotravel.flightticketbooking.model.Flight;
import com.aerotravel.flightticketbooking.model.Passenger;

import com.aerotravel.flightticketbooking.services.AirportService;
import com.aerotravel.flightticketbooking.services.FlightService;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;
import java.util.List;


import org.springframework.security.test.context.support.WithMockUser;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
public class MainControllerTest {

	@Autowired
	private MockMvc mvc;

	@Mock
	private  FlightService flightService;

	@Mock 
	private AirportService airportService;

	@InjectMocks
	private MainController mainController;


	Airport deptAirport = new Airport(1,"EWR","Newark Airport","Newark","New Jersey","United States");
	Airport arrivalAirport = new Airport(38,"LAX","Los Angeles Airport","Los Angeles","California","US");
	Aircraft aircraft = new Aircraft(20,"Airbus","A340",340);
	Flight flight = new Flight(87,"NEW-LAX983", deptAirport, arrivalAirport, 350, "2020-06-04", "2020-06-04");
	Flight expectedValue = new Flight(38, " NEWLAX111", deptAirport, arrivalAirport, 1200, "2020-04-12", "2020-04-12");
	Passenger person = new Passenger(131,"Arya","Stark","2683871656","T436275","aryas@gmail.com","Winterfell");
	Passenger person2 = new Passenger(141,"John","Paul","1111111111","W123","jp@gmail.com","Backyard lane");
	Airport airport = new Airport(101,"IGI","India Airport","Delhi","Delhi","India");
	Aircraft aircraft1 = new Aircraft(21,"Airbus","A360",360);

	@Before
	public void setup() {
		List<Passenger> passengers = new ArrayList<Passenger>();
		passengers.add(person);
		flight.setPassengers(passengers);

	}


//***************************************** INTEGRATION TEST CASES KARAN KATYAL*****************************************************
	@Test
	//TEST ID-11
	public void verifyFlightWhenExists() throws Exception {

		when(flightService.getFlightById(flight.getFlightId())).thenReturn(flight);

		//
		mvc.perform(post("/flight/booking/verify")
				.param("flightId", Long.toString(flight.getFlightId()))
				.param("passengerId",Long.toString(person.getPassengerId()))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print());

	}
	@Test
	//TEST ID-12
	public void verifyFlightWhenExistsGet() throws Exception {

		when(flightService.getFlightById(flight.getFlightId())).thenReturn(flight);

		//
		mvc.perform(get("/flight/booking/verify")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.view().name("verifyBooking"));

	}

	//Integration Testing of Controller for finding flight (Karan Katyal)
	@Test
	//TEST ID-13
	public void flightFound() throws Exception{

		mvc.perform(post("/flight/find")
				.param("departureAirport", deptAirport.getAirportId().toString())
				.param("destinationAirport",arrivalAirport.getAirportId().toString() )
				.param("departureDate", flight.getDepartureDate())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("searchFlight"))
		.andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("airports"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("flights"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("AirportError"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("notFound"))
		.andDo(MockMvcResultHandlers.print());			
	}
	@Test
	//TEST ID-14
	public void flightNotFound() throws Exception{

		mvc.perform(post("/flight/find")
				.param("departureAirport", deptAirport.getAirportId().toString())
				.param("destinationAirport",arrivalAirport.getAirportId().toString())
				.param("departureDate", "2020-04-20")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("searchFlight"))
		.andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("airports"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("AirportError"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("flights"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("notFound"))
		.andDo(MockMvcResultHandlers.print());			
	}
	@Test
	//TEST ID-15
	public void noSameDeptAndArivalAirport() throws Exception{

		mvc.perform(post("/flight/find")
				.param("departureAirport", deptAirport.getAirportId().toString())
				.param("destinationAirport",deptAirport.getAirportId().toString() )
				.param("departureDate", expectedValue.getDepartureDate())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("searchFlight"))
		.andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("airports"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("AirportError"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("flights"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("notFound"))
		.andDo(MockMvcResultHandlers.print());		
	}

	//Integration testing for controller flight booking (Karan Katyal)

	//TEST ID-16
	@Test
	@WithMockUser(username="mike",roles={"AGENT"})
	public void flightBooked() throws Exception{

		mvc.perform(post("/flight/booking")
				.param("departureAirport", deptAirport.getAirportId().toString())
				.param("destinationAirport",arrivalAirport.getAirportId().toString() )
				.param("departureDate", flight.getDepartureDate())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("bookFlight"))
		.andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("AirportError"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("notFound"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("flights"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("airports"))
		.andDo(MockMvcResultHandlers.print());		
	}
	//TEST ID-17
	@Test
	@WithMockUser(username="mike",roles={"AGENT"}) 
	public void noSameDeptAndArivalAirportFlightBook() throws Exception{

		mvc.perform(post("/flight/booking")
				.param("departureAirport", deptAirport.getAirportId().toString())
				.param("destinationAirport",deptAirport.getAirportId().toString() )
				.param("departureDate", expectedValue.getDepartureDate())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("bookFlight"))
		.andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("AirportError"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("airports"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("notFound"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("flights"))
		.andDo(MockMvcResultHandlers.print());			
	}
	//TEST ID-18
	@Test
	@WithMockUser(username="mike",roles={"AGENT"})
	public void flightNotBooked() throws Exception{

		mvc.perform(post("/flight/booking")
				.param("departureAirport", deptAirport.getAirportId().toString())
				.param("destinationAirport",arrivalAirport.getAirportId().toString() )
				.param("departureDate", "2020-04-20")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("bookFlight"))
		.andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("AirportError"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("notFound"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("flights"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("airports"))
		.andDo(MockMvcResultHandlers.print());			
	}
	
	
	//********************************************INTEGRATION TEST CASES RAUNAK KHARBANDA*********************************************

	//Test for adding an airport
	@Test
	@WithMockUser(username="john",roles={"ADMIN"})
	public void addAnAirport() throws Exception {
		mvc.perform(post("/airport/add")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.sessionAttr("airport", airport))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("airports"));
	}

	// Tests for verification procedures in booking a flight 
	@Test
	public void flightFoundAndPassengerFoundForVerify() throws Exception{

		//person.setFlight(flight);
		mvc.perform(post("/flight/booking/verify")
				.param("flightId", Long.toString(flight.getFlightId()))
				.param("passengerId", Long.toString(person.getPassengerId()))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("verifyBooking"))
		.andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("flight"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("passenger"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("notFound"))
		.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void flightFoundAndPassengerNotFoundForVerify() throws Exception{

		mvc.perform(post("/flight/booking/verify")
				.param("flightId", Long.toString(flight.getFlightId()))
				.param("passengerId", "1")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("verifyBooking"))
		.andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("flight"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("passenger"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("notFound"))
		.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void flightNotFoundForVerify() throws Exception{

		mvc.perform(post("/flight/booking/verify")
				.param("flightId", Long.toString(expectedValue.getFlightId()))
				.param("passengerId", Long.toString(person.getPassengerId()))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("verifyBooking"))
		.andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("flight"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("passenger"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("notFound"))
		.andDo(MockMvcResultHandlers.print());
	}
	
	
	//*******************************************INTEGRATION TEST CASES ANIKET PANHALE***********************************************

	//Adding Aircraft Controller Test
	@Test
	@WithMockUser(username="john",roles={"ADMIN"})
	public void addAircraft() throws Exception {
		mvc.perform(post("/aircraft/add")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.sessionAttr("aircraft", aircraft))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("aircrafts"));

	}

	//Integration Testing of Controller for adding flight (Aniket Panhale)
	@Test
	@WithMockUser(username="john",roles={"ADMIN"})
	public void flightAdd() throws Exception{
		
		flight.setArrivalTime("17:00");
		flight.setDepartureTime("15:00");
		mvc.perform(post("/flight/add")
				.param("departureAirport", deptAirport.getAirportId().toString())
				.param("destinationAirport", arrivalAirport.getAirportId().toString())
				.param("aircraft", Long.toString(aircraft.getAircraftId()))
				.param("arrivalTime", flight.getArrivalTime())
				.param("departureTime", flight.getDepartureTime())
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.sessionAttr("flight",flight))
		.andExpect(MockMvcResultMatchers.view().name("flights"))
		.andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("errors"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("sameAirportError"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("flight"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("aircrafts"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("airports"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("currentPage"))
		.andDo(MockMvcResultHandlers.print());			
	}
	
	@Test
	@WithMockUser(username="john",roles={"ADMIN"})
	public void noSameDeptAndArivalAirportFlightAdd() throws Exception{

		flight.setArrivalTime("17:00");
		flight.setDepartureTime("15:00");
		mvc.perform(post("/flight/add")
				.param("departureAirport", deptAirport.getAirportId().toString())
				.param("destinationAirport",deptAirport.getAirportId().toString() )
				.param("aircraft", Long.toString(aircraft.getAircraftId()))
						.param("arrivalTime", flight.getArrivalTime())
						.param("departureTime", flight.getDepartureTime())
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.sessionAttr("flight",flight))
		.andExpect(MockMvcResultMatchers.view().name("newFlight"))
		.andExpect(MockMvcResultMatchers.content().contentType("text/html;charset=UTF-8"))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("errors"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("flight"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("aircrafts"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("airports"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("sameAirportError"))
		.andDo(MockMvcResultHandlers.print());		
	}

	//Integration testing of controller for flight booking (Aniket Panhale)
	@Test
	@WithMockUser(username="mike",roles={"AGENT"})

	public void flightBookingNew() throws Exception {
		
		when(flightService.getFlightById(flight.getFlightId())).thenReturn(flight);
		mvc.perform(get("/flight/booking/new")
				.param("flightId", Long.toString(flight.getFlightId()))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.view().name("newPassenger"));

	}
	@Test
	@WithMockUser(username="mike",roles={"AGENT"})
	public void flightBookingCancel() throws Exception {
		when(flightService.getFlightById(flight.getFlightId())).thenReturn(flight);
		mvc.perform(post("/flight/booking/cancel")
				.param("passengerId", Long.toString(person2.getPassengerId()))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print());
	}	
//*******************************INTEGRATION TEST CASE KAUSHAL PATEL******************************************
		
	//Test Case for airport add get method
	@Test
	@WithMockUser(username="john",roles={"ADMIN"})
	public void airportAddGet() throws Exception{
		mvc.perform(get("/airport/add")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.sessionAttr("airport", airport))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.view().name("newAirport"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("airport"));
	}
	
	
	// Test for Add Aircraft get method
	@Test
	@WithMockUser(username="john",roles={"ADMIN"})
	public void aircraftAddGet() throws Exception{
		mvc.perform(get("/aircraft/add")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.sessionAttr("aircraft", aircraft1))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.view().name("newAircraft"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("aircraft"));
	}
	
	//Test for All Airport get method
	@Test
	@WithMockUser(username="john",roles={"ADMIN"})
	public void airportAllGet() throws Exception{
		mvc.perform(get("/all/airports")
				.param("pageNo","1" )
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.view().name("airports"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("airports"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("currentPage"));
	}
	
	//Test for All aircraft get method
	@Test
	@WithMockUser(username="john",roles={"ADMIN"})
	public void aircraftAllGet() throws Exception{
		mvc.perform(get("/all/aircrafts")
				.param("pageNo","1" )
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.view().name("aircrafts"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("aircrafts"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("currentPage"));
	}
	
	//Test for all flight get method
	@Test
	@WithMockUser(username="john",roles={"ADMIN"})
	public void flightAllGet() throws Exception{
		mvc.perform(get("/all/flights")
				.param("pageNo","1" )
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andDo(MockMvcResultHandlers.print())
		.andExpect(MockMvcResultMatchers.view().name("flights"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("flights"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("currentPage"));
	}
}
