package com.smart.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.entities.Contact;
import com.smart.entities.User;


@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer>{
		
					//There is no major requirement of this but it is only being made to make Pagination on show_contacts
						// userId se uske sare Contacts fetch ho jayenge
	
	//Pagination . . .
	
	@Query("SELECT c FROM Contact c WHERE c.user.id = :userId")
				//List<Contact> findByUserId(@Param("userId") int userId);	//Page lagane se pehle List se sara data show kara rahe the show contacts me
				
				// c.user.id => c matlab Contact me jayega, usme user variable ko dhundega fir  User entity me jakar uski id variable lega
				// jab findByUserId call hogi to wo int userId me chali jayegi fir wo @Param ke userId me jayegi aur wahan se Query me
				//har user ki apni id hai jaise 2, to uske andar jitni bhi contacts banenge to har ek contact me userid 2 he rahegi.
				// where clause se contact me jo userid hai match hogi jo humne provide kari hai function se aur saari contacts ko viewcontact me show kara lenge

	Page<Contact> findByUserId(@Param("userId") int userId, Pageable pageable);
				//Page interface hai. Ye sublist hai list ka. Iski help se kisi position(jaise per page) par jo information hai usse gain kar sakte hain
				//Pageable interface hai jiske paas 2 information hogi , current page aur kitni contacts dikhani hai ek page me
				//															i.e currentPage= page, Contact per page =5
	
	
	
	//Search Contacts
	
	public List<Contact> findByNameContainingAndUser(String name, User user);			//iss method ko SearchController.java ke andar use kiya gaya hai
									// Breakdown of above predefined method :
							//findByName -> ye db se jo bhi input denge usko dhund ke layga	query chalakar. (Name field ka naam hai, iske jagah koi bhi field le sakte hain jisse search karwana hai)
							//Containing ->	ye wo har naam nikaal ke aayga jiske andar wo characters honge, jaise humne "lo" pass kiya search me to har wo naamjisme "lo" aayenge usko show karega...alok, shlok, love, etc
							//AndUser -> upar tak mein agar hum search karenge to wo bhi contacts show kar dega jo dusre contacts me honge,
												//hum chahte hain ke jo current User logged in hai usime se contacts search results nikle.
												//iske liye parameter me bhi User ko pass kiya gaya hai.
							// overall ye kaam kar raha hai ke User ke andar me jo bhi search input denge usse related saare contacts show hoga
			
	
	
}
