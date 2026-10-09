package com.bakery.bakeryordersystem.config;
import com.bakery.bakeryordersystem.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** Creates the demo logins on start-up (only fills in a password if the account has none yet). */
@Component
public class DataInitializer implements CommandLineRunner {
    private final UserService users;
    public DataInitializer(UserService users) { this.users = users; }
    @Override public void run(String... args) {
        users.seedAccount("admin@bakery.lk", "ADMIN", "Shop Admin", "0112345678", "admin123");
        users.seedAccount("nimal@mail.com", "CUSTOMER", "Nimal Perera", "0771234567", "nimal123");
    }
}
