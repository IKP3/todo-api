package io.github.ikp.todoapi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {
  @GetMapping(path = "/tasks")
  public String test(){
    return "Hello World!";
  }

}
