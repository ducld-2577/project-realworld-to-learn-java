package com.example.realworld.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.realworld.dto.TagListResponseDTO;
import com.example.realworld.service.TagService;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<TagListResponseDTO> getTags() {
        TagListResponseDTO response = tagService.getTags();
        return ResponseEntity.ok(response);
    }
}
