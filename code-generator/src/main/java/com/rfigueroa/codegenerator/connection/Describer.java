package com.rfigueroa.codegenerator.connection;

import java.util.List;
import java.util.Map;

public interface Describer<T> {

	List<Map<String, Object>> getDBObjects();
	
	Describer<T> setSettings(Map<String, String> settings);
	
	Describer<T> setConnection(T connection);
}
