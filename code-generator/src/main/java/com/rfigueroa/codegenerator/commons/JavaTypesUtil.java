package com.rfigueroa.codegenerator.commons;

import java.util.HashMap;
import java.util.Map;


public class JavaTypesUtil {

	public static Map<String, String> types = new HashMap<>();
	private static final String C = "(";
	private static final Integer ZERO = 0;
	private static final String ID = "ID";

	static {
		types.put("VARCHAR", "String");
		types.put("NUMERIC", "Double");
		types.put("DECIMAL", "Double");
		types.put("INT", "Integer");
		types.put("DATETIME", "Date");
		types.put("ID", "Long");
	}

	public JavaTypesUtil() {

	}

	public static String getType(String name, String dbType) {
		
		dbType = dbType.toUpperCase();
		name = name.toUpperCase();
		
		if (types.containsKey(dbType)) {
			return types.get(dbType);
		} else if (name.contains(ID)) {
			return types.get(ID);
		}

		return types.get(getTypeName(dbType));
	}
	
	public static String getMethodName(String name) {
		return StringUtil.getMethodName(name);
	}

	private static String getTypeName(String dbType) {

		if (dbType.contains(C)) {
			dbType = dbType.substring(ZERO, dbType.indexOf(C)).trim();
			System.out.println(dbType);
		}

		return dbType;
	}
	
	public static String getIdProperties(String name) {
		if(name.equalsIgnoreCase(ID)) {
		return "@Id\r\n" + 
			   "	@GeneratedValue(strategy = GenerationType.AUTO)";
		}
		return "";
	}
}
