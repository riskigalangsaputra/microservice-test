package org.example.dto;

import lombok.Data;

@Data
public class EmailDto {
    private String subject;
    private String to;
    private String body;
}
