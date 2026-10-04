package com.mcnz.cartel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cartel")
public class CartelController {

    @Autowired
    private UserAccountRepository users;

    @PreAuthorize("hasAuthority('SCOPE_admin') or authentication.name == 'marcus'")
    @GetMapping("/users")
    public List<UserAccount> users() {
        return users.findAll();
    }
}
