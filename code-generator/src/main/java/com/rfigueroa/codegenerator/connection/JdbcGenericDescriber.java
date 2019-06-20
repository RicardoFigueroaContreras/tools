package com.rfigueroa.codegenerator.connection;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.ObjectUtils;

import com.rfigueroa.codegenerator.commons.JdbcUtil;

public final class JdbcGenericDescriber implements Describer<JdbcConnection> {

	private JdbcConnection cnx = null;
	private Map<String, String> settings = null;
	private Map<String, String> classNames = null;

	private JdbcGenericDescriber() {

	}

	@Override
	public Describer<JdbcConnection> setSettings(Map<String, String> settings) {
		this.settings = settings;
		return this;
	}

	@Override
	public Describer<JdbcConnection> setConnection(JdbcConnection connection) {
		this.cnx = connection;
		return this;
	}

	@Override
	public List<Map<String, Object>> getDBObjects() {

		List<Map<String, Object>> objs = new ArrayList();
		String describeTableQuery = settings.get("tablesQuery");
		String describeCollsQuery = settings.get("collsQuery");

		ResultSet rs = cnx.getResultSet(describeTableQuery);
		List<String> tables = JdbcUtil.getTableNames(rs);

		tables.forEach(table -> {
			Map<String, Object> tableMap = new HashMap<String, Object>();
			ResultSet rsCl = cnx.getResultSet(String.format(describeCollsQuery, table));
			List<Map<String, String>> colls = JdbcUtil.getCollNames(rsCl);
			String className = getClassName(table);
			tableMap.put("table", className);
			tableMap.put("colls", colls);
			objs.add(tableMap);
		});

		return objs;
	}

	private String getClassName(String tableName) {
		if(!ObjectUtils.isEmpty(classNames) && 
		   !ObjectUtils.isEmpty(tableName) &&
		   classNames.containsKey(tableName.toLowerCase())) {
			return classNames.get(tableName.toLowerCase());
		}
		return tableName;
	}

	public static Describer newInstance() {
		return new JdbcGenericDescriber();
	}

	@Override
	public Describer<JdbcConnection> setMappingTableNames(Map<String, String> classNames) {
		this.classNames  = classNames;
		return this;
	}

}
