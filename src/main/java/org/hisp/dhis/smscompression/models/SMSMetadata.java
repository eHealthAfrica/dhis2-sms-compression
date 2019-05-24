package org.hisp.dhis.smscompression.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SMSMetadata {
	public static class ID {
		String id;
		public ID (String id) {
			this.id = id;
		}
	}
	
	public Date lastSyncDate;
	public List<ID> users;
	public List<ID> trackedEntityTypes;
	public List<ID> trackedEntityAttributes;
	public List<ID> programs;
	public List<ID> organisationUnits;
	public List<ID> dataElements;
	public List<ID> categoryOptionCombos;
	 
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
		
	private List<String> getIDs (List<ID> ids) {
		if (ids == null) return null;
		ArrayList<String> idList = new ArrayList<>();
		for (ID id : ids) {
			idList.add(id.id);
		}
		return idList;		
	}
}

