package com.zinnaworks.deploy.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;

@Data
@ConfigurationProperties
public class ProjectConfig {
	private List<Project> projects;
	
	private static Map<String, Project> projectMapping = new HashMap<>();
	
	public static Project getProject(String projectName) {
		return projectMapping.get(projectName);
	}
	
	@PostConstruct
	public void mapping() {
		for (Project project : projects) {
			projectMapping.put(project.getName(), project);
		}
	}
}
