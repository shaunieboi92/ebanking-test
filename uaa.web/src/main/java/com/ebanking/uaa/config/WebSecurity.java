package com.ebanking.uaa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ebanking.uaa.filter.AuthTokenFilter;
import com.ebanking.uaa.service.impl.UserDetailsServiceImpl;
import com.ebanking.uaa.utility.JwtAuthenticationEntryPoint;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {

	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	JwtAuthenticationEntryPoint authenticationEntryPoint;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint).and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests().antMatchers("/auth/**").permitAll()
				.anyRequest().authenticated();

		http.addFilterBefore(authenticationJwtTokenFilter(),
				UsernamePasswordAuthenticationFilter.class);
	}

	public WebSecurity(UserDetailsServiceImpl userDetailsService) {
		this.userDetailsService = userDetailsService;
		// this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	// @Override
	// protected void configure(HttpSecurity http) throws Exception {
	// http.cors().and().csrf().disable().authorizeRequests()
	// .antMatchers("/auth/**").permitAll().anyRequest()
	// .authenticated().and().sessionManagement()
	// .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	//
	// http.addFilterBefore(authenticationJwtTokenFilter(),
	// UsernamePasswordAuthenticationFilter.class);
	// }

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
				// .passwordEncoder(bCryptPasswordEncoder);
				.passwordEncoder(passwordEncoder());
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**",
				new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}

}
