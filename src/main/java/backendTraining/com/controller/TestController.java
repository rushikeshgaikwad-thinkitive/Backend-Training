package backendTraining.com.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String home() {
        return "EHR Backend Running";
    }

    @GetMapping("/test")
    public String test() {
        return "TEST API WORKING";
    }
}