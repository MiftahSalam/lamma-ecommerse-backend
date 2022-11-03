package com.miftah.lamaecommerse.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miftah.lamaecommerse.dtos.BaseResponse;

@SpringBootTest
@WebAppConfiguration
public abstract class AbstractControllerTest {
	protected MockMvc mvc;

	@Autowired
	WebApplicationContext webApplicationContext;

	@Autowired
	FilterChainProxy filterChainProxy;

	protected void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilter(filterChainProxy).build();
	}

	protected String mapToJson(Object object) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(object);
	}

	protected <T> BaseResponse<T> mapFromJson(String json, TypeToken<T> typeToken)
			throws JsonParseException, JsonMappingException, IOException {
		return new Gson().fromJson(json, typeToken.getType());
	}
}
