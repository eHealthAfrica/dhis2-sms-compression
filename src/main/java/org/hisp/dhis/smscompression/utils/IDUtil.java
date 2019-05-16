package org.hisp.dhis.smscompression.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class IDUtil {

	public static final int ID_LEN = 11;
	
	public static int getBitLengthForList(List<String> ids) {
		if (!checkIDList(ids)) return -1;
				
		int len = BinaryUtils.log2(ids.size());
		
		boolean coll = false;
		do {
			coll = false;
			ArrayList<Integer> idList = new ArrayList<Integer>();
			for (String id : ids) {
				int newHash = BinaryUtils.hash(id, len);
				if (idList.contains(newHash)) {
					len++;
					coll = true;
					break;					
				} else {
					idList.add(newHash);
				}
			}
			// Prevent infinite loop if something goes wrong
			if (len > 32) return -1;
		} while(coll);
		return len;
	}
	
	public static boolean checkIDList(List<String> ids) {
		HashSet<String> set = new HashSet<String>(ids);
		if (set.size() != ids.size()) return false;
		for (String id : ids) {
			if (!validID(id)) return false;
		}
		return true;
	}
	
	public static boolean validID(String id) {
		return id.matches("^[A-z0-9]{"+ID_LEN+"}$");
	}
	
	public static int convertIDCharToInt(char c) {
		int i = (int) c;		
		
		if (c >= '0' && c <= '9') {
			i -= '0';
		} else if (c >= 'A' && c <= 'Z') {
			i -= '0' + ('A' - '9' - 1);
		} else if (c >= 'a' && c <= 'z') {
			i -= '0' + ('A' - '9' - 1) + ('a' - 'Z' - 1);
		} else {
			return -1;
		}		

		return i;
	}

	public static char convertIDIntToChar(int i) {
		char c = (char) i;
		
		c += '0';
		if (c >= '0' && c <= '9') return c;
		
		c += ('A' - '9' - 1);
		if (c >= 'A' && c <= 'Z') return c;
		
		c += ('a' - 'Z' - 1);
		if (c >= 'a' && c <= 'z') return c;
				
		return 0;
	}
	
	// Used to write new IDs where we have to express the full ID.
	// Must only include chars in the range a-Z0-9, as we encode each 
	// char to 6 bits (64 vals)
	public static void writeNewID(String id, BitOutputStream outStream) throws Exception {
		if (!validID(id)) throw new Exception("Invalid ID");
		for (char c : id.toCharArray()) {
			outStream.write(convertIDCharToInt(c), 6);
		}
	}
	
	// Used to read new IDs where we have to express the full ID.
	// Must only include chars in the range a-Z0-9, as we encode each 
	// char to 6 bits (64 vals)
	public static String readNewID(BitInputStream inStream) throws Exception {
		String id = "";
		while (id.length() < ID_LEN) {
			int i = inStream.read(6);
			id += convertIDIntToChar(i);
		}
		return id;
	}
	
	public static Map<Integer, String> getIDLookup(List<String> idList, int hashLen) {
		HashMap<Integer, String> idLookup = new HashMap<>();
		for (String id : idList) {
			int hash = BinaryUtils.hash(id, hashLen);
			idLookup.put(hash, id);
		}
		return idLookup;
	}
}
