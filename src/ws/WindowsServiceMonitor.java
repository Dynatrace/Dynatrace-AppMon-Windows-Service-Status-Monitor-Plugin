package ws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.dynatrace.diagnostics.pdk.PluginEnvironment;
import com.dynatrace.diagnostics.pdk.Status;

public class WindowsServiceMonitor {

	private static final String CONFIG_SERVICE = "service";
	
	private boolean matchRuleSuccess;
	private String CMD = "SC";
	private String server = null;
	private String param = "QUERY";
	private String serviceName = null; 
	
	private static final Logger log = Logger.getLogger(WindowsServiceMonitor.class.getName());

	protected Status setup(PluginEnvironment env) throws Exception {
		Status result = new Status(Status.StatusCode.Success);
        String host = env.getHost().getAddress();
		String service = env.getConfigString(CONFIG_SERVICE);
		
			if (service != null && host != null) {
				server = host;
//				System.out.println(server);
				serviceName = service;
			}
			
		return result;
	}

	protected Status execute(PluginEnvironment env) throws Exception {
		Status result = new Status(Status.StatusCode.Success);

		String output = executeCommand(CMD, server, param, serviceName, result);

		if (log.isLoggable(Level.FINE)) log.fine("Command output was: " + output);
//        System.out.println(output);
		
		matchRuleSuccess = false;
		if (output != null) {
			if(output.contains("RUNNING")) {
				result.setMessage("Service Running");
				matchRuleSuccess = true;
			}
			
			else if(output.contains("STOPPED")) {
				result = new Status(Status.StatusCode.PartialSuccess);
				result.setMessage("Service Stopped");
				matchRuleSuccess = false;
			}
			else {
			    result = new Status(Status.StatusCode.ErrorInternalUnauthorized);
				result.setMessage("Error occurred");
			}
		}

		return result;
	}
	
	protected boolean isMatchRuleSuccess() {
		return matchRuleSuccess;
	}

	protected void teardown(PluginEnvironment env) throws Exception {
	}

	private String executeCommand(String CMD, String server, String param, String service, Status status) {
		String[] command = {CMD, "\\\\" + server, param, service};
		String inputstream = "";
		String line = "";
		
		try {
		Runtime rt = Runtime.getRuntime();
		Process execute = rt.exec(command);
		BufferedReader BR = new BufferedReader(new InputStreamReader(execute.getInputStream()));
		execute.waitFor();
		while ((line = BR.readLine()) != null){
			inputstream += line;
			}
		execute.destroy();
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		return inputstream;
	}
	
}
