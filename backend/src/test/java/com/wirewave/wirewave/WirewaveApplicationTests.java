package com.wirewave.wirewave;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = WirewaveApplication.class, properties = "spring.profiles.active=test")
public class WirewaveApplicationTests {

	@Test
	void contextLoads() {
		// Проверка, что контекст загружается
	}
}
