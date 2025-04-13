package com.smart.helper;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

// this class is made to remove session messages i.e. success/error after its alert. 
// HttpSession is used to store objects for a certain time
// when alert appears after form submission, it will disappear after refreshing page

@Component
public class SessionHelper {

	public void removeMessageFromSession() {
		
		try {
			
			System.out.println("Removing message from session . . .");
			HttpSession session	=	((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
			session.removeAttribute("message");
			
			//			to use this in thymeleaf :	use this below session message
			// <th:block th:text="${@sessionHelper.removeMessageFromSession()}"></th:block>
			//		Classname will be used in camel case
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
