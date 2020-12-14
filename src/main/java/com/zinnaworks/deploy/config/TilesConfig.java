package com.zinnaworks.deploy.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.tiles2.dialect.TilesDialect;
import org.thymeleaf.extras.tiles2.spring4.web.configurer.ThymeleafTilesConfigurer;
import org.thymeleaf.extras.tiles2.spring4.web.view.ThymeleafTilesView;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

@Configuration
public class TilesConfig {
	@Bean
	public ThymeleafTilesConfigurer tilesConfigurer() {
		ThymeleafTilesConfigurer ttc = new ThymeleafTilesConfigurer();
		ttc.setDefinitions("classpath:/webapp/tiles.xml");
		ttc.setCheckRefresh(true);
		return ttc;
	}
	@Bean
	public ViewResolver tilesViewResolver() {
		ThymeleafViewResolver vr = new ThymeleafViewResolver();
		vr.setTemplateEngine(templateEngine1());
		//vr.setViewNames(new String[] { "tiles/jsp/*" });
		vr.setViewClass(ThymeleafTilesView.class);
		vr.setCharacterEncoding("UTF-8");
		vr.setOrder(Ordered.LOWEST_PRECEDENCE);
		vr.setCache(false);
		return vr;
	}	

	@Bean
	public SpringTemplateEngine templateEngine() {
		final SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setTemplateResolver(templateResolver());
		engine.setAdditionalDialects(dialects());
		return engine;
	}
	
	@Bean
	public SpringTemplateEngine templateEngine1() {
		final SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setTemplateResolver(templateResolver2());
		engine.setAdditionalDialects(dialects());
		return engine;
	}

	@Bean
	public SpringResourceTemplateResolver templateResolver() {

		//ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setPrefix("classpath:webapp/templates/");
		templateResolver.setCacheable(false);
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("HTML5");
		templateResolver.setCharacterEncoding("UTF-8");
		return templateResolver;
	}
	
	@Bean
	public SpringResourceTemplateResolver templateResolver1() {

		//ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setPrefix("/WEB-INF/");
		templateResolver.setCacheable(false);
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("HTML5");
		templateResolver.setCharacterEncoding("UTF-8");
		return templateResolver;
	}
	
	@Bean
	public SpringResourceTemplateResolver templateResolver2() {

		//ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setPrefix("classpath:webapp/");
		templateResolver.setCacheable(false);
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("HTML5");
		templateResolver.setCharacterEncoding("UTF-8");
		return templateResolver;
	}

	@Bean
	public ViewResolver thymeleafViewResolver() {
		ThymeleafViewResolver vr = new ThymeleafViewResolver();
		vr.setTemplateEngine(templateEngine());
		vr.setCharacterEncoding("UTF-8");
		vr.setOrder(Ordered.HIGHEST_PRECEDENCE);
		vr.setExcludedViewNames(new String[] { "tiles/jsp/*", "tiles/thymeleaf/*" });
		return vr;
	}

	private Set<IDialect> dialects() {
		final Set<IDialect> set = new HashSet<IDialect>();
		set.add(new TilesDialect());
		return set;
	}
}
