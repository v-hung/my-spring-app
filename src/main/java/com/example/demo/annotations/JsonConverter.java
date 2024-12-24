package com.example.demo.annotations;

import java.util.Map;

import org.springframework.http.HttpStatus;

import com.example.demo.exception.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class JsonConverter implements AttributeConverter<Map<String, Object>, String> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(Map<String, Object> attribute) {

		try {

			return objectMapper.writeValueAsString(attribute);

		} catch (JsonProcessingException e) {

			throw new BusinessException(HttpStatus.BAD_REQUEST, "Error converting Map to JSON");

		}

	}

	@Override
	public Map<String, Object> convertToEntityAttribute(String dbData) {

		try {

			return objectMapper.readValue(dbData, new TypeReference<Map<String, Object>>() {
			});

		} catch (JsonProcessingException e) {

			throw new BusinessException(HttpStatus.BAD_REQUEST, "Error converting JSON to Map");

		}

	}
}
