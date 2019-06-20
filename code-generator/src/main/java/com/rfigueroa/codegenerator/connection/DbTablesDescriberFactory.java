package com.rfigueroa.codegenerator.connection;

import java.util.Map;

public final class DbTablesDescriberFactory {

	private DbTablesDescriberFactory() {
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Describer getDbTablesDescriber(String provider, Map<String, String> settings) {

		switch (provider) {
		case "GENERIC":
			return JdbcGenericDescriber.newInstance()
						.setSettings(settings)
						.setConnection(JdbcConnection.newInstance(settings));
		default:
			return null;
		}
	}
}
