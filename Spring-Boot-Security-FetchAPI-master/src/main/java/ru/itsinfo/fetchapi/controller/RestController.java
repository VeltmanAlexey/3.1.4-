package ru.itsinfo.fetchapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itsinfo.fetchapi.model.Role;
import ru.itsinfo.fetchapi.model.User;
import ru.itsinfo.fetchapi.service.AppService;

import javax.validation.Valid;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {
    private final AppService appService;

    @Autowired
    public RestController(AppService appService) {
        this.appService = appService;
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(appService.findAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(appService.getOneUser(id));
    }

    @PostMapping("/users")
    public ResponseEntity<User> insert(@Valid @RequestBody User user) {
        return ResponseEntity.ok(appService.insertUser(user));
    }

    @PutMapping("/users")
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        return ResponseEntity.ok(appService.updateUser(user));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        appService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/roles")
    public ResponseEntity<Iterable<Role>> findAllRoles() {
        return ResponseEntity.ok(appService.findAllRoles());
    }
}
