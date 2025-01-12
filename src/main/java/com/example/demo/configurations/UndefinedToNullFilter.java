package com.example.demo.configurations;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

@Component
public class UndefinedToNullFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest)request;
		Map<String, String[]> parameterMap = httpRequest.getParameterMap();

		Map<String, String[]> modifiedParameters = new HashMap<>();

		// Replace "undefined" with null for all parameters
		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {

			String[] values = entry.getValue();

			if (values != null) {

				for (int i = 0; i < values.length; i++) {

					if ("undefined".equals(values[i])) {

						values[i] = null;

					}

				}

			}

			modifiedParameters.put(entry.getKey(), values);

		}

		// Create a wrapped request to override parameters
		HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(httpRequest) {
			@Override
			public Map<String, String[]> getParameterMap() {

				return modifiedParameters;

			}

			@Override
			public String getParameter(String name) {

				String[] params = modifiedParameters.get(name);
				return params != null && params.length > 0 ? params[0] : null;

			}

			@Override
			public String[] getParameterValues(String name) {

				return modifiedParameters.get(name);

			}
		};

		chain.doFilter(wrappedRequest, response);

	}
}
