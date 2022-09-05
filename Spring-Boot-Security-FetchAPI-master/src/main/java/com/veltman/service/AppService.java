package com.veltman.service;

import com.veltman.model.Role;
import com.veltman.model.User;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.ui.Model;

import java.util.List;

public interface AppService extends UserDetailsService {

    List<User> findAllUsers();

    User getOneUser(Long id);

    User insertUser(User user);

    User updateUser(User user);

    void deleteUser(Long id);

    Iterable<Role> findAllRoles();

    String getPage(Model model, @Nullable Authentication auth);
}
