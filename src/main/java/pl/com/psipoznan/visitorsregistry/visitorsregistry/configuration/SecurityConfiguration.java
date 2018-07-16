package pl.com.psipoznan.visitorsregistry.visitorsregistry.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
 
    /**
     * Zalozenie domyslnych uzytkownikow kazdorazowo przy starcie aplikacji
     * podczas inicjacji kontekstu SpringSecurity 
     * 
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) 
      throws Exception {
        auth
          .inMemoryAuthentication()
          .withUser("user").password(passwordEncoder().encode("1234")).roles("USER")
          .and()
          .withUser("admin").password(passwordEncoder().encode("1234")).roles("ADMIN");
    }
    
    @Override
	protected void configure(HttpSecurity http) throws Exception {
		// nalezy wylaczyc csrf aby moc korzystac z naszych formatek html
		http.csrf().disable();
		// wylaczamy ramki poniewaz konsola admina bazy h2 przez to by sie niezainicjowala
		http.headers().frameOptions().disable();

		/**
		 * cala mantra dostepow dla poszczegolnych URL
		 */
		http
        .authorizeRequests()
        .antMatchers("/VAADIN/**","/console/*","/ws/**", "/h2_console/**", "/PUSH/**", "/UIDL/**", "/error/**", "/accessDenied/**", "/vaadinServlet/**").permitAll()
        .antMatchers("/login*").anonymous()
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .loginPage("/login.html")
        .loginProcessingUrl("/perform_login")
        .defaultSuccessUrl("/index.html")
        .failureUrl("/login.html")
        .and()
        .logout().logoutSuccessUrl("/login.html");
	}
    
    /**
     * Encoder do hashowania hasel w aplikacji
     * od SpringSecurity 5 musi byc zdefiniowany jakikolwiek
     * @return
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
