package com.mcnz.cartel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

public class CartelController {

    private UserAccountRepository users;

    public List<UserAccount> users() {
        return users.findAll();
    }
}
