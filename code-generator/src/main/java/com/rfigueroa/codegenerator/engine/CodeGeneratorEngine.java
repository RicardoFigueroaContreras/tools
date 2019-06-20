package com.rfigueroa.codegenerator.engine;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rfigueroa.codegenerator.base.CustomVelocityEngine;
import com.rfigueroa.codegenerator.connection.DbTablesDescriberFactory;

@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
public final class CodeGeneratorEngine {

	private static String urlBase = "";
	private static LinkedHashMap dbConfig = null;
	private static List templates = null;
	private static String basePackage = null;
	private static String fileExtension = null;
	private static String outputFolder = null;
	private static String packageDelimiter = null;
	private final static String WinDelimiter = "/";
	private static Map<String, String> classNames = null;

	private CodeGeneratorEngine() {

	}

	public static CodeGeneratorEngine newInstance() {
		return new CodeGeneratorEngine();
	}

	private String getConfigurationString() throws IOException, URISyntaxException {
		URL path = getClass().getResource("EngineConfig.json");
		return new String(Files.readAllBytes(Paths.get(path.toURI())));
	}

	private HashMap<String, Object> convertJsonToMap(String json)
			throws JsonParseException, JsonMappingException, IOException {
		return new ObjectMapper().readValue(json, HashMap.class);
	}

	public CodeGeneratorEngine loadConfiguration(String language) {

		try {

			HashMap<String, Object> config = convertJsonToMap(getConfigurationString());
			LinkedHashMap languageConfig = (LinkedHashMap) config.get(language);

			urlBase = (String) config.get("BasePath");
			outputFolder = (String) config.get("OutputFolder");
			basePackage = (String) config.get("BasePackage");
			dbConfig = (LinkedHashMap) languageConfig.get("InputDB");
			templates = (List) languageConfig.get("Templates");
			packageDelimiter = (String) languageConfig.get("PackageDelimiter");
			fileExtension = (String) languageConfig.get("FileExtension");
			classNames = (LinkedHashMap) config.get("classNames");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	public void generateCode() {

		List<Map<String, Object>> dbObjects = DbTablesDescriberFactory
				.getDbTablesDescriber("GENERIC", dbConfig)
				.setMappingTableNames(classNames)
				.getDBObjects();

		dbObjects.forEach(obj -> {
			templates.forEach((config) -> {
				try {

					Map mapConfig = (Map) config;
					String url = urlBase + mapConfig.get("path");

					obj.put("BasePackage", basePackage);
					obj.putAll(mapConfig);

					String templateIn = getTemplate(url);
					String code = CustomVelocityEngine.velocityTransform(obj, templateIn);

					String fileNamePath = (String) obj.get("table");
					fileNamePath = getFileNamePath(obj);

					createCodeFile(code, fileNamePath);

				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		});

	}

	private String getFileNamePath(Map<String, Object> config) {

		String name = (String) config.get("table");
		String outputFileSufix = (String) config.get("outputFileSufix");
		String packageSufix = (String) config.get("packageSufix");
		StringBuffer fileNamePath = new StringBuffer();

		String finalPackageFolder = createPackageFolder(packageSufix);

		fileNamePath.append(finalPackageFolder);
		fileNamePath.append(name);
		fileNamePath.append(outputFileSufix);
		fileNamePath.append(fileExtension);

		return fileNamePath.toString();
	}

	private String createPackageFolder(String packageSufix) {
		String packageFolder = basePackage.replace(packageDelimiter, WinDelimiter) + packageSufix;
		String finalPackageFolder = outputFolder + packageFolder + WinDelimiter;
		File folders = new File(finalPackageFolder);

		if (!folders.exists()) {
			folders.mkdirs();
		}

		return finalPackageFolder;
	}

	private String getTemplate(String url) throws IOException {
		return FileUtils.readFileToString(new File(url));
	}

	private void createCodeFile(String code, String fileNamePath) throws IOException {
		FileUtils.writeByteArrayToFile(new File(fileNamePath), code.getBytes());
	}

	public static void main(String[] args) {
		CodeGeneratorEngine.newInstance().loadConfiguration("JAVA3").generateCode();
	}
}
