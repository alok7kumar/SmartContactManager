package com.smart.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class MyConfig {

	@Bean
	public UserDetailsService getUserDetailsService() {
		return new UserDetailsServiceImp();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	//ChatGPT authorization
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasRole("USER")
                .requestMatchers("/css/**", "/js/**", "/image/**","/signup", "/do_register", "/", "/about").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")	//login par click karte he ye url aayga
                .loginProcessingUrl("/dologin")		//isko likhna hai login form ke action par
                .defaultSuccessUrl("/user/index", true)		//login credential sahi hone par is url par bhej dega
               // .failureUrl("/login_fail")  		// agar login me kuch issue aayi to error page ka url ye rahega
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")			//`?logout` is used to alert message with thymeleaf
                .permitAll()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            );

        return http.build();
    }
	
	// My authorization
	/*		@Bean
			public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception  {
				
				return security .csrf(csrf -> csrf.disable())  //csrf will disable the automatic generated login form from security
						.authorizeHttpRequests(auth->auth
						.requestMatchers("/css/**", "/js/**", "/image/**","/", "/signup").permitAll()		//permitAll() means it is accessible to all without login
						.requestMatchers("/admin/**").hasRole("ADMIN")	// role must be passed as it is written here in register form
						.requestMatchers("/user/**").hasRole("USER")	// role must be passed as it is written here in register form
						.anyRequest()
						.authenticated()
					)

						.formLogin( form->form.permitAll() )	//this is default security form
						.logout(logout -> logout
								.logoutUrl("/logout")
								.logoutSuccessUrl("/")
								.permitAll()
							)	//After logged in (both admin & user), type "logout" in url, you will be logged out and /home will be returned
						.build();	
			}*/

	// Authorization for AccesController class
	
	
	 @Bean
	 public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
		 DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		 authProvider.setUserDetailsService(userDetailsService);
		 authProvider.setPasswordEncoder(passwordEncoder()); 
		 return new ProviderManager(List.of(authProvider)); 
		 }
	 
	
	
	/*
	 * @Bean public AuthenticationProvider authenticationProvider() {
	 * DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	 * provider.setUserDetailsService(getUserDetailsService());
	 * provider.setPasswordEncoder(passwordEncoder()); return provider; }
	 */
	 
}
