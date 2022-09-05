package com.veltman.service;

import com.veltman.model.Role;
import com.veltman.model.User;
import com.veltman.repository.RoleRepository;
import com.veltman.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class AppServiceImpl implements AppService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppServiceImpl(UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String getPage(Model model, @Nullable Authentication auth) {
        if (Objects.isNull(auth)) {
            return "login-page";
        }

        User user = (User) auth.getPrincipal();
        model.addAttribute("user", user);

        if (user.hasRole("ROLE_USER")) {
            return "user-page";
        } else if (user.hasRole("ROLE_ADMIN")) {
            return "main-page";
        }
        return "login-page";

    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", email))
        );
    }

    @Override
    @Transactional
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User getOneUser(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    @Transactional
    public User insertUser(User user) {

        String oldPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            user = userRepository.save(user);
        } catch (Exception e) {
            user.setPassword(oldPassword);
        }
        return user;
    }

    @Override
    @Transactional
    public User updateUser(User user) {

        String oldPassword = user.getPassword();
        user.setPassword(user.getPassword().isEmpty() ?
                getOneUser(user.getId()).getPassword() :
                passwordEncoder.encode(user.getPassword()));
        try {
            user = userRepository.save(user);
        } catch (Exception e) {
            user.setPassword(oldPassword);
        }

        return user;
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Iterable<Role> findAllRoles() {
        return roleRepository.findAll();
    }


}
