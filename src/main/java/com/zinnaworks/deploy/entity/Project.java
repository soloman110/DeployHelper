package com.zinnaworks.deploy.entity;

import java.util.List;
import lombok.Data;

@Data
public class Project {
	private String name;
	public String user;
	private String password;
	private String warpath;
	private String warbackuppath;
	private String commands;
	
	public List<DestHost> serverList;
	@Data
	public static class DestHost{
		private String ip;
		private String name;
		private String group;
	}
}
