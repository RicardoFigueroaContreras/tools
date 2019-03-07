package com.rfigueroa.codegenerator.base;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.rfigueroa.codegenerator.commons.JavaTypesUtil;
import com.rfigueroa.codegenerator.commons.StringUtil;

public class CustomVelocityEngine {

	/**
	 * 
	 * @param inputDatamodel
	 * @param template
	 * @return
	 */
	public static String velocityTransform(Map<String, Object> dbObjects,
			String template) {

		VelocityEngine velocityEngine = newInstanceVelocityEngine();

		Map<String, Object> context = new HashMap<String, Object>();
		context.put("stringUtil", StringUtil.class);
		context.put("java", JavaTypesUtil.class);
		context.put("db", dbObjects);

		VelocityContext velocityContext = new VelocityContext(context);
		StringWriter writer = new StringWriter();

		velocityEngine.evaluate(velocityContext, writer, "", template);

		return writer.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	private static VelocityEngine newInstanceVelocityEngine() {
		return new VelocityEngine();
	}


	private CustomVelocityEngine() {

	}

}
