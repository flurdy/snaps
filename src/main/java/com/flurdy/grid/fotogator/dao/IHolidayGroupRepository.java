/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.flurdy.grid.fotogator.dao;

import com.flurdy.grid.fotogator.domain.HolidayGroup;
import java.util.Collection;

public interface IHolidayGroupRepository {

	public void addHolidayGroup(HolidayGroup holidayGroup);

	public HolidayGroup findHolidayGroup(Long groupId);
	public Collection<HolidayGroup> findHolidayGroups(String groupName);

}
