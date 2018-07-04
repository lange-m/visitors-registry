package pl.com.psipoznan.visitorsregistry.visitorsregistry.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyController {

	/**
	 * kontroler dla index.html 
	 * mapuje widok do podanej w adnotacji sciezki
	 * w tym przypadku widok jest dostepny z poziomu localhost:8080/
	 */
	@GetMapping("/")
	public String index(Model model) {

		model.addAttribute("message", "Hello Spring MVC 5!");
		return "index";
	}
}
