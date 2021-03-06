package pl.com.psipoznan.visitorsregistry.visitorsregistry.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import pl.com.psipoznan.visitorsregistry.visitorsregistry.model.Identyficator;
import pl.com.psipoznan.visitorsregistry.visitorsregistry.model.RememberedVisitor;
import pl.com.psipoznan.visitorsregistry.visitorsregistry.model.Visitor;
import pl.com.psipoznan.visitorsregistry.visitorsregistry.repositories.IdentyficatorRepository;
import pl.com.psipoznan.visitorsregistry.visitorsregistry.repositories.RememberedVisitorRepository;
import pl.com.psipoznan.visitorsregistry.visitorsregistry.repositories.VisitorRepository;
import pl.com.psipoznan.visitorsregistry.visitorsregistry.tools.excel.ExcelGenerator;

@Controller
public class VisitorsController {
	
	@Autowired
	private VisitorRepository visitorRepo;
	@Autowired
	private IdentyficatorRepository identyfRepo;
	@Autowired
	private RememberedVisitorRepository rememberedRepo;
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("register", new Visitor());
		return "visitor";
	}
	
	@PostMapping("/registerVisitor")
	public String registerSubmit(@ModelAttribute RememberedVisitor rv, Model model) {
		
		rememberedRepo.saveAndFlush(rv);
		
		model.addAttribute("name", rv.getLogin() + " ( " + rv.getName() + " z " + rv.getCompany() + " )");
		model.addAttribute("registered", true);
		
		return "secured/visitors-sign-up";
	}
	
	@PostMapping("/saveVisitor")
	public String saveSubmit(@ModelAttribute Visitor v, Model model) {
		
		Visitor visitor = null;
		
		if (v.getName().startsWith("#") && (v.getCompany() == null || v.getCompany().isEmpty())) {
			RememberedVisitor rv = rememberedRepo.findByLogin(v.getName());
			visitor = new Visitor(rv.getName(), rv.getCompany());
			rv.setLastVisit(LocalDateTime.now());
			rememberedRepo.saveAndFlush(rv);
		} else {
			visitor = new Visitor(v.getName(), v.getCompany());
		}
		
		String like = "";
		
		Collection<SimpleGrantedAuthority> authorities = 
				(Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		
		if (authorities
				.stream().filter(authority -> authority.getAuthority().equals("ROLE_USER_B"))
				.findFirst().isPresent()) {
			like = "B%";
		} else if (authorities
				.stream().filter(authority -> authority.getAuthority().equals("ROLE_USER_A"))
				.findFirst().isPresent()) {
			like = "A%";
		}
		
		Identyficator identyf = identyfRepo.findFirstByActiveAndDeletedAndKeyLike(false, false, like);
		if (identyf == null) {
			model.addAttribute("noIdentyf", true);
			return "visitor";
		}
		
		visitor.setTicket(identyf.getKey());
		identyf.setActive(true);
		identyfRepo.saveAndFlush(identyf);
		System.out.println(visitor.getName() + " " + visitor.getCompany()
				+ " o: " + visitor.getEnter() + " identyf: " + visitor.getTicket());
		visitorRepo.saveAndFlush(visitor);
		
		model.addAttribute("ticket", visitor.getTicket());
		model.addAttribute("registered", true);
		
		return "visitor";
	}
	
	@GetMapping("/secured/visitors-view")
    public String visitorsView(Model model) {
        model.addAttribute("visitors", visitorRepo.findAll());
        return "secured/visitors-view";
    }
	
	@GetMapping("/secured/identyf-view")
    public String identyfView(Model model) {
        model.addAttribute("identyficators", identyfRepo.findAll());
        return "secured/identyf-view";
    }
	
	@GetMapping("/secured/signed-up-view")
    public String signedUpView(Model model) {
        model.addAttribute("registeredVisitors", rememberedRepo.findAll());
        return "/secured/signed-up-view";
    }
	
	@GetMapping("/visitor-out")
	public String visitorOut() {
		return "visitor-out";
	}
	
	@GetMapping("/generate")
	public String genIdentyf() {
		for (int i = 1 ; i <= 25 ; i++) {
			Identyficator identyfA = new Identyficator(i < 10 ? "A0"+i : "A"+i);
			Identyficator identyfB = new Identyficator(i < 10 ? "B0"+i : "B"+i);
			identyfRepo.saveAndFlush(identyfA);
			identyfRepo.saveAndFlush(identyfB);
		}
		return "visitor";
	}

	@PostMapping("/end-visit")
	public String endVisit(@RequestParam String identyf) {
		System.out.println(identyf);
		Identyficator identyficator = identyfRepo.findByKey(identyf);
		Visitor visitor = visitorRepo.findByTicket(identyf);
		
		visitor.setExit(LocalDateTime.now());
		visitor.setTicket(null);
		
		identyficator.setLastUserId(visitor.getId());
		identyficator.setActive(false);
		visitorRepo.saveAndFlush(visitor);
		identyfRepo.saveAndFlush(identyficator);
		
		return "index";
	}
	
	@RequestMapping(value="/end-visit-by-admin")
	public ModelAndView endVisitByAdmin(@RequestParam String identyf) {
		
		System.out.println(identyf);
		
		Identyficator identyficator = identyfRepo.findByKey(identyf);
		Visitor visitor = visitorRepo.findByTicket(identyf);
		
		visitor.setExit(LocalDateTime.now());
		visitor.setTicket(null);
		
		identyficator.setLastUserId(visitor.getId());
		identyficator.setActive(false);
		visitorRepo.saveAndFlush(visitor);
		identyfRepo.saveAndFlush(identyficator);
		
		return new ModelAndView("redirect:/secured/visitors-view");
	}
	
	@RequestMapping(value="secured/files/{fileName}", method=RequestMethod.GET)
	public void getFile(
			@PathVariable("fileName") String filename,
			HttpServletResponse response) throws IOException {
		
		ExcelGenerator excel = new ExcelGenerator();
		
		response.getOutputStream().write(excel.generate(
				new String[] {"Nazwisko","Firma","Identyfikator","Wejście","Wyjście"}, visitorRepo.findAll()));
		response.setContentType("application/vnd.ms-excel");
		response.flushBuffer();
	}
}
