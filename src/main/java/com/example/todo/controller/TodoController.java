package com.example.todo.controller;

import com.example.todo.model.Todo;
import com.example.todo.sevice.TodoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('SCOPE_Todo.User')")
    public Todo createTodo(@RequestBody Todo todo, Authentication authentication) {
        return todoService.save(todo);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_Todo.User')")
    public Todo updateTodo(@PathVariable String id, @RequestBody Todo todo) {
        return todoService.save(todo);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_Todo.Admin')")
    public void deleteTodo(@PathVariable String id) {
        todoService.deleteById(id);
    }
}

