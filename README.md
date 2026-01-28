# BlogAlchemy

A modern, feature-rich blogging platform built with Spring Boot and PostgreSQL. Create, manage, and publish blog posts with advanced features like post scheduling, nested comments, multi-image uploads, and full-text search.

## Features

- **Content Management**: Create, edit, and delete blog posts with rich content support
- **Post Scheduling**: Schedule posts for future publication
- **Multi-Image Upload**: Attach multiple images to posts (10MB max per image)
- **Nested Comments**: Threaded comment system with reply functionality
- **Featured Posts**: Mark important posts as featured
- **Search**: Full-text search across post titles and content
- **User Authentication**: Secure login with role-based access control
- **Draft Mode**: Save posts as drafts before publishing

## Tech Stack

- **Backend**: Java 17, Spring Boot 3.3.2, Spring Security, Spring Data JPA
- **Database**: PostgreSQL
- **Frontend**: Thymeleaf, HTML/CSS/JavaScript
- **Build Tool**: Maven

## Quick Start

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+

### Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/BlogAlchemy.git
cd BlogAlchemy/blogalchemy
```

2. Create a PostgreSQL database:
```sql
CREATE DATABASE blogalchemy;
```

3. Update database credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/blogalchemy
spring.datasource.username=your_username
spring.datasource.password=your_password
```

4. Build and run:
```bash
mvn clean install
mvn spring-boot:run
```

5. Open your browser and navigate to:
```
http://localhost:8080
```

## Usage

### Creating a Post

1. Log in to your account
2. Click "New Post"
3. Enter title and content
4. Upload images (optional)
5. Choose to publish immediately or schedule for later
6. Click "Submit"

### Managing Posts

- **Search**: Use the search bar to find posts by keywords
- **Edit**: Click the edit button on your posts to modify them
- **Delete**: Remove posts you no longer need
- **Feature**: Admins can mark posts as featured
- **Schedule**: Set a future publication date and time

### Comments

- View any post to see its comments
- Add new comments at the bottom of the post
- Reply to existing comments to create threaded discussions

## Project Structure

```
blogalchemy/
├── src/
│   ├── main/
│   │   ├── java/com/example/blogalchemy/
│   │   │   ├── config/        # Security and MVC configuration
│   │   │   ├── controller/    # Request handlers
│   │   │   ├── model/         # Entity classes
│   │   │   ├── repository/    # Data access layer
│   │   │   └── service/       # Business logic
│   │   └── resources/
│   │       ├── templates/     # Thymeleaf HTML templates
│   │       ├── static/        # CSS, JS, images
│   │       └── application.properties
│   └── test/
├── uploads/                   # Image storage directory
└── pom.xml                    # Maven configuration
```

## Configuration

### Database

Configure your database connection in `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/blogalchemy
spring.datasource.username=postgres
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
```

### File Uploads

Adjust file upload limits if needed:

```properties
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
upload.dir=./uploads
```

### Security

The application uses Spring Security with:
- BCrypt password encryption
- Role-based access control (USER, ADMIN)
- Session-based authentication
- CSRF protection

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/posts` | List all posts |
| GET | `/posts/{id}` | View single post |
| GET | `/posts/new` | New post form |
| POST | `/posts` | Create post |
| GET | `/posts/{id}/edit` | Edit post form |
| POST | `/posts/{id}` | Update post |
| GET | `/posts/{id}/delete` | Delete post |
| POST | `/posts/{postId}/comments` | Add comment |
| POST | `/posts/{postId}/comments/{parentId}/reply` | Reply to comment |

## Development

### Running Tests

```bash
mvn test
```

### Building for Production

```bash
mvn clean package
java -jar target/blogalchemy-0.0.1-SNAPSHOT.jar
```

### Development Mode

The application includes Spring Boot DevTools for automatic restart and live reload during development.

## Documentation

For more detailed information, see [PROJECT.md](PROJECT.md) which includes:
- Complete architecture overview
- Detailed data models
- Security implementation details
- Troubleshooting guide
- Future enhancement roadmap

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/YourFeature`
3. Commit your changes: `git commit -m 'Add YourFeature'`
4. Push to the branch: `git push origin feature/YourFeature`
5. Submit a pull request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Author

**Vidya Sagar Reddy Desu**

## Acknowledgments

- Spring Boot team for the excellent framework
- PostgreSQL community
- All contributors and users

---

**Version**: 0.0.1-SNAPSHOT
**Status**: Active Development
