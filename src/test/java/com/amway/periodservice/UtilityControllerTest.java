package com.amway.periodservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UtilityControllerTest {

	@Autowired
	private UtilityController utilityController;

	@Test
	public void testContainerId() {
		Assert.notNull(utilityController.containerId());
	}
}
