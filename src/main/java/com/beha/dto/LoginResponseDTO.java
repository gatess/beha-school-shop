package com.beha.dto;

import java.util.List;

import lombok.Data;

@Data
public class LoginResponseDTO {
		private String errorMessage;
		private String status;
		private List<LessonDTO> lesson;
}
