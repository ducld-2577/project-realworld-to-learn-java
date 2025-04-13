package com.example.realworld.dto;
import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentListResponseDTO {
    private List<CommentDTO> comments;
}
