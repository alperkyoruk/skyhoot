package skylab.skyhoot.core.security;

import org.springframework.http.HttpMethod;
import skylab.skyhoot.Business.abstracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserService userService, PasswordEncoder passwordEncoder) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(x ->
                        x
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()


                             .requestMatchers("/api/auth/**").permitAll()

                                .requestMatchers("/api/users/addUser").permitAll()
                                .requestMatchers("/api/users/deleteUser").hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers("/api/users/updateUser").hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers("/api/users/getUserById").hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers("/api/users/getUserByUsername").hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers("/api/users/getUsers").hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers("/api/users/addModerator").hasRole("ADMIN")
                                .requestMatchers("/api/users/removeModerator").hasRole("ADMIN")
                                .requestMatchers("/api/users/addVip").hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers("/api/users/removeVip").hasAnyRole("ADMIN", "MODERATOR")

                                .requestMatchers("/api/games/addGame").hasAnyRole("ADMIN", "MODERATOR", "VIP")
                                .requestMatchers("/api/games/connectPlayer").permitAll()
                                .requestMatchers("/api/games/deleteGame").hasAnyRole("ADMIN", "MODERATOR", "VIP")
                                .requestMatchers("/api/games/endGame").hasAnyRole("ADMIN", "MODERATOR", "VIP")
                                .requestMatchers("/api/games/getGameByGameCode").permitAll()

                                .requestMatchers("/api/games/getGameEntityByGameCode").hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers("/api/games/getGameId").permitAll()
                                .requestMatchers("/api/games/getGameEntityByGameId").hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers("/api/games/getGamesByHostId").hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers("api/games/getGameStarted").permitAll()
                                .requestMatchers("/api/games/getLeaderboard").permitAll()
                                //   .requestMatchers("/api/games/getNextQuestion").hasAnyRole("ADMIN", "MODERATOR", "VIP")
                                .requestMatchers("/api/games/startGame").hasAnyRole("ADMIN", "MODERATOR", "VIP")
                                .requestMatchers("/api/games/updateGame").hasAnyRole("ADMIN", "MODERATOR", "VIP")

                                .requestMatchers("/api/questions/addQuestion").hasAnyRole("ADMIN", "MODERATOR", "VIP")
                                .requestMatchers("/api/questions/deleteQuestion").hasAnyRole("ADMIN", "MODERATOR", "VIP")
                                .requestMatchers("/api/questions/getQuestionById").hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers("/api/questions/updateQuestion").hasAnyRole("ADMIN", "MODERATOR", "VIP")
                                .requestMatchers("/api/questions/getQuestionsByHost").hasAnyRole("ADMIN", "MODERATOR", "VIP")
                                .requestMatchers("/api/questions/getQuestionEntityById").hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers("/api/questions/getQuestionsByGameId").hasAnyRole("ADMIN", "MODERATOR")

                                .requestMatchers("/api/answerOptions/addAnswerOption").hasAnyRole("ADMIN", "MODERATOR", "VIP")
                                .requestMatchers("/api/answerOptions/deleteAnswerOption").hasAnyRole("ADMIN", "MODERATOR", "VIP")
                                .requestMatchers("/api/answerOptions/getAnswerOptionById").hasAnyRole("ADMIN", "MODERATOR", "VIP")
                                .requestMatchers("/api/answerOptions/updateAnswerOption").hasAnyRole("ADMIN", "MODERATOR", "VIP")
                                .requestMatchers("/api/answerOptions/getAnswerOptionEntityById").hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers("/api/answerOptions/getAnswerOptionsByQuestionId").hasAnyRole("ADMIN", "MODERATOR", "VIP")

                                .requestMatchers("/api/players/addPlayer").permitAll()
                                .requestMatchers("/api/players/**").hasAnyRole("ADMIN", "MODERATOR")

                                .requestMatchers("/api/playerAnswers/addPlayerAnswer").permitAll()
                                .requestMatchers("/api/playerAnswers/getPlayerAnswerById").hasAnyRole("ADMIN", "MODERATOR", "VIP")
                                .requestMatchers("/api/playerAnswers/deletePlayerAnswer").hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers("/api/playerAnswers/getPlayerAnswersByPlayerId").hasAnyRole("ADMIN", "MODERATOR", "VIP")
                                .requestMatchers("/api/playerAnswers/getPlayerAnswersByQuestionId").hasAnyRole("ADMIN", "MODERATOR", "VIP")
                                .requestMatchers("/api/playerAnswers/validatePlayerAnswer").hasAnyRole("ADMIN", "MODERATOR", "VIP")






                                .anyRequest().permitAll()

                )
                .sessionManagement(x -> x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();


    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}