package br.com.pi.goodtrip.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("embarques")
public class EmbarqueController {
	
	@GetMapping("ler/{id}")
	public String lerConvites(){
		
		return "Alô!!!!";
	}
}