package com.disp.learnspringsecurity.model;

import com.disp.learnspringsecurity.AuthenticationFacade;
import com.disp.learnspringsecurity.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            var userObj = user.get();
            return new CustomUserDetails(
                    userObj.getUsername(),
                    userObj.getPassword(),
                    userObj.getEmail(),
                    getAuthorities(userObj)
            );
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        // Преобразуем роли в GrantedAuthority
        return Arrays.stream(user.getRole().split(","))
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

    public void updateUserProfile(UserProfileDto userProfileDto) {
        // Получаем текущего пользователя (например, из SecurityContext)
        String username = authenticationFacade.getAuthenticatedUsername();
        Optional<User> user = userRepository.findByUsername(username);

        // Обновляем данные пользователя
        user.get().setFirstName(userProfileDto.getFirstName().isEmpty() ? null : userProfileDto.getFirstName());

        user.get().setLastName(userProfileDto.getLastName().isEmpty() ? null : userProfileDto.getLastName());

//        user.get().setFirstName(userProfileDto.getFirstName());
//        user.get().setLastName(userProfileDto.getLastName());

        // Сохраняем обновленные данные в БД
        userRepository.save(user.get());
    }

/*
    private String[] getRoles(User user) {
        if (user.getRole() == null) {
            return new String[]{"USER"};
        }
        return user.getRole().split(",");
*/

}
