package ma.jwttoken.JWTtokens.controller;

import ma.jwttoken.JWTtokens.dto.AuthResponseDto;
import ma.jwttoken.JWTtokens.dto.LoginDto;
import ma.jwttoken.JWTtokens.dto.RegisterDto;
import ma.jwttoken.JWTtokens.models.RolesEntity;
import ma.jwttoken.JWTtokens.models.UserEntity;
import ma.jwttoken.JWTtokens.repository.RoleRepository;
import ma.jwttoken.JWTtokens.repository.UserReposiroty;
import ma.jwttoken.JWTtokens.security.JWTGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserReposiroty userRepo;
    private RoleRepository rolerepo;
    private PasswordEncoder passwordEncoder;
    private JWTGenerator jwtGenerator;

    public AuthController(AuthenticationManager authenticationManager, UserReposiroty userRepo, RoleRepository rolerepo, PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepo;
        this.rolerepo = rolerepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token =jwtGenerator.generetToken(authentication);
            return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        if (userRepo.existsByUsername(registerDto.getUsername())) {
            return new ResponseEntity<>("Username is taken!", HttpStatus.BAD_REQUEST);
        }

        UserEntity buildUser = new UserEntity();
        buildUser.setUsername(registerDto.getUsername());
        buildUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        RolesEntity buildRole = rolerepo.findByNameRole("USER").orElse(null);
        buildUser.setRoles(Collections.singletonList(buildRole));

        userRepo.save(buildUser);

        return new ResponseEntity<>("User register success!", HttpStatus.OK);
    }
}
