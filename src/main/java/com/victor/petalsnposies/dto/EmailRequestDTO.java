package com.victor.petalsnposies.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailRequestDTO {
	private String name;
	private String email;
	private String eventDate;
	private String venue;
	private String message;
//	private String to;
//	private String subject; 
//	private String body;
}
