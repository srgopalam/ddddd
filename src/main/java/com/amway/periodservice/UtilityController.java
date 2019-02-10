package com.amway.periodservice;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/utility/")
public class UtilityController {

	public static final Logger logger = LoggerFactory.getLogger(UtilityController.class);

	// -----------------------Container Id -------------------------------------
	@RequestMapping(value = "/containerid/", method = RequestMethod.GET)
	public ResponseEntity<Object> containerId() {
		final InetAddress localHost;
		HashMap<String, String> map = new HashMap<>();
		JSONObject containerId;
		try {
			localHost = InetAddress.getLocalHost();
			map.put("host", localHost.getHostName());
			map.put("ip", localHost.getHostAddress());
			containerId = new JSONObject(map);
		} catch (UnknownHostException e) {
			logger.warn("An exception occurred while trying to determine the host and IP address: {}", e);
			return new ResponseEntity<>(new CustomErrorType("Container Id not found."), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(containerId, HttpStatus.OK);

	}

}
