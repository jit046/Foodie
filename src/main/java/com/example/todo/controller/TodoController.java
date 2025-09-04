package com.example.todo.controller;

import com.example.todo.model.Todo;
import com.example.todo.service.TodoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.findAll();
    }

    @GetMapping("/{id}")
    public Todo getTodoById(@PathVariable String id) {
        return todoService.findById(id).orElse(null);
    }

    @PostMapping
    public Todo createTodo(@RequestBody Todo todo, Authentication authentication) {
        return todoService.save(todo);
    }

    @PutMapping("/{id}")
    public Todo updateTodo(@PathVariable String id, @RequestBody Todo todo) {
        return todoService.save(todo);
    }

    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable String id) {
        todoService.deleteById(id);
    }
}

