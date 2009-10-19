/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.flurdy.grid.fotogator.service;

import com.flurdy.grid.fotogator.domain.HolidayGroup;
import java.util.Set;

public interface IHolidayGroupService {

	public Set<HolidayGroup> searchForHolidayGroups(String groupName);

	public void addHolidayGroup(HolidayGroup holidayGroup);

	public HolidayGroup findHolidayGroup(Long groupId);

}
