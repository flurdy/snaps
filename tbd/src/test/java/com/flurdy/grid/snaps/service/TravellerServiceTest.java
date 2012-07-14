package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.dao.ITravellerRepository;
import com.flurdy.grid.snaps.domain.HolidayGroup;
import com.flurdy.grid.snaps.domain.Traveller;
import com.flurdy.grid.snaps.exception.SnapTechnicalException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
public class TravellerServiceTest extends AbstractServiceTest {

	@Mock
	private ITravellerRepository travellerRepository;

	@InjectMocks
	private ITravellerService travellerService = new TravellerService();



	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
	}


	@Test
	public void testFindTraveller(){

//		assert defaultTravellerId > 0;
		Mockito.when(travellerRepository.findTraveller(new Long(1))).thenReturn(CURRENT_TRAVELLER);

		Traveller traveller = travellerService.findTraveller(1);

		Assert.assertNotNull( traveller );
		Assert.assertEquals( traveller.getFullname(), DEFAULT_FULLNAME );

		Mockito.verify(travellerRepository).findTraveller(new Long(1));
		Mockito.verifyNoMoreInteractions(travellerRepository);
	}


	@Test(expected = SnapTechnicalException.class)
	public void testFindInvalidTraveller(){

		Traveller traveller = travellerService.findTraveller(-1);
		Mockito.verifyZeroInteractions(travellerRepository);

	}




	@Test
	public void testFindNonExistentTraveller(){

		Mockito.when(travellerRepository.findTraveller(new Long(999))).thenReturn(null);

		Traveller traveller = travellerService.findTraveller(999);

		Assert.assertNull( traveller );
		Mockito.verify(travellerRepository).findTraveller(new Long(999));
		Mockito.verifyNoMoreInteractions(travellerRepository);

	}
	

	@Test
	public void testFindAllTravellers(){
		List<Traveller> allTravellers = new ArrayList<Traveller>(){{ add(CURRENT_TRAVELLER); }};
		Mockito.when(travellerRepository.findAllTravellers()).thenReturn(allTravellers);

		List<Traveller> travellers = travellerService.findTravellers();

		Assert.assertTrue( travellers.size() == 1 );
		Mockito.verify(travellerRepository).findAllTravellers();
		Mockito.verifyNoMoreInteractions(travellerRepository);
	}

}
