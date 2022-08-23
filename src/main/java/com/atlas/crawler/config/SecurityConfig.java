/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.config;

import com.atlas.crawler.captcha.CaptchaAuthenticationProvider;
import com.atlas.crawler.captcha.CaptchaDetailsSource;
import com.atlas.crawler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userService;

	@Autowired
	private CaptchaAuthenticationProvider authenticationProvider;

	@Autowired
	private CaptchaDetailsSource detailsSource;

	@Autowired
	private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

	@Autowired
	private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

	@Autowired
	private CustomLogoutSuccessHandler customLogoutSuccessHandler;


	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/").hasAnyRole("ORDINARY","SUPERVISOR","EXPERT","MANAGER")
			.antMatchers("/ordinary/**").hasAnyRole("ORDINARY","SUPERVISOR","EXPERT","MANAGER")
			.antMatchers("/supervisor/**").hasAnyRole("SUPERVISOR","MANAGER")
			.antMatchers("/expert/**").hasAnyRole("EXPERT","MANAGER")
			.antMatchers("/manager/**").hasRole("MANAGER")
			.and()
			.formLogin()
				.loginPage("/login")
				.loginProcessingUrl("/authenticateTheUser")
				.successHandler(customAuthenticationSuccessHandler)
				.failureHandler(customAuthenticationFailureHandler)
				.authenticationDetailsSource(detailsSource)
				.permitAll()
			.and()
			.logout()
				.logoutSuccessHandler(customLogoutSuccessHandler)
				.permitAll();
//			.and()
//				.exceptionHandling()
//				.accessDeniedPage("/error");
				
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		
		auth.setUserDetailsService(userService);
		auth.setPasswordEncoder(passwordEncoder());
		
		return auth;
	}
	


}
