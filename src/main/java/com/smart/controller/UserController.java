package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
// user authorization ke liye controller i.e. localhost:8081/user/****
public class UserController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	// method for adding common data to response
	
	@ModelAttribute					//ye function user ko har handler me show karne ke liye banaya gaya hai, ModelAttribute se ye har baar chal payega
	public void addCommonData(Model model, Principal principal) {		//Principal security ka part hai jiske help se user ka unique identifier nikalenge
		
		String userName	=	principal.getName();
		System.out.println("USERNAME "+userName);
		// iske andar ke data after loggedin ke liye hai
		//pehle principal ke help se unique identifier nikal raha i.e. email, fir jab ye aa gaya to iski help se user ka pura data fetch kara lenge
		
		//get the user all data using userName(email)
		User user	=	userRepository.getUserByUserName(userName);
		System.out.println("USER "+user);
		
		model.addAttribute("user", user);  // iski help se form ke andar jahan bhi user ka data chahiye fetch kara lenge like user.name
		
	}
	
	
	// dashboard home
	@RequestMapping("/index") 												//isko open karne ke liye localhost:8081/user/index likhna hoga
	public String dashboard(Model model, Principal principal) { 
		
		model.addAttribute("title", "User Dashboard");
		return "normal/user_dashboard"; 												//normal folder ke andar user_dashboard file hai
	}
	
	
	
	
	// open add-contact form handler in user_dashboard
	
	@GetMapping("/add_contact")
	public String openAddContactForm(Model model) {
		
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		
		return "normal/add_contact_form";
	}
	
	
	
	
	
	//processing add contact form
	
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Principal principal, HttpSession session) {
		//Image ko kisi variable me store nahi kiya ja sakta, isiliye image ko @Requestparam ke help se alag se recieve karne ke baad
				// usko multipartFile me store kiya ja raha.
		//@ModelAttribute se addcontactform ke input field ke attribute Contact.java ke attribute me chale jayenge. Contact ke variables form ke name attribute se match honge
		
		try {

			String name	=	principal.getName();
			User user	=	userRepository.getUserByUserName(name);	
			
			
			/* if(3>2) { throw new Exception(); } */  // It was used for the demonstration of error message inside catch
			 
				
			contact.setUser(user);	// Bidirection mapping hone ke wajah se contact mein user ko set kiya ja raha, ye nahi hua to user me id nahi aayga
			
			
			//processing and uploading file(here image) . . . 
			
			if (file.isEmpty()) {
				
				//if file is empty then try our message
				System.out.println("File is empty");
				contact.setImage("contact.png");		//add contact ke wakt agar image nahi dala to ye wala image show karega
				
			} else {

				//upload the file to the folder and update the filename to contact
				contact.setImage(file.getOriginalFilename());
				
				File saveFile	=	new ClassPathResource("static/image").getFile();
				
				Path path	=	Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
				System.out.println("Image is uploaded");
				// Note: Profile me image upload hone ke baad project explorer ke static/image me nahi dikhega image
				// dekhne ke liye : right click on project name -> show in -> System Explorer -> Cose again Project Name -> target -> classes -> static -> image -> files
			
			}
			
			
			// pehle hum Principal ki help se jis user ne login kiya hai uska naam nikalenge, fir usko user me store kara lenge. Ab usi user contacts ko add karte jayenge
			
			user.getContacts().add(contact); //getContacts me List hai, to list aaygi aur usme contact ko  add karte jayenge
			
			userRepository.save(user); // user to already hai he db mein, ab uske andar contacts bhi aa jayenge
			
			System.out.println("DATA "+contact);
			
			System.out.println("Added to database ");
			
			//contact added succes message
			
			session.setAttribute("message", new Message("Your contact is added !! Add more . . .", "success"));
			// Message.java se new Message object create ho raha aur constructor me content & type pass hua
			// add_contact_form.html ke top par laga hai
			
		} catch (Exception e) {
			System.out.println("ERROR "+e.getMessage());
			e.printStackTrace();	//It will specify detailed Exception
			
			//contact added error message
			
			session.setAttribute("message", new Message("Something went wrong !! Try again . . .", "danger"));
		}
		
		return "normal/add_contact_form";
	}
	
	
	
	
	// 	show control handler
	
	@GetMapping("/show-contacts/{page}")
	public  String showContacts(@PathVariable("page") Integer page, Model model, Principal principal) {
		
		model.addAttribute("title", "View User Contacts");
		
		//contacts ki list ko bhejni hai
		
		/*     one way of getting all contacts. If use this, there will be no need to make ContactRepository
		 								String userName = principal.getName();
		 								User user = userRepository.getUserByUserName(userName); 
		 								List<Contact>	contacts = user.getContacts();
		 */
		
		String userName = principal.getName();		// It will fetch email
		
		User user = userRepository.getUserByUserName(userName);
		//pricipal se email nikal kar, usse user ki sari detail nikalkar user object me store kara diye , 
				//user ke id ki help se, repository me query se contacts ki id match kara kar, puri Contact List nikal jayegi.
		
		
		
		//for Pagination : per page = 5    .. 5 value hai jiska matlab per page kitne contact show honge
		//							current page =  [page]				..  page ek variable hai jo batayga current page
		
		Pageable pageable	=	PageRequest.of(page, 5);		// yahan par dono variable pass ho rahi hai i.e currentpage & ContactPerPage
		
		//List<Contact> contacts	=	contactRepository.findByUserId(user.getId()); //Page lagane se pehle
		Page<Contact> contacts	=	contactRepository.findByUserId(user.getId(), pageable);		//Page lagane ke baad, isme pageable pass hua jiskepaas pagination variable hai
		 
		
		model.addAttribute("contacts", contacts); // isse hum saare contacts dekh sakenge
		
		model.addAttribute("currentPage", page);	// ye batayga ke aap kis page par ho
		
		model.addAttribute("totalPages",contacts.getTotalPages());	// ye batayga ke pagination me total kitne pages hai
		
		return "normal/show_contacts";
	}
	
	
	
	
	
	
	//showing specific contact details after clicking in emailid of contacts view
	
	@RequestMapping("/{cId}/contact")
	public String showContactDetail(@PathVariable("cId") Integer cId, Model model) {
		System.out.println("C.ID "+cId);
		
		Optional<Contact> optional	=	contactRepository.findById(cId);
		Contact contact		=	optional.get();
		
		model.addAttribute("contact", contact);
		model.addAttribute("title", contact.getName());		//It will show in the title of this url
		
		return "normal/contact_detail";
	}
	
	
	
	
	
	
	// delete contact handler
	
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cId, Model model,Principal principal , HttpSession session) {
		
		/*
		 		 Optional<Contact> optional = contactRepository.findById(cId); 
		 		 Contact contact	= optional.get();
		 		 
		 		 contactRepository.delete(contact); 
		 		 
		 		 // solving Delete Bug 1 :
		 		 //Generally itne se he delete ho jata hai, but yahan delete nahi ho pa raha hai, kyuki
		 		 //contact user se associated hai. jab humne user me mapping kari thi tab humne
		 		 //usme cascadeType.All lagaya tha jisse contact linked hai user se.
		 */
		// To humko pehle contact ko user se unlink karna hoga
		
		Optional<Contact> optional = contactRepository.findById(cId); 
		 Contact contact	= optional.get();
			
			  		System.out.println("Contact  "+contact.getcId()); 
			  		/*
			  		contact.setUser(null); // isse unlink ho raha hai
			  		contactRepository.delete(contact);
			  		 */
			  		
		
		 // Solving delete bug 2 :
		 // Upar ke 3 steps karne se bhi delete nahi hua. 
		 // Unlink hone ke baad us contact ka userid me null save ho ja raha tha, jisse wo contact show_contact me nahi aa raha tha, but db me abhi bhi present tha
		 //	sabse pehle to User.java ke @OneToMany(orphanRemoval=true) me ye parameter pass karna hai
		 // orphanRemoval : kyuki linking aisi hai ke one user can have many contacts, User parent entity hai aur Contact child entity,
			  		//jab bhi child entity unlink ho jayegi apne parent se tab usko remove kar diya jayega
		 			 
		User user	 =	userRepository.getUserByUserName(principal.getName());	// isse current user niklega jo loggedin hai
		user.getContacts().remove(contact);	
		userRepository.save(user);
		//current user se uske saare contacts nikal lenge, fir usme se jo humne optional se cId nikali usko remove kara denge
		//saare List se wo wala partcular contact remove hone ke liye bhi objects ki matching karani padegi
		//iske liye Contact.java me equals() method ko override kiya gaya hai
		//jab object equal ho jayega to contact hatega
		//JVM internally equal ko call karega aur ek ek karke check karega jo remove contact se match kare, fir usko remove kar dega
		//process ke baad ,save(user) se jo user already db me hai usko update kar dega jisse contacts bhi update ho jayenge remove ke baad
		
		System.out.println("DELETED");
		 
		session.setAttribute("message", new Message("Contact deleted successfully . . .", "success" ));
		
		return "redirect:/user/show-contacts/0";
	}
	
	
	
	
	
	// open update form handler
	
	@PostMapping("/update-contact/{cid}")		// ye url show_contacts.html & contact_detail.html ke update button par form tag ke th:action me lagakar istemal kiya gaya hai
	public String updateForm(@PathVariable("cid") Integer cid, Model model) {
		
		model.addAttribute("title", "Update Contact");
		
		Contact contact		 =	contactRepository.findById(cid).get();
		
		model.addAttribute("contact", contact); // isiko update form me dikhana hoga
		
		return "normal/update_form";
	}
	
	
	
	
	
	// update contact handler
	
	@RequestMapping(value = "/process-update", method = RequestMethod.POST)
	public String updateHandler(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file, 
													Model model, HttpSession session, Principal principal) {
		
		try {
			
			//fetching old contact details
			Contact oldContactDetail =	contactRepository.findById(contact.getcId()).get(); //iska use bas purane detail nikalne me hoga
			
				//image. .
			if(!file.isEmpty()) { //jab file empty nahi hogi
				
				//file work..
				//rewrite
				
				//delete old photo
						// jab bhi profile me file upload karte hain to wo project explorer me na jakr system explorer(server) ke target folder me chala jata hai,
						// aur agar aise he direct update karenge to purani photo bhi rahegi server me aur nayi bhi rahegi
				
				File deleteFile	=	new ClassPathResource("static/image").getFile();
				File file1 =		new File(deleteFile, oldContactDetail.getImage());
				file1.delete();
				
				
				
				//upload new photo
				File saveFile	=	new ClassPathResource("static/image").getFile(); // folder me se image file ko nikalkar saveFile me store kiya
				
				Path path	=	Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename()); // us file ka absolute path nikalkar usko store karaya original filename se, aur yahi path pass hoga copy() mein
				
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING); //Finally path ko copy ke andar pass kara ke store kiya gaya image ko
				//File se Files tak ki line mein image ko folders se nikal kar save kiya ja raha hai , yahi kaam humne process-contact url me bhi kiya hai
				
				contact.setImage(file.getOriginalFilename());  // file jis naam se upload hogi wahi naam db me store ho jayega
			}
			else {
				//agar file empty hai, to puraane se nikalkar naye me daal dena hai, matlab jo pehle se rahega wahi set ho jayega
				contact.setImage(oldContactDetail.getImage());
			}
			
			
			
			User user	=	userRepository.getUserByUserName(principal.getName()); //yahan se current user ki id niklegi
			
			contact.setUser(user); //aur user ki id ko contact me set kara diye
			
			contactRepository.save(contact); // if ke baad ye line likhne se update save nahi ho raha tha, id null ho ja rahi thi.
			// isliye principal se userid nikalkar contact me set karana pada
			
			session.setAttribute("message", new Message("Contact updated successfully . . .", "success"));
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		System.out.println("CONTACT NAME "+contact.getName());
		System.out.println("CONTACT ID "+contact.getcId());
		return "redirect:/user/"+contact.getcId()+"/contact"; //dynamic url ban raha, isse jahan update ho raha save hone ke baad usi id ka page khulega
	}
	
	
	
	
	
	// your profile handler
	@GetMapping("/profile")
	public String yourProfile(Model model) {
		
		model.addAttribute("title", "Profile Page");
		//sabse top par @ModelAttribute ke andar addCommonData() mein humne user ka call kiya he hai to hame kuch karne ki jarurat nahi hai 
		
		return "normal/profile";
	}
	
	
	
	
	
	//open settings handler
	@GetMapping("/settings")
	public String openSettings(Model model) {
		
		model.addAttribute("title", "Settings");
		return "normal/settings";
	}
	
	
	
	
	//change password handler
	
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,
												@RequestParam("newPassword") String newPassword, 
													Principal principal, HttpSession session  ) {			
	//Form se data uthana hai isliye @RequestParam ka use ho raha hai, matlab jo bhi data input field me bhara jayga usko fetch kar lega.  agar url se uthana hota to @PathVariable ka use hota
																//form se data uthaya ja raha hai name attribute ki madad se
		
		System.out.println("OLD PASSWORD : "+oldPassword);
		System.out.println("NEW PASSWORD : "+newPassword);
		
		String userName	=	principal.getName();
		User currentUser	=	userRepository.getUserByUserName(userName);
		System.out.println(currentUser.getPassword()); //password to nikal jayga par wo encrypted rahega kyuki BCrypt laga hua hai
		
		if(passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
			
			// if old password provided by user matches to saved old password, then
			//change the password ....... by the newly entered password
			
			currentUser.setPassword(passwordEncoder.encode(newPassword));  // it will set newly entered password
			userRepository.save(currentUser);									//new password will be saved to db
			session.setAttribute("message", new Message("Your password is successfully changed !!", "success"));
		} else {
			
			//error .................... entered old and new didn't matched
			session.setAttribute("message", new Message("Please enter the correct old password. . .", "danger"));
			return "redirect:/user/settings";
		}
		
		return "redirect:/user/index";
	}
	
	
	
	
	
}
