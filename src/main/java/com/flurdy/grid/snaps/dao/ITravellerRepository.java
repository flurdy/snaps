package com.flurdy.grid.snaps.dao;

import com.flurdy.grid.snaps.domain.Traveller;
import java.util.List;

public interface ITravellerRepository {

	public void addTraveller(Traveller traveller);

	public List<Traveller> findAllTravellers();

	public Traveller findTraveller(long travellerId);

	public void updateTraveller(Traveller traveller);

	public void deleteTraveller(Traveller traveller);

}
