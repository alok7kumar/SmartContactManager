package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title","Home - Smart Contact Manager"); // It is used in base.html <title>
		return "home";
	}
	
	// handler for custom login
	 @GetMapping("/login")
	 public String loginPage(Model model) {
	     model.addAttribute("title", "Login Page");
		 return "login"; // Points to login.html
	    }
	
	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title","About - Smart Contact Manager");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title","Register - Smart Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}

	//handler for registering user
	@RequestMapping(value = "/do_register", method = RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result1,
			@RequestParam(value = "agreement",defaultValue = "false") boolean agreement, Model model, 
			  HttpSession session) {
		//@Valid is used for allow validation written above variables in User(POJO)
		// Validation means like providing name, email,etc is mandatory in fields
		//BindingResult comes under Validation dependency
		//jab user form me jayga to saare fields match honge bas agreement checkbox match nahi hoga
		//checkbox ka name="agreement", ye pojo me declare nahi hai, to yahan checkbox ke boolean variable banaya gaya hai agreement, isime check ka data recieve hoga.
		//default value false diya ja raha hai, taki pehle se he checked naa dikhe
		
		try {
			
			if( !agreement)
			{
				System.out.println("You have not agreed Terms and Conditions");
				throw new Exception("You have not agreed Terms and Conditions"); //agar agreement nahi kiya to yahan se catch me chala jayega, user add karega aur "went wrong " message show karega
			}
			
			if(result1.hasErrors()) {		// if validation found error then it will return back to signup
				System.out.println("ERROR " + result1.toString());
				model.addAttribute("user", user);
				return "signup";
			}
			
			
			//agar Agreement(checkbox check kiya)
			user.setRole("ROLE_USER");
			user.setEnabled(true);	//this is to show if user is active
			user.setImageUrl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			//Above 3 variables are not mentioned in form, So it is set here.
			
			
			System.out.println("Agreement "+agreement);
			System.out.println("USER "+user);
			
			User result	=	userRepository.save(user);
			
			model.addAttribute("user", new User()); //save ke baad naya User set kar dega aur field blank aayga form me.
			session.setAttribute("message", new Message("Successfully Registered !!", "alert-success"));
			// Message.java se new Message object create ho raha aur constructor me content & type pass hua
				//ye message signup.html mein form ke upar lagaya gaya hai
			return "redirect:/login";	//"signup"
			
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went Wrong !!"+e.getMessage(), "alert-danger"));
			return "signup";
		}
		
	}
	
}	
	






	
	/* 
	 * 			Following method is only made to test , if data is sending correctly to db or not, and checking 3rd unwanted table
	@GetMapping("/test")
	@ResponseBody //due to this it will return "Working" in browser, aur jaise he url call hoga name, email db mein set ho jayega
	public String test() {
		User user	=	new User();
			user.setName("Alok Kumar");
			user.setEmail("ak@gmail.com");
			
			userRepository.save(user);
		return "Working";
	}
	 */

