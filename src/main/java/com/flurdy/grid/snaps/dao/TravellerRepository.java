package com.flurdy.grid.snaps.dao;

import com.flurdy.grid.snaps.domain.Traveller;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class TravellerRepository extends AbstractRepository implements ITravellerRepository {

	@Override
	public void addTraveller(Traveller traveller) {

		assert traveller != null;

		entityManager.persist( traveller );

	}

}
