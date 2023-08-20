package ma.jwttoken.JWTtokens.service;

import ma.jwttoken.JWTtokens.models.RolesEntity;
import ma.jwttoken.JWTtokens.models.UserEntity;
import ma.jwttoken.JWTtokens.repository.UserReposiroty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerUserDetailsService implements UserDetailsService {


    private UserReposiroty userRepo;

    public CustomerUserDetailsService(UserReposiroty userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username not found"));
        return new User(userEntity.getUsername(), userEntity.getPassword(), mapRolesToAuthorities(userEntity.getRoles()));
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<RolesEntity> roles) {
        return roles.stream().map(MyRole -> new SimpleGrantedAuthority(MyRole.getNameRole())).collect(Collectors.toList());
    }
}
