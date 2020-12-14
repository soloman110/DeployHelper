package com.zinnaworks.command;

public interface CustomizedCommand {
	public String execute(String cmd);
	public boolean supports(String cmd);
}
