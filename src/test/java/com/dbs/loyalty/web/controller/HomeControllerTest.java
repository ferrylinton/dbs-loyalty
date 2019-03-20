package com.dbs.loyalty.web.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HomeControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;
	
	@Test
	public void testView() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
		ResponseEntity<String> entity = this.restTemplate.exchange(HomeController.HOME_URL, HttpMethod.GET, new HttpEntity<Void>(headers), String.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
}
