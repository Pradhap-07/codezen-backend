package com.codezen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import java.util.List;

@Configuration
public class CorsConfig {

	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true); // Allow credentials (only if needed)

		// 🔥 Allow requests from both localhost and Vercel
		corsConfiguration.setAllowedOrigins(List.of(
				"http://localhost:3000",
				"https://codezen-psi.vercel.app" // Replace with actual Vercel domain
		));

		corsConfiguration.setAllowedHeaders(List.of("*")); // Allow all headers
		corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);

		System.out.println("🔥 CORS Filter Loaded Successfully");
		return new CorsFilter(source);
	}
}
