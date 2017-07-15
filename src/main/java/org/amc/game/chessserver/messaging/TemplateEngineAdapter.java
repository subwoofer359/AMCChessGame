package org.amc.game.chessserver.messaging;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

public class TemplateEngineAdapter {
	private TemplateEngine engine;
	
	public TemplateEngineAdapter(TemplateEngine engine) {
		this.engine = engine;
	}
	
	public String process(String templateName, Context ctx) {
		return engine.process(templateName, ctx);
	}
}
