package com.zinnaworks.deploy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zinnaworks.deploy.entity.Project;
import com.zinnaworks.deploy.entity.ProjectConfig;

@RequestMapping(value="/deploy")
@Controller
public class NavigatorController {

	@RequestMapping("/index")
	public String hello(Model model, @RequestParam(defaultValue = "fine") String name) {
		model.addAttribute("name", name);
		model.addAttribute("servers", ProjectConfig.getProject("smd"));
		return "tiles/thymeleaf/smd";
	}
	
	@RequestMapping("/smdif")
	public String ifcheck(Model model) {
		model.addAttribute("servers", ProjectConfig.getProject("smd"));
		return "tiles/thymeleaf/smdif";
	}
	
	@RequestMapping("/{projectName}")
	public String smd(@PathVariable String projectName, Model model) {
		model.addAttribute("servers", ProjectConfig.getProject(projectName));
		model.addAttribute("project", projectName);
		return "tiles/thymeleaf/deployTemplate";
	}
	
	@RequestMapping("/serverinfo")
	@ResponseBody
	public Project serverinfo(Model model, @RequestParam String projectName) {
		return ProjectConfig.getProject(projectName);
	}
}
