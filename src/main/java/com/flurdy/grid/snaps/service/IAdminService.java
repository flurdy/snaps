package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.domain.HolidayGroup;
import com.flurdy.grid.snaps.domain.Traveller;
import org.springframework.security.access.prepost.PreAuthorize;

public interface IAdminService {

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER')")
	public void updateTraveller(Traveller traveller);

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER')")
	public void deleteTraveller(long travellerId);

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER')")
	public void updateHolidayGroup(HolidayGroup holidayGroup);

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER')")
	public void deleteHolidayGroup(long holidayId);
}
