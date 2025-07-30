package edu.man.dgs.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "Welcome! Visit /giql for GraphiQL Playground or /graphql for GraphQL endpoint.";
    }
}
