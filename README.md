# 📇 SmartContactManager

SmartContactManager is a secure web application that allows users to register, log in, and manage their personal contacts. Each user can add, view, search, and delete multiple contacts through an intuitive UI built with Thymeleaf and Bootstrap.

---

## ✨ Features

- 🔐 User Registration and Login (Spring Security)
- ➕ Add Multiple Contacts Per User
- 📋 View All Contacts with Pagination
- 🔍 Search Functionality for Contacts
- ❌ Delete Contacts with SweetAlert Confirmation
- 💡 Responsive UI using Bootstrap
- ✅ Secure session management with Spring Security

---

## 🔄 Application Workflow

1. 📝 **Sign Up** — New users can register through the signup page.
2. 🔐 **Login** — After successful registration, users can log in.
3. 🏠 **Dashboard** — Once logged in, users are redirected to their personal dashboard.
4. ➕ **Add Contacts** — Users can add multiple contacts using a form.
5. 📋 **View Contacts** — All added contacts are displayed with pagination support.
6. 🔍 **Search Contacts** — Users can search for specific contacts by name.
7. ❌ **Delete Contacts** — Contacts can be removed with a confirmation popup using SweetAlert.

---

## 🛠 Tech Stack

- **Backend:** Spring Boot, Spring MVC, Spring Security
- **Frontend:** Thymeleaf, HTML, CSS, Bootstrap, SweetAlert (via CDN)
- **Database:** MySQL

---

## 📦 Installation

### Prerequisites

- Java 17+
- Maven
- MySQL Server

### Steps

1. Clone the repository:

git clone https://github.com/yourusername/SmartContactManager.git

2.Build and run the project using Maven:

./mvnw spring-boot:run

3.Access the app at:

http://localhost:8081

## 📸 Screenshots

![Login Page](![login](https://github.com/user-attachments/assets/d877b63b-d741-4c00-bfd1-5d3ac907fecc)
)
![User Dashboard](![viewcontacts](https://github.com/user-attachments/assets/03d832ad-d0b3-445c-bd79-44eeb34f23a7)
)


