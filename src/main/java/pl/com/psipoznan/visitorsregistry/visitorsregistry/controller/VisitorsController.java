package pl.com.psipoznan.visitorsregistry.visitorsregistry.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import pl.com.psipoznan.visitorsregistry.visitorsregistry.model.Identyficator;
import pl.com.psipoznan.visitorsregistry.visitorsregistry.model.Visitor;
import pl.com.psipoznan.visitorsregistry.visitorsregistry.repositories.IdentyficatorRepository;
import pl.com.psipoznan.visitorsregistry.visitorsregistry.repositories.VisitorRepository;

@Controller
public class VisitorsController {
	
	@Autowired
	private VisitorRepository visitorRepo;
	@Autowired
	private IdentyficatorRepository identyfRepo;

	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("register", new Visitor());
		return "visitor";
	}
	
	@PostMapping("/saveVisitor")
	public String registerSubmit(@ModelAttribute Visitor v) {
		
		Visitor visitor = new Visitor(v.getName(), v.getCompany());
		
		Identyficator identyf = identyfRepo.findFirstByActiveAndDeleted(false, false);
		visitor.setTicket(identyf.getKey());
		identyf.setActive(true);
		identyfRepo.saveAndFlush(identyf);
		System.out.println(visitor.getName() + " " + visitor.getCompany()
				+ " o: " + visitor.getEnter() + " identyf: " + visitor.getTicket());
		visitorRepo.saveAndFlush(visitor);
		
		return "visitor(registered=true)";
	}
	@GetMapping("/secured/visitors-view")
    public String messages(Model model) {
        model.addAttribute("visitors", visitorRepo.findAll());
        return "secured/visitors-view";
    }
}
