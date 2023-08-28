package com.springboot.mapping;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
public class MappingApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void contextLoads() {
//		Assertions.assertNotNull(applicationContext);
//		Assertions.assertNotNull(applicationContext.getBean(MappingApplication.class));
//		Assertions.assertEquals(TimeZone.getTimeZone("UTC"), TimeZone.getDefault());
//		Assertions.assertEquals(Locale.US, Locale.getDefault());
	}
}