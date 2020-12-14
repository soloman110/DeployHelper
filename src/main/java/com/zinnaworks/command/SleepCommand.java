package com.zinnaworks.command;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class SleepCommand implements CustomizedCommand{
	
	final static String regex = "^\\s*sleep\\s+(\\d+)\\s*";
	
	@Override
	public String execute(String cmd) {
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(cmd.toLowerCase());
		matcher.find();
		String miliSeconds = StringUtils.trim(matcher.group(1));
		try {
			TimeUnit.MILLISECONDS.sleep(Integer.valueOf(miliSeconds));
		} catch (NumberFormatException | InterruptedException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	@Override
	public boolean supports(String cmd) {
		return Pattern.matches(regex, cmd.toLowerCase());
	}
}
