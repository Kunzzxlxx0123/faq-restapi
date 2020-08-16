package com.spring.faq.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusRequest{

	@Size(max = 100)
	@NotBlank
	private String title;

	@NotBlank
	private String description;

	public StatusRequest(@Size(max = 100) String title, String description) {
		super();
		this.title = title;
		this.description = description;
	}
	
}
