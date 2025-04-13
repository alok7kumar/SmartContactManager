package com.smart.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	//	 It is made for authentication with emailid. 
	@Query("select u from User u where u.email = :email")
	public User getUserByUserName(@Param("email") String email);
	// when we will write email in signin page, it will store in Param("email") then it will go to :email parameter in query to get matched and fetch details
	//	@Param is used to fetch dynamic content in variable that is stored it in String email when user enters in signin page.
	//		then @Param(email) will send to query parameter :email, then it will be matched with db i.e. u.email
	
	

}
