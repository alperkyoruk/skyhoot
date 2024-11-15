package skylab.skyhoot.WebAPI.controllers;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import skylab.skyhoot.Business.abstracts.UserService;
import skylab.skyhoot.core.result.DataResult;
import skylab.skyhoot.core.result.ErrorDataResult;
import skylab.skyhoot.core.result.SuccessDataResult;
import skylab.skyhoot.core.security.JwtService;
import skylab.skyhoot.entities.DTOs.User.AuthRequest;
import skylab.skyhoot.entities.Role;
import skylab.skyhoot.entities.User;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private UserService userService;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }


    @PostMapping("/generateToken")
    public DataResult<String> generateToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            // Cast to UserDetails to access the authorities (roles)
            User user = (User) authentication.getPrincipal();

            // Check if the user has any of the allowed roles
            boolean hasAllowedRole = user.getAuthorities().stream()
                    .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN") ||
                            role.getAuthority().equals("ROLE_MODERATOR") ||
                            role.getAuthority().equals("ROLE_VIP"));

            if (hasAllowedRole) {
                return new SuccessDataResult<>(jwtService.generateToken(authRequest.getUsername()), "Token generated successfully");
            }
            return new ErrorDataResult<>("Access denied: Insufficient role permissions");
        }

        return new ErrorDataResult<>("Invalid username or password");

    }
}
