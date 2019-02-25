package org.hisp.dhis.smscompression.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Metadata {
	public static class ID {
		String id;
		public ID (String id) {
			this.id = id;
		}
	}
	
	public Date lastSyncDate;
	public ID[] users;
	public ID[] trackedEntityTypes;
	public ID[] trackedEntityAttributes;
	public ID[] programs;
	public ID[] organisationUnits;
	public ID[] dataElements;
	public ID[] categoryOptionCombos;
	 
	public List<String> getUsers() {
		return getIDs(users);
	}

	public List<String> getTrackedEntityTypes() {
		return getIDs(trackedEntityTypes);
	}

	public List<String> getTrackedEntityAttributes() {
		return getIDs(trackedEntityAttributes);
	}

	public List<String> getPrograms() {
		return getIDs(programs);
	}

	public List<String> getOrganisationUnits() {
		return getIDs(organisationUnits);
	}

	public List<String> getDataElements() {
		return getIDs(dataElements);
	}
	
	public List<String> getCategoryOptionCombos() {
		return getIDs(categoryOptionCombos);
	}	
		
	private List<String> getIDs (ID[] ids) {
		if (ids == null) return null;
		ArrayList<String> idList = new ArrayList<>();
		for (ID id : ids) {
			idList.add(id.id);
		}
		return idList;		
	}
}

