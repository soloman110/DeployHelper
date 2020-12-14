package com.zinnaworks.command;

import java.util.ArrayList;
import java.util.List;

public class CustomizedCommandManager {
	private static List<CustomizedCommand> chain = new ArrayList<>();
	public void addCommand(CustomizedCommand  cmd) {
		chain.add(cmd);
	}
	public static String execute(String cmd)  {
		for (CustomizedCommand customizedCommand : chain) {
			if(customizedCommand.supports(cmd))
				return customizedCommand.execute(cmd);
		}
		throw new UnsupportedOperationException();
	}
	public static boolean isCustomizedCommand(String cmd) {
		for (CustomizedCommand customizedCommand : chain) {
			if(customizedCommand.supports(cmd)) return true;
		}
		return false;
	}
	static {
		chain.add(new SleepCommand());
	}
}