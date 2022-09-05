package ru.itsinfo.fetchapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itsinfo.fetchapi.service.AppService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // сервис, с помощью которого тащим пользователя
    private final AppService appService;

    private final PasswordEncoder passwordEncoder;

    private final SuccessUserHandler successUserHandler;


    @Autowired
    public SecurityConfig(AppService appService,
                          PasswordEncoder passwordEncoder,
                          SuccessUserHandler successUserHandler) {
        this.appService = appService;
        this.passwordEncoder = passwordEncoder;
        this.successUserHandler = successUserHandler;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(appService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                // Декларирует, что все запросы к любой конечной точке должны быть авторизованы, иначе они должны быть отклонены
                .authorizeRequests()
                .antMatchers("/", "/img/**", "/css/**", "/js/**", "/webjars/**").permitAll()
//                .antMatchers(HttpMethod.GET, "/api/users/*").hasRole("USER")
                .antMatchers("/api/users/*", "/api/roles").hasRole("ADMIN")
                .anyRequest().authenticated();
//                .and().sessionManagement().disable(); // сообщает Spring, что не следует хранить информацию о сеансе для пользователей, поскольку это не нужно для API
//                .and().httpBasic(); // сообщает Spring, чтобы он ожидал базовую HTTP аутентификацию

        http.formLogin()
                .successHandler(successUserHandler)
                .loginPage("/") // указываем страницу с формой логина
                .permitAll() // даем доступ к форме логина всем
                .loginProcessingUrl("/login");
        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll();
    }
}