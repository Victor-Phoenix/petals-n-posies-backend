package com.victor.petalsnposies.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailRequestDTO {
	private String to;
	private String subject; 
	private String body;
}
