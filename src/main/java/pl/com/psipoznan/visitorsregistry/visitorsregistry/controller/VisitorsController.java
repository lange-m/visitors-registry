package pl.com.psipoznan.visitorsregistry.visitorsregistry.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import pl.com.psipoznan.visitorsregistry.visitorsregistry.model.Visitor;
import pl.com.psipoznan.visitorsregistry.visitorsregistry.repositories.VisitorRepository;

@Controller
public class VisitorsController {
	
	@Autowired
	private VisitorRepository visitorRepo;

	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("register", new Visitor());
		return "visitor";
	}
	
	@PostMapping("/saveVisitor")
	public String registerSubmit(@ModelAttribute Visitor v) {
		Visitor visitor = new Visitor(v.getName(), v.getCompany());
		System.out.println(visitor.getName() + " " + visitor.getCompany()
				+ " o: " + visitor.getEnter());
		visitorRepo.saveAndFlush(visitor);
		
		return "visitor";
	}
}
