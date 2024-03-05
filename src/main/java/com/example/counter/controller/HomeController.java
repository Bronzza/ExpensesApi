package com.example.counter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/rest/home")
@CrossOrigin(origins = "*")
public class HomeController {
    @ResponseBody
    @CrossOrigin
    @RequestMapping(value = "",method = RequestMethod.GET)
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("Hello World");
    }


}
