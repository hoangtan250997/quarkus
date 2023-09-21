
CREATE TABLE skill(
    id BIGSERIAL PRIMARY KEY,
    skill_name VARCHAR(300) NOT NULL UNIQUE,
    description VARCHAR(2000),
    status VARCHAR(255) NOT NULL
    );

CREATE TABLE agile_skills_user(
	id BIGSERIAL PRIMARY KEY,
	user_name VARCHAR,
	email VARCHAR NOT NULL UNIQUE,
	"password" VARCHAR NOT NULL,
	status VARCHAR NOT NULL,
	"role" VARCHAR NOT NULL
	);

CREATE TABLE topic(
    id BIGSERIAL PRIMARY KEY,
    topic_name VARCHAR(300) NOT NULL,
    description VARCHAR(2000),
    status VARCHAR(255) NOT NULL,
    skill_id BIGSERIAL NOT NULL CONSTRAINT fk_skill REFERENCES skill(id)
    );

CREATE TABLE department(
	id BIGSERIAL PRIMARY KEY,
	department_name VARCHAR NOT NULL,
	status VARCHAR NOT NULL
	);

CREATE TABLE team(
	id BIGSERIAL PRIMARY KEY,
	team_name VARCHAR NOT NULL UNIQUE,
	status VARCHAR,
	department_id BIGSERIAL NOT NULL CONSTRAINT fk_department REFERENCES department(id)
	);

CREATE TABLE position (
	id bigserial NOT NULL,
	closed_date timestamp NULL,
	created_date timestamp NULL,
	position_name varchar(255) NOT NULL,
	note varchar(2000) NULL,
	opened_date timestamp NULL,
	quantity int4 NOT NULL,
	status varchar(255) NOT NULL,
	team_id int8 NOT NULL,
	CONSTRAINT position_pkey PRIMARY KEY (id),
	CONSTRAINT fkqvxrh500irlffxruq1advrytn FOREIGN KEY (team_id) REFERENCES team(id)
);

CREATE TABLE required_skill (
	id bigserial NOT NULL,
	level varchar(255) NULL,
	required_skill_note varchar(2000) NULL,
	require varchar(255) NULL,
	position_id int8 NOT NULL,
	skill_id int8 NOT NULL,
	CONSTRAINT required_skill_pkey PRIMARY KEY (id),
	CONSTRAINT fk3hnu1d70vg6krrgbkqttpep2p FOREIGN KEY (position_id) REFERENCES position(id),
	CONSTRAINT fkmqe06r5dbjcvky4i9tatl1r1u FOREIGN KEY (skill_id) REFERENCES skill(id)
);

CREATE TABLE required_topic (
	id bigserial NOT NULL,
	level varchar(255) NULL,
	required_topic_note varchar(2000) NULL,
	require varchar(255) NULL,
	required_skill_id int8 NOT NULL,
	topic_id int8 NOT NULL,
	CONSTRAINT required_topic_pkey PRIMARY KEY (id),
	CONSTRAINT fk5oh5j1npi0aofu0suna3dk99d FOREIGN KEY (topic_id) REFERENCES topic(id),
	CONSTRAINT fkbdykjm7bwl011pao8iqpejesi FOREIGN KEY (required_skill_id) REFERENCES required_skill(id)
);

INSERT INTO agile_skills_user (email, user_name, "password", "role", status) VALUES('huy.nguyen@axonactive.com', 'huynguyen', '$2a$10$zaGCM.1.pJ0ms8JTl7IdHOiJLRR1VjRuwmDtE6oBynENIU1fnCDcO', 'ROLE_ADMIN', 'ACTIVE');
INSERT INTO agile_skills_user (email, user_name, "password", "role", status) VALUES('an.doan@axonactive.com', 'andoan', '$2a$10$PMgsMMr.vwEECuKuLNQJcO8XFjJQvmj/j4aFrj1.7UjDC0N.CxjcS', 'ROLE_USER', 'ACTIVE');
INSERT INTO agile_skills_user (email, user_name, "password", "role", status) VALUES('an.nguyen@axonactive.com', 'annguyen', '$2a$10$HGHGg6CM3Gs79LEbo3XBH.J2XsckQEKG1bi.CNP9/cUsIWQ3UV.Iy', 'ROLE_USER', 'ACTIVE');
INSERT INTO agile_skills_user (email, user_name, "password", "role", status) VALUES('binh.dao@axonactive.com', 'binhdao', '$2a$10$c1frwx.124FeqoEoMkeFfuw1zULyGM1QEN2jkVtiAD3BCeU.sBZD2', 'ROLE_USER', 'ACTIVE');
INSERT INTO agile_skills_user (email, user_name, "password", "role", status) VALUES('vu.hoang@axonactive.com', 'vuhoang', '$2a$10$MasZBAswLAtD0GIQT1DhtOaSpjDW0oTa.Uy.W/t5x1tqF7Tr2kqYu', 'ROLE_USER', 'ACTIVE');

--plain password: huyNguyen@2023  , base64encoded password: aHV5Tmd1eWVuQDIwMjM=
--plain password: anDoan@2023  , base64encoded password: YW5Eb2FuQDIwMjM=
--plain password: anNguyen@2023  ,  base64encoded password: YW5OZ3V5ZW5AMjAyMw==
--plain password: binhDao@2023  ,  base64encoded password: YmluaERhb0AyMDIz
--plain password: vuHoang@2023  ,  base64encoded password: dnVIb2FuZ0AyMDIz

INSERT INTO skill (description, skill_name, status) VALUES('Java is a versatile, object-oriented programming language known for its simplicity, portability, and reliability. Its "Write Once, Run Anywhere" capability allows Java programs to run on any platform with the Java Virtual Machine (JVM). With an extensive standard library and third-party frameworks, Java is widely used in web development, enterprise software, Android app development, and more. Its stability, backward compatibility, and strong community support have sustained its relevance amidst newer languages, making it a top choice for building modern, scalable, and reliable applications across various industries and platforms.', 'Java', 'ACTIVE');
INSERT INTO skill (description, skill_name, status) VALUES('Python is a high-level, versatile programming language known for its simplicity and readability, making it a popular choice for beginners and experienced developers alike. With its extensive standard library and a vast ecosystem of third-party packages, Python enables rapid development across various domains, including web development, data analysis, machine learning, and automation. Its clean syntax and dynamic nature promote efficient and expressive coding, making Python a powerful tool for building robust and scalable applications.', 'Python', 'ACTIVE');
INSERT INTO skill (description, skill_name, status) VALUES('', 'C++', 'ACTIVE');
INSERT INTO skill (description, skill_name, status) VALUES(null, 'C#', 'ACTIVE');
INSERT INTO skill (description, skill_name, status) VALUES('Assembly language is a low-level programming language that provides a direct interface with a computers hardware architecture. It consists of mnemonics and instructions that correspond to specific machine operations, allowing programmers to write code that directly manipulates registers, memory, and I/O devices. Assembly language is used when precise control over hardware resources is required, often in the development of operating systems, device drivers, and embedded systems.', 'INACTIVE1689682070456_Assembly', 'INACTIVE');
INSERT INTO skill (description, skill_name, status) VALUES(null, 'INACTIVE1689682076679_Holy C', 'INACTIVE');
INSERT INTO skill (description, skill_name, status) VALUES('JavaScript is a widely used programming language that was initially developed for web development. It enables interactive and dynamic elements on websites, making it an essential part of modern web applications. JavaScript allows developers to add functionality to web pages by manipulating the Document Object Model (DOM) and responding to user interactions. It supports event-driven programming and provides features for handling asynchronous operations, such as making HTTP requests. In addition to web development, JavaScript has expanded its reach to other domains like server-side programming (Node.js), desktop application development (Electron), and mobile app development (React Native). Its versatility, coupled with its extensive library ecosystem, makes JavaScript a powerful tool for creating interactive and engaging web experiences across various platforms and devices.', 'JavaScipt', 'ACTIVE');
INSERT INTO skill (description, skill_name, status) VALUES('TypeScript is a superset of JavaScript that adds static typing and additional features to the language. It was developed by Microsoft to enhance JavaScripts productivity and maintainability, particularly in larger-scale applications. TypeScript introduces a type system that allows developers to explicitly declare types for variables, functions, and objects. This enables early error detection, improved code documentation, and better tooling support, as well as enhanced collaboration in team environments. TypeScript code is transpiled into plain JavaScript, which means it can run on any JavaScript runtime or browser. It provides advanced features like classes, interfaces, modules, and generics, empowering developers to write more structured and scalable code. TypeScript is widely adopted in modern web development, especially in projects utilizing frameworks like Angular, React, or Vue.js. Its combination of type safety and compatibility with existing JavaScript codebases makes it a valuable language for building robust and maintainable applications.', 'TypeScript', 'ACTIVE');
INSERT INTO skill (description, skill_name, status) VALUES('Ruby is a dynamic, object-oriented programming language known for its simplicity and elegance. It was designed to prioritize developer happiness and productivity. Ruby clean and readable syntax resembles natural language, making it easy to understand and write. It follows the principle of "least surprise," providing intuitive behavior and minimizing unexpected results. Ruby promotes object-oriented programming and encourages modular and reusable code. It has a robust standard library and a vibrant ecosystem of third-party libraries and gems. Ruby is commonly used for web development with frameworks like Ruby on Rails, but it is also suitable for scripting, automation, and other domains. With its focus on developer happiness and its supportive community, Ruby continues to be a popular choice for creating elegant and expressive applications.', 'INACTIVE1689682070789_Ruby', 'INACTIVE');
INSERT INTO skill (description, skill_name, status) VALUES('Node.js is a powerful and versatile open-source JavaScript runtime built on Chrome V8 engine. It allows developers to execute server-side code with efficiency and scalability. With its event-driven, non-blocking I/O model, Node.js is ideal for building real-time applications, web servers, and APIs. Its lightweight nature facilitates easy deployment, making it a popular choice for microservices architecture and cloud computing. Node.js leverages the npm package manager, offering access to a vast array of modules, enabling rapid development and reducing time-to-market. Its cross-platform compatibility empowers developers to build server-side applications for a wide range of platforms effortlessly.', 'Node.js', 'ACTIVE');
INSERT INTO skill (description, skill_name, status) VALUES('A strong understanding of various testing methodologies, including manual and automated testing, is crucial. Proficiency in creating and executing test plans, test cases, and test scripts to identify bugs, defects, and inconsistencies is essential to ensure the quality and reliability of software products.', 'Testing Expertise', 'ACTIVE');
INSERT INTO skill (description, skill_name, status) VALUES('QA Engineers need an exceptional eye for detail to identify even the smallest issues that might impact the user experience. Being able to meticulously analyze software components, track defects, and ensure that all aspects of the application align with design specifications is essential for maintaining high-quality standards.', 'Attention to Detail', 'ACTIVE');
INSERT INTO skill (description, skill_name, status) VALUES('Proficiency in containerization technologies like Docker and orchestration tools like Kubernetes to automate deployment, scaling, and management of containerized applications.', 'Containerization and Orchestration', 'ACTIVE');
INSERT INTO skill (description, skill_name, status) VALUES('Expertise in building and optimizing CI/CD pipelines using tools like Jenkins, GitLab CI/CD, or CircleCI to automate code integration, testing, and deployment processes.', 'Continuous Integration and Continuous Deployment (CI/CD)', 'ACTIVE');


INSERT INTO topic (description, topic_name, status, skill_id) VALUES('Object-Oriented Programming (OOP) is a programming paradigm based on the concept of "objects," representing real-world entities with data and behavior. OOP principles include encapsulation, abstraction, inheritance, and polymorphism. Encapsulation hides object details, abstraction focuses on essential features, inheritance promotes code reuse, and polymorphism enables flexibility. OOP enhances code organization, readability, and maintainability by modeling software systems as interconnected objects. It is widely used in languages like Java, C++, and Python, facilitating the development of robust and efficient applications across various domains.', 'OOP', 'ACTIVE', 1);
INSERT INTO topic (description, topic_name, status, skill_id) VALUES('Java 8, released in 2014, introduced significant enhancements to the language. Its most notable addition was the inclusion of functional programming features, like lambda expressions and functional interfaces. These streamlined code and enabled developers to write concise, expressive, and more readable programs. Java 8 also introduced the powerful Stream API, allowing efficient data processing through functional-style operations. Another crucial update was the Optional class, which reduced the risk of null pointer exceptions. With these advancements, Java 8 became a major milestone, promoting modern development practices and making Java a more competitive and versatile language in the programming landscape.', 'Java 8', 'ACTIVE', 1);
INSERT INTO topic (description, topic_name, status, skill_id) VALUES('Unit testing is a software testing technique where individual units or components of a program are tested in isolation to ensure they work as expected. It involves writing small test cases to validate the correctness of each units behavior, often using frameworks like JUnit for Java. Unit tests verify that functions, methods, or classes produce the desired outputs for a given set of inputs, making it easier to identify and fix bugs early in the development process. By providing rapid feedback on code changes and ensuring code reliability, unit tests play a crucial role in maintaining code quality and facilitating continuous integration practices.', 'Data Structures', 'ACTIVE', 1);
INSERT INTO topic (description, topic_name, status, skill_id) VALUES('Exception handling is a programming concept that enables developers to gracefully manage and respond to unexpected errors or exceptional situations that may occur during program execution. By using try-catch blocks, programmers can identify and handle these exceptions, preventing application crashes and providing meaningful error messages to users or logs for debugging. Java and many other programming languages support exception handling, allowing developers to maintain code stability and reliability, making applications more robust and resilient to unexpected issues that may arise during runtime.', 'Exception handling', 'ACTIVE', 1);

INSERT INTO topic (description, topic_name, status, skill_id) VALUES(' Introduction to Python syntax, variables, data types, and basic operations.', 'Basic Syntax and Data Types', 'ACTIVE', 2);
INSERT INTO topic (description, topic_name, status, skill_id) VALUES('Understanding conditional statements (if, else, elif), loops (for, while), and branching in Python.', 'Control Flow', 'ACTIVE', 2);
INSERT INTO topic (description, topic_name, status, skill_id) VALUES('Exploring function definition, arguments, return values, and working with modules in Python.', 'Functions and Modules', 'ACTIVE', 2);
INSERT INTO topic (description, topic_name, status, skill_id) VALUES(' Reading from and writing to files, manipulating file objects, and handling exceptions.', 'File Handling', 'ACTIVE', 2);
INSERT INTO topic (description, topic_name, status, skill_id) VALUES('', 'Data Structures', 'ACTIVE', 2);
INSERT INTO topic (description, topic_name, status, skill_id) VALUES(null, 'Error Handling and Exceptions', 'ACTIVE', 2);
INSERT INTO topic (description, topic_name, status, skill_id) VALUES('Introduction to popular Python libraries and packages, such as NumPy, Pandas, Matplotlib, and more.', 'INACTIVE1689732701202_Libraries and Packages', 'INACTIVE', 2);
INSERT INTO topic (description, topic_name, status, skill_id) VALUES('Exploring frameworks like Flask and Django, handling HTTP requests, and building web applications.', 'Web Development with Python', 'ACTIVE', 2);
INSERT INTO topic (description, topic_name, status, skill_id) VALUES(' Working with databases using Python, including connecting to databases, executing SQL queries, and handling data retrieval and manipulation.', 'Database Connectivity', 'ACTIVE', 2);
INSERT INTO topic (description, topic_name, status, skill_id) VALUES('Understanding the importance of testing in software development, writing unit tests using frameworks like pytest, and debugging techniques in Python to identify and fix errors in code.', 'Testing and Debugging', 'ACTIVE', 2);
INSERT INTO topic (description, topic_name, status, skill_id) VALUES('Exploring ways to achieve concurrency and parallelism in Python, such as using threads, processes, and asynchronous programming with libraries like asyncio.', 'Concurrency and Parallelism', 'ACTIVE', 2);
INSERT INTO topic (description, topic_name, status, skill_id) VALUES(' Learning how to extract data from websites using Python libraries like BeautifulSoup and Selenium, and automating repetitive tasks and interactions with web applications.', 'Web Scraping and Automation', 'ACTIVE', 2);

INSERT INTO topic (description, topic_name, status, skill_id) VALUES('Error handling is a crucial aspect of software development that involves managing and responding to unexpected issues or exceptions that can occur during the execution of a program. When errors arise, whether due to invalid input, network failures, or programming mistakes, error handling techniques come into play to ensure the application handles these situations gracefully. In JavaScript, try-catch blocks are commonly employed to encapsulate code that may potentially generate errors. The try block contains the code where errors may occur, while the catch block allows for capturing and handling specific types of errors. Effective error handling involves providing informative error messages to users, logging errors for debugging and analysis, and implementing appropriate strategies for error recovery, such as retrying failed operations or falling back to alternative approaches. It is important to strike a balance between providing detailed error information for troubleshooting and maintaining security by not exposing sensitive data. Robust error handling practices contribute to the stability, reliability, and usability of software systems, ensuring that errors are properly managed and users are provided with a smooth experience even in the face of unexpected situations.', 'Error handling', 'ACTIVE', 7);
INSERT INTO topic (description, topic_name, status, skill_id) VALUES('Variables and data types are fundamental concepts in programming. In JavaScript, variables serve as containers for storing values that can be accessed and manipulated throughout the program. Each variable has a specific data type that determines the kind of value it can hold, such as numbers, strings, booleans, arrays, or objects. Understanding variables and data types is crucial for performing operations and making decisions based on the data being processed. It allows developers to allocate memory, assign values, and perform calculations or transformations. Having a clear understanding of variables and data types is essential for writing efficient and meaningful JavaScript code.', 'Variables and data types', 'ACTIVE', 7);
INSERT INTO topic (description, topic_name, status, skill_id) VALUES('Operators and expressions are fundamental aspects of programming languages like JavaScript. Operators perform specific actions on data, such as arithmetic calculations or logical evaluations, while expressions combine operators, variables, and constants to generate new values. JavaScript provides a wide range of operators, including arithmetic, assignment, comparison, and logical operators. Expressions, formed by combining these operators with variables and constants, are used to perform calculations, make decisions, and control program flow. Understanding operators and expressions is essential for effective programming in JavaScript, enabling developers to manipulate data, perform computations, and create dynamic and functional applications.', 'Operators and expressions', 'ACTIVE', 7);

INSERT INTO topic (description, topic_name, status, skill_id) VALUES('Error handling in Node.js is a critical practice that deals with managing unforeseen issues during code execution. By using try-catch blocks, developers can gracefully handle exceptions and prevent application crashes. Node.js offers built-in Error objects, while custom errors can be created to categorize specific error scenarios effectively. Asynchronous code is handled using Promises and async/await, simplifying error management. Logging and error monitoring aid in quickly identifying and resolving issues in production. Effective error handling ensures Node.js applications remain stable, reliable, and user-friendly, ultimately improving the overall performance and user experience of the application.', 'Error Handling', 'ACTIVE', 10);
INSERT INTO topic (description, topic_name, status, skill_id) VALUES('Serverless architecture in Node.js is a paradigm where developers focus on writing code without managing server infrastructure. It leverages cloud services like AWS Lambda, Azure Functions, or Google Cloud Functions to execute functions on-demand in response to events. With Node.js being well-suited for event-driven, lightweight tasks, it allows seamless development and scaling of individual functions. Developers only pay for actual resource consumption, reducing operational overhead. Serverless in Node.js enables rapid development, cost efficiency, and automatic scaling, making it an ideal choice for event-driven applications, APIs, and microservices.', 'Serverless architecture ', 'ACTIVE', 10);
INSERT INTO topic (description, topic_name, status, skill_id) VALUES('Testing and debugging in Node.js are crucial processes to ensure code quality and identify and rectify issues. Testing involves writing and running test cases using tools like Jest or Mocha to verify the correctness of functions and modules. Unit, integration, and end-to-end testing help catch bugs early and maintain robust code. Node.js built-in debugging tools like the Node Inspector or the Chrome Developer Tools enable developers to inspect code execution, set breakpoints, and analyze variables in real-time. Proper testing and debugging practices in Node.js lead to more reliable, maintainable code, reducing downtime and improving overall application performance.', 'Testing and Debugging', 'ACTIVE', 10);
INSERT INTO topic (description, topic_name, status, skill_id) VALUES('Creating a web server in Node.js involves using its built-in HTTP module to handle incoming HTTP requests and serve responses. By utilizing simple and efficient code, developers can quickly set up a server. With Node.js event-driven, non-blocking architecture, the server can handle concurrent requests efficiently. Developers define request handlers to process incoming requests and generate appropriate responses. Additional frameworks like Express.js offer higher-level abstractions for routing and middleware, streamlining the server creation process. Node.js ability to handle large numbers of simultaneous connections makes it an excellent choice for scalable web applications and APIs.', 'Creating web server', 'ACTIVE', 10);
INSERT INTO topic (description, topic_name, status, skill_id) VALUES('Integrating frontend technologies like React or Angular with Node.js involves creating a powerful and cohesive full-stack web application. Node.js serves as the backend, handling server-side logic, data processing, and API interactions. Meanwhile, React or Angular operates on the client-side, managing the user interface and user interactions. This combination allows for a seamless flow of data and real-time updates between the frontend and backend. Node.js facilitates API development, providing endpoints for frontend components to interact with the server. This integration fosters a more efficient and scalable development process, enabling developers to build modern, feature-rich web applications with a smooth user experience.', 'Integrating Frontend Technologies (e.g., React, Angular)', 'ACTIVE', 10);
INSERT INTO topic (description, topic_name, status, skill_id) VALUES('', 'Performance Optimization', 'ACTIVE', 10);

INSERT INTO topic (description, topic_name, status, skill_id) VALUES('Familiarity with automation tools and frameworks like Selenium, Appium, or JUnit. Knowledge of scripting languages (e.g., Java, Python) to develop automated test scripts that enhance testing efficiency and repeatability.', 'Test Automation Frameworks', 'ACTIVE', 11);

INSERT INTO topic (description, topic_name, status, skill_id) VALUES('Understanding the concepts of containers, images, Dockerfile, and Docker Hub. Learning how to build, manage, and distribute containerized applications using Docker.', 'Docker Fundamentals', 'ACTIVE', 13);
INSERT INTO topic (description, topic_name, status, skill_id) VALUES('Exploring the components of Kubernetes, including pods, services, deployments, and namespaces. Understanding how Kubernetes orchestrates containers and provides scaling and management capabilities.', 'Kubernetes Architecture', 'ACTIVE', 13);
INSERT INTO topic (description, topic_name, status, skill_id) VALUES('Exploring tools like Prometheus and Grafana to monitor the health and performance of containers and clusters. Integrating logging solutions like Fluentd and Elasticsearch for centralized log aggregation.', 'Monitoring and Logging', 'ACTIVE', 13);

INSERT INTO topic (description, topic_name, status, skill_id) VALUES('Integrating version control systems like Git with CI/CD pipelines to automate code integration, testing, and deployment processes.', 'Version Control Integration', 'ACTIVE', 14);
INSERT INTO topic (description, topic_name, status, skill_id) VALUES('Implementing automated testing practices, including unit tests, integration tests, and end-to-end tests, to ensure code quality and prevent regressions.', 'Automated Testing', 'ACTIVE', 14);
INSERT INTO topic (description, topic_name, status, skill_id) VALUES('Planning and orchestrating the release of new versions or updates, including rollback procedures and versioning strategies.', 'Release Management', 'ACTIVE', 14);

INSERT INTO department (department_name, status) VALUES('Schwyz', 'ACTIVE');
INSERT INTO department (department_name, status) VALUES('Glarus', 'ACTIVE');
INSERT INTO department (department_name, status) VALUES('Genève', 'ACTIVE');
INSERT INTO department (department_name, status) VALUES('INACTIVE1689682076679_Wallis', 'INACTIVE');
INSERT INTO department (department_name, status) VALUES('Bern', 'ACTIVE');
INSERT INTO department (department_name, status) VALUES('Basel', 'ACTIVE');
INSERT INTO department (department_name, status) VALUES('Thurgau', 'ACTIVE');
INSERT INTO department (department_name, status) VALUES('Lucerne', 'ACTIVE');
INSERT INTO department (department_name, status) VALUES('Fribourg', 'ACTIVE');

INSERT INTO team (team_name, status, department_id) VALUES('Wow', 'ACTIVE', 1);
INSERT INTO team (team_name, status, department_id) VALUES('Next', 'ACTIVE', 1);
INSERT INTO team (team_name, status, department_id) VALUES('Pixels', 'ACTIVE', 1);
INSERT INTO team (team_name, status, department_id) VALUES('Alibaba', 'ACTIVE', 2);
INSERT INTO team (team_name, status, department_id) VALUES('Dilbert', 'ACTIVE', 2);
INSERT INTO team (team_name, status, department_id) VALUES('Doremon', 'ACTIVE', 2);
INSERT INTO team (team_name, status, department_id) VALUES('Halogen', 'ACTIVE', 3);
INSERT INTO team (team_name, status, department_id) VALUES('Jaguar', 'ACTIVE', 3);
INSERT INTO team (team_name, status, department_id) VALUES('Slash', 'ACTIVE', 3);
INSERT INTO team (team_name, status, department_id) VALUES('INACTIVE1689682076676_Oracle', 'INACTIVE', 4);
INSERT INTO team (team_name, status, department_id) VALUES('INACTIVE1689682076677_Challenge', 'INACTIVE', 4);
INSERT INTO team (team_name, status, department_id) VALUES('Kangaroo', 'ACTIVE', 5);
INSERT INTO team (team_name, status, department_id) VALUES('Dolphin', 'ACTIVE', 5);
INSERT INTO team (team_name, status, department_id) VALUES('Dodo', 'ACTIVE', 5);
INSERT INTO team (team_name, status, department_id) VALUES('Anonymous', 'ACTIVE', 6);
INSERT INTO team (team_name, status, department_id) VALUES('Fusion', 'ACTIVE', 6);
INSERT INTO team (team_name, status, department_id) VALUES('Apollo', 'ACTIVE', 6);
INSERT INTO team (team_name, status, department_id) VALUES('Bolt', 'ACTIVE', 7);
INSERT INTO team (team_name, status, department_id) VALUES('Everest', 'ACTIVE', 7);
INSERT INTO team (team_name, status, department_id) VALUES('Bamboo', 'ACTIVE', 7);
INSERT INTO team (team_name, status, department_id) VALUES('Wave', 'ACTIVE', 8);
INSERT INTO team (team_name, status, department_id) VALUES('Knight', 'ACTIVE', 8);
INSERT INTO team (team_name, status, department_id) VALUES('Lightning', 'ACTIVE', 8);
INSERT INTO team (team_name, status, department_id) VALUES('Gravity', 'ACTIVE', 9);
INSERT INTO team (team_name, status, department_id) VALUES('Rocket', 'ACTIVE', 9);
INSERT INTO team (team_name, status, department_id) VALUES('BlueBirds', 'ACTIVE', 9);

INSERT INTO position (closed_date, created_date, position_name, note, quantity, status, team_id) VALUES('2019-08-22 11:52:31.416', '2019-04-22 11:52:31.416', 'Senior Sofware Developer', 'We are looking for an experienced Java developer with a strong understanding of object-oriented programming and Java frameworks like Spring. The ideal candidate should have at least 5 years of Java development experience and a solid understanding of database concepts and SQL. Join our dynamic team and work on challenging projects in a collaborative environment.', 2, 'CLOSE', 1);
INSERT INTO position (closed_date, created_date, position_name, note, quantity, status, team_id) VALUES('2019-04-22 12:52:31.416', '2019-02-22 9:52:31.416', 'Junior Sofware Developer', 'We are looking for an junior Java developer with understanding of object-oriented programming and Java frameworks like Spring. The ideal candidate should have at least 1 years of Java development experience and a solid understanding of database concepts and SQL. Join our dynamic team and work on challenging projects in a collaborative environment.', 3, 'CLOSE', 2);
INSERT INTO position (closed_date, created_date, position_name, note, quantity, status, team_id) VALUES('2019-07-22 8:52:31.416', '2019-05-22 9:52:31.416', 'Frontend Developer', 'Are you a talented Front-End Developer with a passion for creating captivating user interfaces using React.js? Were on the lookout for a skilled developer to join our team. As a key player, youll transform designs into seamless, responsive web applications, collaborating with cross-functional teams. If youre experienced in HTML, CSS, JavaScript (ES6+), and have a sharp eye for detail, we want to hear from you. Elevate your career in an innovative environment where your expertise will make a real impact. Ready to shape the digital future? Apply today with your portfolio and lets chat!', 2, 'CLOSE', 3);
INSERT INTO position (closed_date, created_date, position_name, note, quantity, status, team_id) VALUES('2019-03-22 20:52:31.416', '2019-01-22 8:52:31.416', 'Senior Fullstack Developer', 'We are looking for an Senior Fullstack developer with understanding of object-oriented programming and Java frameworks like Spring . The ideal candidate should have at least 5 years of Java development experience and a solid understanding of database concepts and SQL. Join our dynamic team and work on challenging projects in a collaborative environment.', 2, 'CLOSE', 3);
INSERT INTO position (closed_date, created_date, position_name, note, quantity, status, team_id) VALUES('2019-03-22 9:52:31.416', '2019-03-22 8:52:31.416', 'Technical Architect Java Developer', 'Unleash your Java expertise as a Technical Architect Java Developer at [Your Company Name]. Were seeking a visionary developer to shape the technical direction of our projects. If you possess a strong foundation in Java, along with proven experience in architecting complex systems, we want to connect with you. Collaborate with teams to design and implement high-performance solutions while staying updated on industry trends. If youre a problem solver, a team player, and excited about leading technical innovation, we encourage you to apply. Join us in crafting the next generation of cutting-edge applications. Submit your application today!', 2, 'CLOSE', 3);
INSERT INTO position (closed_date, created_date, position_name, note, quantity, status, team_id) VALUES('2019-05-22 10:52:31.416', '2019-04-22 8:52:31.416', 'QA Engineer', 'Are you a detail-oriented individual with a knack for uncovering even the subtlest of software glitches? Step into the role of QA Engineer at [Your Company Name] and play a pivotal part in delivering flawless user experiences. Were seeking a meticulous QA professional to put our products to the test, ensuring they meet the highest quality standards. If you have a passion for breaking things, a keen eye for detail, and a knack for designing comprehensive test cases, wed love to hear from you. Collaborate with cross-functional teams and contribute to our commitment to excellence. Ready to make your mark? Apply now and help us shape impeccable software solutions!', 2, 'CLOSE', 3);
INSERT INTO position (closed_date, created_date, position_name, note, quantity, status, team_id) VALUES('2019-06-22 11:52:31.416', '2019-04-22 8:52:31.416', 'Python Developer', 'We have filled the Python Developer position with a talented individual who will be responsible for developing and maintaining Python-based web applications. The candidate should have experience with Python frameworks like Django or Flask and a good understanding of RESTful API design principles. The successful candidate will work closely with our cross-functional team to deliver high-quality software solutions.', 1, 'CLOSE', 4);
INSERT INTO position (closed_date, created_date, position_name, note, quantity, status, team_id) VALUES('2019-06-22 11:52:31.416', '2019-04-22 8:52:31.416', 'Node.js Developer', 'Join our team as a Node.js Developer and drive forward digital solutions that redefine industries. Your expertise in building scalable, server-side applications using Node.js will be instrumental. Collaborate with cross-functional teams to architect and implement robust APIs, ensuring optimal performance and seamless functionality. If youre passionate about real-time applications, event-driven programming, and mastering asynchronous concepts, this role is for you. Elevate your career in a dynamic environment that values innovation. Ready to shape the digital landscape? Apply now and be a key player in revolutionizing our tech-driven future.', 2, 'CLOSE', 5);
INSERT INTO position (closed_date, created_date, position_name, note, quantity, status, team_id) VALUES('2019-04-22 11:52:31.416', '2019-03-22 11:52:31.416', 'Advanced Java Developer', 'Step into the role of an Advanced Java Developer and be the driving force behind cutting-edge software solutions. Your deep expertise in Java programming, including advanced frameworks and design patterns, will fuel our innovation. Collaborate seamlessly with teams to design and implement complex functionalities, ensuring high-performance applications. If youre passionate about optimizing code, solving intricate challenges, and staying ahead in the Java ecosystem, we want you on our team. Join us to shape the future of technology and be a vital contributor to our success. Ready to embark on this exciting journey? Apply today!', 4, 'CLOSE', 6);
INSERT INTO position (closed_date, created_date, position_name, note, quantity, status, team_id) VALUES('2019-05-22 11:52:31.416', '2019-08-22 11:52:31.416', 'DevOps Developer', 'Become a pivotal force as an Advanced DevOps Developer, orchestrating fluid operations and continuous delivery pipelines. Your mastery of advanced DevOps tools and practices will streamline development-to-deployment workflows. Collaborate across teams to automate provisioning, monitor performance, and ensure scalable, resilient systems. If youre passionate about bridging the gap between development and operations, optimizing CI/CD pipelines, and championing a culture of automation, this role is your platform. Join us to revolutionize how we deliver technology and be at the forefront of driving efficiency and innovation. Ready to shape the DevOps landscape? Apply now!', 3, 'CLOSE', 2);
INSERT INTO position (closed_date, created_date, position_name, note, quantity, status, team_id) VALUES('2020-08-22 11:47:52.849', '2020-07-22 13:48:17.450', 'DevOps Developer', 'Become a pivotal force as an Advanced DevOps Developer, orchestrating fluid operations and continuous delivery pipelines. Your mastery of advanced DevOps tools and practices will streamline development-to-deployment workflows. Collaborate across teams to automate provisioning, monitor performance, and ensure scalable, resilient systems. If youre passionate about bridging the gap between development and operations, optimizing CI/CD pipelines, and championing a culture of automation, this role is your platform. Join us to revolutionize how we deliver technology and be at the forefront of driving efficiency and innovation. Ready to shape the DevOps landscape? Apply now!', 3, 'CLOSE', 4);
INSERT INTO position (closed_date, created_date, position_name, note, quantity, status, team_id) VALUES('2020-04-22 11:47:52.849', '2020-03-22 11:47:52.849', 'Node.js Developer', 'Join our team as a Node.js Developer and drive forward digital solutions that redefine industries. Your expertise in building scalable, server-side applications using Node.js will be instrumental. Collaborate with cross-functional teams to architect and implement robust APIs, ensuring optimal performance and seamless functionality. If youre passionate about real-time applications, event-driven programming, and mastering asynchronous concepts, this role is for you. Elevate your career in a dynamic environment that values innovation. Ready to shape the digital landscape? Apply now and be a key player in revolutionizing our tech-driven future.', 2, 'CLOSE', 3);
INSERT INTO position (closed_date, created_date, position_name, note, quantity, status, team_id) VALUES('2020-06-22 11:47:52.849', '2020-05-22 11:47:52.849', 'Node.js Developer', 'Join our team as a Node.js Developer and drive forward digital solutions that redefine industries. Your expertise in building scalable, server-side applications using Node.js will be instrumental. Collaborate with cross-functional teams to architect and implement robust APIs, ensuring optimal performance and seamless functionality. If youre passionate about real-time applications, event-driven programming, and mastering asynchronous concepts, this role is for you. Elevate your career in a dynamic environment that values innovation. Ready to shape the digital landscape? Apply now and be a key player in revolutionizing our tech-driven future.', 2, 'CLOSE', 1);
INSERT INTO position (closed_date, created_date, position_name, note, quantity, status, team_id) VALUES('2020-05-22 11:47:52.849', '2020-03-22 11:47:52.849', 'Advanced Java Developer', 'Step into the role of an Advanced Java Developer and be the driving force behind cutting-edge software solutions. Your deep expertise in Java programming, including advanced frameworks and design patterns, will fuel our innovation. Collaborate seamlessly with teams to design and implement complex functionalities, ensuring high-performance applications. If youre passionate about optimizing code, solving intricate challenges, and staying ahead in the Java ecosystem, we want you on our team. Join us to shape the future of technology and be a vital contributor to our success. Ready to embark on this exciting journey? Apply today!', 4, 'CLOSE', 7);
INSERT INTO position (closed_date, created_date, position_name, note, quantity, status, team_id) VALUES('2020-02-22 11:47:52.849', '2020-01-22 11:47:52.849', 'Advanced Java Developer', 'Step into the role of an Advanced Java Developer and be the driving force behind cutting-edge software solutions. Your deep expertise in Java programming, including advanced frameworks and design patterns, will fuel our innovation. Collaborate seamlessly with teams to design and implement complex functionalities, ensuring high-performance applications. If youre passionate about optimizing code, solving intricate challenges, and staying ahead in the Java ecosystem, we want you on our team. Join us to shape the future of technology and be a vital contributor to our success. Ready to embark on this exciting journey? Apply today!', 4, 'CLOSE', 4);
INSERT INTO position (closed_date, created_date, position_name, note, quantity, status, team_id) VALUES('2020-09-22 11:47:52.849', '2020-08-22 11:47:52.849', 'Frontend Developer', 'We are looking for an Frontend developer with understanding of . The ideal candidate should have at least 1 years of Java development experience and a solid understanding of database concepts and SQL. Join our dynamic team and work on challenging projects in a collaborative environment.', 3, 'CLOSE', 1);
INSERT INTO position (closed_date, created_date, position_name, note, quantity, status, team_id) VALUES('2020-09-22 15:47:52.849', '2020-09-22 14:47:52.849', 'Frontend Developer', 'We are looking for an Frontend developer with understanding of . The ideal candidate should have at least 1 years of Java development experience and a solid understanding of database concepts and SQL. Join our dynamic team and work on challenging projects in a collaborative environment.', 4, 'CLOSE', 4);
INSERT INTO position (closed_date, created_date, position_name, note, quantity, status, team_id) VALUES('2020-10-22 15:47:52.849', '2020-08-22 15:47:52.849', 'Technical Architect Java Developer', 'Unleash your Java expertise as a Technical Architect Java Developer at [Your Company Name]. Were seeking a visionary developer to shape the technical direction of our projects. If you possess a strong foundation in Java, along with proven experience in architecting complex systems, we want to connect with you. Collaborate with teams to design and implement high-performance solutions while staying updated on industry trends. If youre a problem solver, a team player, and excited about leading technical innovation, we encourage you to apply. Join us in crafting the next generation of cutting-edge applications. Submit your application today!', 2, 'CLOSE', 4);
INSERT INTO position (closed_date, created_date, position_name, note, quantity, status, team_id) VALUES('2020-06-22 15:47:52.849', '2020-05-22 15:47:52.849', 'QA Engineer', 'Are you a detail-oriented individual with a knack for uncovering even the subtlest of software glitches? Step into the role of QA Engineer at [Your Company Name] and play a pivotal part in delivering flawless user experiences. Were seeking a meticulous QA professional to put our products to the test, ensuring they meet the highest quality standards. If you have a passion for breaking things, a keen eye for detail, and a knack for designing comprehensive test cases, wed love to hear from you. Collaborate with cross-functional teams and contribute to our commitment to excellence. Ready to make your mark? Apply now and help us shape impeccable software solutions!', 2, 'CLOSE', 6);
INSERT INTO position (closed_date, created_date, position_name, note, quantity, status, team_id) VALUES('2020-12-22 15:47:52.849', '2020-10-22 15:47:52.849', 'Advanced Java Developer', 'Step into the role of an Advanced Java Developer and be the driving force behind cutting-edge software solutions. Your deep expertise in Java programming, including advanced frameworks and design patterns, will fuel our innovation. Collaborate seamlessly with teams to design and implement complex functionalities, ensuring high-performance applications. If youre passionate about optimizing code, solving intricate challenges, and staying ahead in the Java ecosystem, we want you on our team. Join us to shape the future of technology and be a vital contributor to our success. Ready to embark on this exciting journey? Apply today!', 4, 'CLOSE', 1);
INSERT INTO position (closed_date, created_date, position_name, note, quantity, status, team_id) VALUES('2020-11-22 15:47:52.849', '2020-09-22 15:47:52.849', 'Senior Sofware Developer', 'We are looking for an experienced Java developer with a strong understanding of object-oriented programming and Java frameworks like Spring. The ideal candidate should have at least 5 years of Java development experience and a solid understanding of database concepts and SQL. Join our dynamic team and work on challenging projects in a collaborative environment.', 2, 'CLOSE', 1);

INSERT INTO public.position (closed_date,created_date,position_name,note,quantity,status,team_id) VALUES ('2022-12-20 22:27:38.424','2022-09-06 07:43:49.679','Software Tester','Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry''s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.',2,'CLOSE',3);
INSERT INTO public.position (closed_date,created_date,position_name,note,quantity,status,team_id) VALUES ('2022-11-01 04:27:40.261','2022-10-10 12:28:45.704','Programmer Analyst','Lorem Ipsum chỉ đơn giản là một đoạn văn bản giả, được dùng vào việc trình bày và dàn trang phục vụ cho in ấn.
Lorem Ipsum đã được sử dụng như một văn bản chuẩn cho ngành công nghiệp in ấn từ những năm 1500, khi một họa sĩ vô danh ghép nhiều đoạn văn bản với nhau để tạo thành một bản mẫu văn bản.
Đoạn văn bản này không những đã tồn tại năm thế kỉ, mà khi được áp dụng vào tin học văn phòng, nội dung của nó vẫn không hề bị thay đổi.
Nó đã được phổ biến trong những năm 1960 nhờ việc bán những bản giấy Letraset in những đoạn Lorem Ipsum, và gần đây hơn, được sử dụng trong các ứng dụng dàn trang, như Aldus PageMaker.',5,'CLOSE',7);
INSERT INTO public.position (closed_date,created_date,position_name,note,quantity,status,team_id) VALUES ('2022-11-01 14:27:45.199','2022-05-31 19:02:23.104','Delivery Manager','Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt.',2,'CLOSE',10);
INSERT INTO public.position (closed_date,created_date,position_name,note,quantity,status,team_id) VALUES ('2022-05-06 22:47:46.96','2022-04-22 15:36:43.584','Data Scientist','Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt.',4,'CLOSE',1);
INSERT INTO public.position (closed_date,created_date,position_name,note,quantity,status,team_id) VALUES ('2022-09-25 13:45:49.313','2022-08-22 14:27:10.028','Data Scientist','Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt.',2,'CLOSE',18);
INSERT INTO public.position (closed_date,created_date,position_name,note,quantity,status,team_id) VALUES ('2022-01-31 11:59:56.753','2022-01-06 15:24:48.675','Data Scientist','Et harum quidem rerum facilis est et expedita distinctio.

Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus.

Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae.',2,'CLOSE',20);
INSERT INTO public.position (closed_date,created_date,position_name,note,quantity,status,team_id) VALUES ('2022-09-06 11:23:06.759','2022-06-06 14:20:27.852','Delivery Manager','Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt.',5,'CLOSE',1);
INSERT INTO public.position (closed_date,created_date,position_name,note,quantity,status,team_id) VALUES ('2022-08-22 22:27:31.285','2022-07-12 22:27:31.285','New Developer','Pellentesque at nulla. Suspendisse potenti. Cras in purus eu magna vulputate luctus.

Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Vivamus vestibulum sagittis sapien. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.

Etiam vel augue. Vestibulum rutrum rutrum neque. Aenean auctor gravida sem.',4,'CLOSE',9);
INSERT INTO public.position (closed_date,created_date,position_name,note,quantity,status,team_id) VALUES ('2022-02-11 14:44:34.36','2022-01-31 11:14:25.938','Python Engineer','🚀 Join Our Team as a Python Developer! 🚀

Are you ready to embark on a transformative journey in the world of software development? We are thrilled to announce an exciting opportunity for a talented Back-End Developer to become a crucial part of our innovative and dynamic team. At [Company Name], we believe that technology has the power to shape the future, and we are on a mission to create cutting-edge solutions that redefine industries and elevate user experiences.

As a Back-End Developer at [Company Name], you will play a pivotal role in crafting the backbone of our digital products and services. You''ll collaborate closely with a diverse team of forward-thinking engineers, designers, and visionaries to design, develop, and deploy robust, scalable, and efficient server-side applications. Your expertise will be vital in architecting complex systems, optimizing database performance, and ensuring seamless integration with front-end components. We''re seeking a problem solver with a passion for clean and maintainable code, who can navigate through challenges and take ownership of projects from inception to completion.

Our ideal candidate possesses a strong foundation in back-end technologies such as [Programming Languages], [Frameworks], and [Databases], coupled with a deep understanding of RESTful APIs, microservices architecture, and cloud computing platforms like [Cloud Provider]. You should have an eye for detail, an eagerness to stay up-to-date with industry trends, and the ability to communicate effectively within a collaborative environment.

In return, [Company Name] is committed to fostering a culture of continuous learning and professional growth. You''ll have access to ongoing training opportunities, mentorship from seasoned experts, and the chance to work on exciting projects that push the boundaries of innovation. Plus, enjoy a flexible work environment that values work-life balance and encourages creativity.',5,'CLOSE',2);
INSERT INTO public.position (closed_date,created_date,position_name,note,quantity,status,team_id) VALUES ('2022-10-23 19:27:36.765','2022-06-06 11:15:33.329','Python Engineer','🚀 Join Our Team as a Python Developer! 🚀

Are you ready to embark on a transformative journey in the world of software development? We are thrilled to announce an exciting opportunity for a talented Back-End Developer to become a crucial part of our innovative and dynamic team. At [Company Name], we believe that technology has the power to shape the future, and we are on a mission to create cutting-edge solutions that redefine industries and elevate user experiences.

As a Back-End Developer at [Company Name], you will play a pivotal role in crafting the backbone of our digital products and services. You''ll collaborate closely with a diverse team of forward-thinking engineers, designers, and visionaries to design, develop, and deploy robust, scalable, and efficient server-side applications. Your expertise will be vital in architecting complex systems, optimizing database performance, and ensuring seamless integration with front-end components. We''re seeking a problem solver with a passion for clean and maintainable code, who can navigate through challenges and take ownership of projects from inception to completion.

Our ideal candidate possesses a strong foundation in back-end technologies such as [Programming Languages], [Frameworks], and [Databases], coupled with a deep understanding of RESTful APIs, microservices architecture, and cloud computing platforms like [Cloud Provider]. You should have an eye for detail, an eagerness to stay up-to-date with industry trends, and the ability to communicate effectively within a collaborative environment.

In return, [Company Name] is committed to fostering a culture of continuous learning and professional growth. You''ll have access to ongoing training opportunities, mentorship from seasoned experts, and the chance to work on exciting projects that push the boundaries of innovation. Plus, enjoy a flexible work environment that values work-life balance and encourages creativity.',5,'CLOSE',3);
INSERT INTO public.position (closed_date,created_date,position_name,note,quantity,status,team_id) VALUES ('2023-05-12 11:39:17.681','2023-02-11 12:15:13.951','Python Engineer','Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?',2,'OPEN',5);
INSERT INTO public.position (closed_date,created_date,position_name,note,quantity,status,team_id) VALUES ('2023-03-08 16:08:16.985','2023-01-31 10:07:27.121','Python Engineer','Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?',5,'OPEN',5);
INSERT INTO public.position (closed_date,created_date,position_name,note,quantity,status,team_id) VALUES ('2023-07-01 16:39:19.825','2023-08-22 16:10:25.749','Python Engineer','Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?',2,'OPEN',6);
INSERT INTO public.position (closed_date,created_date,position_name,note,quantity,status,team_id) VALUES ('2023-05-06 16:39:22.106','2023-02-28 16:10:29.347','Python Engineer','Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?',2,'OPEN',7);
INSERT INTO public.position (closed_date,created_date,position_name,note,quantity,status,team_id) VALUES (NULL,'2023-07-31 16:31:14.039','Frontend Developer','We are looking for an Frontend developer with understanding of . The ideal candidate should have at least 1 years of Java development experience and a solid understanding of database concepts and SQL. Join our dynamic team and work on challenging projects in a collaborative environment.',3,'OPEN',14);
INSERT INTO public.position (closed_date,created_date,position_name,note,quantity,status,team_id) VALUES ('2023-01-05 16:39:14.117','2022-12-25 14:59:39.212','Database Administrator','Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?',2,'OPEN',20);
INSERT INTO public.position (closed_date,created_date,position_name,note,quantity,status,team_id) VALUES (NULL,'2023-06-25 11:33:18.681','Database administrator',NULL,1,'OPEN',8);
INSERT INTO public.position (closed_date,created_date,position_name,note,quantity,status,team_id) VALUES (NULL,'2023-07-13 10:12:34.171','Electrician',NULL,2,'OPEN',6);
INSERT INTO public.position (closed_date,created_date,position_name,note,quantity,status,team_id) VALUES (NULL,'2023-08-22 16:38:52.888012','Advanced Java Developer','Step into the role of an Advanced Java Developer and be the driving force behind cutting-edge software solutions. Your deep expertise in Java programming, including advanced frameworks and design patterns, will fuel our innovation. Collaborate seamlessly with teams to design and implement complex functionalities, ensuring high-performance applications. If you''re passionate about optimizing code, solving intricate challenges, and staying ahead in the Java ecosystem, we want you on our team. Join us to shape the future of technology and be a vital contributor to our success. Ready to embark on this exciting journey? Apply today!',10,'OPEN',7);
INSERT INTO public.position (closed_date,created_date,position_name,note,quantity,status,team_id) VALUES ('2023-08-22 16:39:24.944109','2023-04-15 11:36:33.016','Java Senior','Trái với quan điểm chung của số đông, Lorem Ipsum không phải chỉ là một đoạn văn bản ngẫu nhiên. Người ta tìm thấy nguồn gốc của nó từ những tác phẩm văn học la-tinh cổ điển xuất hiện từ năm 45 trước Công Nguyên, nghĩa là nó đã có khoảng hơn 2000 tuổi.

Một giáo sư của trường Hampden-Sydney College (bang Virginia - Mỹ) quan tâm tới một trong những từ la-tinh khó hiểu nhất, "consectetur"',2,'OPEN',3);


INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('MASTER','A required skill.','NICE_TO_HAVE',1,1);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','MUST_HAVE',1,2);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','NICE_TO_HAVE',2,1);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','NICE_TO_HAVE',2,7);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','NICE_TO_HAVE',3,7);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','NICE_TO_HAVE',4,1);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','NICE_TO_HAVE',4,7);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','NICE_TO_HAVE',5,1);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','NICE_TO_HAVE',6,11);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('LEARNED','A required skill.','NICE_TO_HAVE',7,2);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('MASTER','A required skill.','MUST_HAVE',8,10);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','NICE_TO_HAVE',9,1);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','MUST_HAVE',10,13);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('MASTER','A required skill.','MUST_HAVE',10,14);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','MUST_HAVE',11,13);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('MASTER','A required skill.','MUST_HAVE',11,14);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('MASTER','A required skill.','MUST_HAVE',12,10);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('MASTER','A required skill.','MUST_HAVE',13,10);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','NICE_TO_HAVE',14,1);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','NICE_TO_HAVE',15,1);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','NICE_TO_HAVE',16,1);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','NICE_TO_HAVE',16,7);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','NICE_TO_HAVE',17,1);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','NICE_TO_HAVE',17,7);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','NICE_TO_HAVE',18,1);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','NICE_TO_HAVE',18,7);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','NICE_TO_HAVE',19,11);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','NICE_TO_HAVE',20,1);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('LEARNED','Cras mi pede, malesuada in.','NICE_TO_HAVE',22,1);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('MASTER','consectetuer adipiscing eli','MUST_HAVE',22,2);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('MASTER','Ut enim ad minima veniam, quis nostrum','MUST_HAVE',23,2);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('MASTER','nisi ut aliquid ex ea commodi consequatur?','MUST_HAVE',24,2);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('MASTER','voluptatibus maiores alias consequatur','MUST_HAVE',26,3);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','Cras mi pede, malesuada in.','NICE_TO_HAVE',26,4);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('LEARNED','Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem.','NICE_TO_HAVE',26,5);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('MASTER','Cras mi pede, malesuada in.','MUST_HAVE',26,6);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('LEARNED','','NICE_TO_HAVE',27,1);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','NICE_TO_HAVE',27,7);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('MASTER','A required skill.','MUST_HAVE',27,8);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('LEARNED','','NICE_TO_HAVE',28,1);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','NICE_TO_HAVE',28,7);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('MASTER','A required skill.','MUST_HAVE',28,8);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','MUST_HAVE',29,7);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','MUST_HAVE',30,7);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES (null,'',null,31,7);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('MASTER','A required skill.','MUST_HAVE',32,1);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('MASTER','A required skill.','MUST_HAVE',32,2);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('MASTER','A required skill.','MUST_HAVE',32,3);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('MASTER','A required skill.','MUST_HAVE',32,7);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('MASTER','A required skill.','MUST_HAVE',32,8);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('MASTER','A required skill.','MUST_HAVE',32,10);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('MASTER','A required skill.','MUST_HAVE',33,2);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','NICE_TO_HAVE',33,8);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','NICE_TO_HAVE',33,10);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('MASTER','A required skill.','MUST_HAVE',34,2);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','NICE_TO_HAVE',34,8);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','NICE_TO_HAVE',34,10);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('MASTER','A required skill.','MUST_HAVE',35,2);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','NICE_TO_HAVE',35,8);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','NICE_TO_HAVE',35,10);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('MASTER','A required skill.','MUST_HAVE',36,2);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','NICE_TO_HAVE',36,8);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','NICE_TO_HAVE',36,10);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('MASTER','','MUST_HAVE',37,1);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('MASTER','','MUST_HAVE',37,3);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('MASTER','','MUST_HAVE',37,7);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','MUST_HAVE',38,1);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('USED','A required skill.','MUST_HAVE',38,7);
INSERT INTO public.required_skill (level,required_skill_note,require,position_id,skill_id) VALUES ('MASTER','A required skill.','MUST_HAVE',41,1);



INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',1,1);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','NICE_TO_HAVE',1,2);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','MUST_HAVE',2,5);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','NICE_TO_HAVE',2,6);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',3,1);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','NICE_TO_HAVE',3,2);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','MUST_HAVE',4,17);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',4,18);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',5,17);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','NICE_TO_HAVE',5,18);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',6,1);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',6,2);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',7,17);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',7,18);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',8,1);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',8,2);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','MUST_HAVE',8,3);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',8,4);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',9,26);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',10,5);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','NICE_TO_HAVE',10,6);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','MUST_HAVE',10,7);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','NICE_TO_HAVE',10,8);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','MUST_HAVE',11,20);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','NICE_TO_HAVE',11,21);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',11,22);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','MUST_HAVE',11,23);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','NICE_TO_HAVE',11,24);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',12,1);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','NICE_TO_HAVE',12,2);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','NICE_TO_HAVE',12,3);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',12,4);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',13,27);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','NICE_TO_HAVE',13,28);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',13,29);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','MUST_HAVE',14,30);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',14,31);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','MUST_HAVE',14,32);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',15,27);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','NICE_TO_HAVE',15,28);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',15,29);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','MUST_HAVE',16,30);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',16,31);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','MUST_HAVE',16,32);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','MUST_HAVE',17,20);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','NICE_TO_HAVE',17,21);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',17,22);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','MUST_HAVE',17,23);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','NICE_TO_HAVE',17,24);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','MUST_HAVE',18,20);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','NICE_TO_HAVE',18,21);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',18,22);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','MUST_HAVE',18,23);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','NICE_TO_HAVE',18,24);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',19,1);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','NICE_TO_HAVE',19,2);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','MUST_HAVE',19,3);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','NICE_TO_HAVE',19,4);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',20,1);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','NICE_TO_HAVE',20,2);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','MUST_HAVE',20,3);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','NICE_TO_HAVE',20,4);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',21,1);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','MUST_HAVE',21,2);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',22,17);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',22,18);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',23,1);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','MUST_HAVE',23,2);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',24,17);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','MUST_HAVE',24,18);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',25,1);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',25,2);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',25,3);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',25,4);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',26,17);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','MUST_HAVE',26,18);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','NICE_TO_HAVE',27,26);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',28,1);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','NICE_TO_HAVE',28,2);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','MUST_HAVE',28,3);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',28,4);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','','NICE_TO_HAVE',29,1);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','Cras mi pede, malesuada in','MUST_HAVE',29,2);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','In quis justo. Maecenas rhoncus aliquam lacus. ','MUST_HAVE',30,5);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','Morbi quis tortor id nulla ultrices aliquet.','NICE_TO_HAVE',30,6);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','','NICE_TO_HAVE',31,5);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','Cras mi pede, malesuada in','MUST_HAVE',31,6);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','','MUST_HAVE',31,7);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','Cras mi pede, malesuada in','NICE_TO_HAVE',31,8);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','Cras mi pede, malesuada in','MUST_HAVE',31,9);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','Cras mi pede, malesuada in','MUST_HAVE',31,10);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','','NICE_TO_HAVE',32,5);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','','MUST_HAVE',32,7);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','Cras mi pede, malesuada in','NICE_TO_HAVE',32,8);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','Cras mi pede, malesuada in','MUST_HAVE',32,12);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','Cras mi pede, malesuada in','MUST_HAVE',32,11);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','Itaque earum rerum hic tenetur','MUST_HAVE',32,6);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','a sapiente delectus, ut aut','MUST_HAVE',32,9);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','a sapiente delectus, ut aut','MUST_HAVE',32,10);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','Some note.','NICE_TO_HAVE',37,3);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',38,17);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',38,18);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','Some note.','NICE_TO_HAVE',40,3);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',41,17);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',41,18);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','At vero eos et accusamus.','NICE_TO_HAVE',43,17);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','At vero eos et accusamus.','NICE_TO_HAVE',43,18);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','maxime placeat facere possimus.','NICE_TO_HAVE',43,19);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','At vero eos et accusamus.','NICE_TO_HAVE',44,17);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','At vero eos et accusamus.','NICE_TO_HAVE',44,18);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','maxime placeat facere possimus.','NICE_TO_HAVE',44,19);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',52,5);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','NICE_TO_HAVE',52,6);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',52,7);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',52,8);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','MUST_HAVE',52,9);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',52,10);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',52,12);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','NICE_TO_HAVE',52,13);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',52,14);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',52,15);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',54,23);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',55,5);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','NICE_TO_HAVE',55,6);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',55,7);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',55,8);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','MUST_HAVE',55,9);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',55,10);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',55,12);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','NICE_TO_HAVE',55,13);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',55,14);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',55,15);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',57,23);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',58,5);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','NICE_TO_HAVE',58,6);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',58,7);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',58,8);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','MUST_HAVE',58,9);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',58,10);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',58,12);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','NICE_TO_HAVE',58,13);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',58,14);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',58,15);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',60,23);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',61,5);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','NICE_TO_HAVE',61,6);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',61,7);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',61,8);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','MUST_HAVE',61,9);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',61,10);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',61,12);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','NICE_TO_HAVE',61,13);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',61,14);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',61,15);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',63,23);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',67,1);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','NICE_TO_HAVE',67,2);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','MUST_HAVE',68,17);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('USED','A required topic.','NICE_TO_HAVE',68,18);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','NICE_TO_HAVE',69,1);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','NICE_TO_HAVE',69,2);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('LEARNED','A required topic.','NICE_TO_HAVE',69,3);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES ('MASTER','A required topic.','MUST_HAVE',69,4);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES (NULL,'Check Later.',NULL,37,1);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES (NULL,'Check Later.',NULL,40,1);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES (NULL,'At vero eos et accusamus.',NULL,45,17);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES (NULL,'At vero eos et accusamus.',NULL,45,18);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES (NULL,'maxime placeat facere possimus.',NULL,45,19);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES (NULL,'A required topic.',NULL,54,22);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES (NULL,'A required topic.',NULL,57,22);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES (NULL,'A required topic.',NULL,60,22);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES (NULL,'A required topic.',NULL,63,22);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES (NULL,'A required topic.',NULL,64,1);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES (NULL,'A required topic.',NULL,64,2);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES (NULL,'A required topic.',NULL,64,3);
INSERT INTO public.required_topic (level,required_topic_note,require,required_skill_id,topic_id) VALUES (NULL,'A required topic.',NULL,64,4);
