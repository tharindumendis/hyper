Hyper POS

📌 Overview

Hyper POS is a modern Point of Sale (POS) system built using Spring Boot. It is designed to provide a seamless, efficient, and scalable solution for managing sales transactions, grn, and customer data.

🚀 Features

✅ User authentication & role-based access control
✅ Product & grn management
✅ Sales processing & receipt generation
✅ Customer management
✅ Reports & analytics dashboard
✅ RESTful API for integration with other systems✅ Secure payment processing

🛠️ Tech Stack

Backend: Spring Boot

Database: MySQL

Frontend (Optional): React / Angular / Vue.js

Other: Docker, JWT Authentication, Lombok

⚙️ Installation

📌 Prerequisites

Java 21+

Maven

MySQL

🔧 Steps

1️⃣ Clone the repository:

https://github.com/tharindumendis/hyper.git

2️⃣ Configure the database in application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/hyper_pos
spring.datasource.username=root
spring.datasource.password=yourpassword

3️⃣ Build and run the project:

mvn clean install
mvn spring-boot:run

4️⃣ Access the application at 👉 http://localhost:8080/api/**

📜 API Documentation

API documentation can be accessed via Swagger UI:

http://localhost:8080/documentation
