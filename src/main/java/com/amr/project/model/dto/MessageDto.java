package com.amr.project.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private Long id;
    private Long chat;
    private Long to;
    private Long from;
    private String textMessage;

    public MessageDto(Long id, Long to, Long from, String textMessage) {
        this.id = id;
        this.to = to;
        this.from = from;
        this.textMessage = textMessage;
    }
}
