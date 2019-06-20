package com.rfigueroa.codegenerator.commons;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class JdbcUtil {

	private JdbcUtil() {

	}

	public static List<String> getTableNames(ResultSet rs) {

		List<String> tables = new ArrayList<>();

		try {
			while (rs.next()) {
				tables.add(StringUtil.correctName(rs.getString(1)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return tables;
	}

	public static List<Map<String, String>> getCollNames(ResultSet rs) {

		List<Map<String, String>> colls = new ArrayList<>();
		Map<String, String> coll = null;

		try {
			while (rs.next()) {
				coll = new HashMap<String, String>();
				coll.put("name", rs.getString(1));
				coll.put("type", rs.getString(2));
				colls.add(coll);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return colls;
	}
	
	

}
