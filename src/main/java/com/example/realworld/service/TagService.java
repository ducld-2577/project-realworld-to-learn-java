package com.example.realworld.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.realworld.dto.TagDTO;
import com.example.realworld.dto.TagListResponseDTO;
import com.example.realworld.repository.TagRepository;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public TagListResponseDTO getTags() {
        List<TagDTO> tags = tagRepository.findAll().stream().map(tag -> new TagDTO(tag.getName()))
                .collect(Collectors.toList());
        return new TagListResponseDTO(tags);
    }
}
