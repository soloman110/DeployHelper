package com.zinnaworks.deploy.controller;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jcraft.jsch.JSchException;
import com.zinnaworks.command.CustomizedCommandManager;
import com.zinnaworks.deploy.entity.Project;
import com.zinnaworks.deploy.entity.ProjectConfig;
import com.zinnaworks.deploy.service.FileStorageService;
import com.zinnaworks.deploy.util.ExecuteShellUtil;
import com.zinnaworks.deploy.util.JsonUtil;

@RequestMapping("/deploy")
@Controller
public class RemoteExecuteController {
	private static final Logger logger = LoggerFactory.getLogger(RemoteExecuteController.class);
	
	private static final int READ_TIMEOUT = 150; //초 execute CONNECT_TIMEOUT는 60초 로 되어있
	
	@Autowired
	private FileStorageService fileStorageServiceImpl;

	@Autowired
	ProjectConfig smdservers;

	@PostMapping("/sftp")
	@ResponseBody
	public String uploadFile(@RequestParam("file") MultipartFile file, String remoteBaseDir, String servers,
			String project) throws JSchException {
		List<Map<String, String>> list = JsonUtil.jsonToObjectList(servers, List.class, Map.class);
		
		Project projectConfig = ProjectConfig.getProject(project);
		String user = projectConfig.getUser();
		String ps = projectConfig.getPassword();

		String fileName = fileStorageServiceImpl.storeFile(file);
		String localFilePath = fileStorageServiceImpl.getUploadDir();
		
		List<CompletableFuture<Boolean>> todoList = new ArrayList<>();
		for (Map<String, String> map : list) {
			String ip =  map.get("ip");
			CompletableFuture<Boolean> f = CompletableFuture.supplyAsync(() -> {
				try {
					fileStorageServiceImpl.sftp(ip, user, ps, remoteBaseDir, fileName, localFilePath);
				} catch (JSchException e) {
					logger.error( "[uploadFile error] "+ e.getMessage(), e );
					return false;
				}
				return true;
			});
			todoList.add(f);
		}
		for (CompletableFuture<Boolean> completableFuture : todoList) {
			try {
				Boolean result = completableFuture.get(READ_TIMEOUT, TimeUnit.SECONDS);
				if(!result) {
					return "fail";
				}
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				e.printStackTrace();
				return "fail";
			}
		}
		return "ok";
	}

	@ResponseBody
	@PostMapping("/command")
	public String remoteExecute(@RequestBody Map<String, Object> paramBodyMap) throws Exception {
		String commands = (String) paramBodyMap.get("commands");
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> hosts = (List<Map<String, Object>>) paramBodyMap.get("servers");
		Project project = ProjectConfig.getProject((String)paramBodyMap.get("project"));
		String user = project.getUser();
		String ps = project.getPassword();
		
		if (StringUtils.isBlank(commands)) {
			throw new RuntimeException("commands is Empty...");
		}
		List<CompletableFuture<String>> tasklist = new ArrayList<>();
		for (Map<String, Object> host : hosts) {
			String[] cmds = commands.split(System.lineSeparator());
			CompletableFuture<String> f = CompletableFuture.supplyAsync(() -> {
				ExecuteShellUtil exe = ExecuteShellUtil.getInstance();
				LocalTime start = LocalTime.now();
				logger.info(String.format("[Session Start] ====================== IP: %s Time: %s", (String) host.get("ip"), start.toString()));
				try {
					exe.init((String) host.get("ip"), 22, user, ps);
					for (String cmd : cmds) {
						logger.info("[CMD Strart]: " + cmd + " IP: " + (String) host.get("ip"));
						if(CustomizedCommandManager.isCustomizedCommand(cmd))  {
							CustomizedCommandManager.execute(cmd);
							continue;
						}
						List<String> result = exe.remoteExecute(cmd);
						logger.info(String.format("[RESULT][IP] %s =======>\n%s", (String) host.get("ip"), String.join(System.getProperty("line.separator"), result)));
						logger.info("[CMD END]: " + cmd + " IP: " + (String) host.get("ip"));
					}
				} catch (JSchException e) {
					return "HOST: " + host + "Error" + e.toString();
				} finally {
					exe.close();
				}
				LocalTime end = LocalTime.now();
				logger.info(String.format("[Session END] ========================= IP: %s Time: %s", (String) host.get("ip"), end.toString()));
				return "ok";
			});
			tasklist.add(f);
		}
		for (CompletableFuture<String> completableFuture : tasklist) {
			String result = completableFuture.get(READ_TIMEOUT, TimeUnit.SECONDS);
			if (!"ok".equals(result)) {
				return result;
			} else {
				logger.info("Result: " + result);
			}
		}
		return "ok";
	}
	
}