package pl.com.psipoznan.visitorsregistry.visitorsregistry.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import pl.com.psipoznan.visitorsregistry.visitorsregistry.view.LoggingAccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
 
	@Autowired
    private LoggingAccessDeniedHandler accessDeniedHandler;
	
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
        // adresy dopuszczone bez logowania
        	.antMatchers("/error/**", "/accessDenied/**","/js/**","/css/**","/img/**","/webjars/**","/static/**", "/resources/**","/webapp/**","/h2_console/**").permitAll()
        	.antMatchers("/login*").anonymous()
        	//do widokow spod secured tylko rola admin
        	.antMatchers("/secured/**").hasRole("ADMIN")
        	.antMatchers("/**").permitAll()
        	.anyRequest().authenticated()
        .and()
        .formLogin()
        	//przysloneta domyslna strona logowania (bo wyglada paskudnie)
        	.loginPage("/login")
        	.permitAll()
        .and()
        .logout()
        	//obsluga wylogowania uzytkownika
        	.invalidateHttpSession(true)
        	.clearAuthentication(true)
        	.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        	// odwolanie do strony z logowaniem ale tym razem ze zmienna w adresie
        	.logoutSuccessUrl("/login?logout")
        	.permitAll()
        .and()
        // przesloniecie domyslnej strony access denied
        .exceptionHandling()
        	.accessDeniedHandler(accessDeniedHandler);
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
