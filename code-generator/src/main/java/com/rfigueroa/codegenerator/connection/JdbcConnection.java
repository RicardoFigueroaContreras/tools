package com.rfigueroa.codegenerator.connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.util.ObjectUtils;

public final class JdbcConnection {

	private SettingMapper settings = null;
	private Connection connection = null;
	
	private JdbcConnection(Map<String, String> dbSettings) {
		settings = new SettingMapper(dbSettings);
	}

	
	private DataSource getDataSource() {
        return DataSourceBuilder.create()
                .username(settings.getUsername())
                .password(settings.getPwd())
                .url(settings.getUrl())
                .driverClassName(settings.getDriver())
                .build();
	}
	
	public Connection getConnection() {
		try {
			if (!ObjectUtils.isEmpty(connection)) {
				return connection;
			}
			return getDataSource().getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	} 
	
	public Statement createStatement() {
		try {
			return getConnection().createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ResultSet getResultSet(String sql) {
		try {
			return createStatement().executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;	
	}
	
	public static class SettingMapper {

		Map<String, String> dbSettings = new HashMap<String, String>();
		
		public SettingMapper(Map<String, String> dbSettings) {
			this.dbSettings = dbSettings;
		}
		
		String getUsername() {
			return dbSettings.get("username");
		}
		
		String getPwd() {
			return dbSettings.get("password");
		}
		
		String getUrl() {
			return dbSettings.get("url");
		}
		
		String getDriver() {
			return dbSettings.get("driver");
		}
		
		String getSqlQuery() {
			return dbSettings.get("sql");
		}
	}
	
	public static JdbcConnection newInstance(Map<String, String> dbSettings) {
		return new JdbcConnection(dbSettings);
	}
}
