package com.rfigueroa.codegenerator.connection;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rfigueroa.codegenerator.commons.JdbcUtil;

public final class JdbcGenericDescriber implements Describer<JdbcConnection> {

	private JdbcConnection cnx = null;
	private Map<String, String> settings = null;

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
			tableMap.put("table", table);
			tableMap.put("colls", colls);
			objs.add(tableMap);
		});

		return objs;
	}

	public static Describer newInstance() {
		return new JdbcGenericDescriber();
	}

}
