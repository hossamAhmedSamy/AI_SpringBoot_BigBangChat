# BigBangChat

BigBangChat is a Spring Boot web application that provides an intelligent chat interface powered by multiple AI models working together. The application compares responses from different AI models (DeepSeek and Gemini) and uses a third model to select or combine the best responses.

![WhatsApp Image 2025-04-12 at 02 03 17_97e81951](https://github.com/user-attachments/assets/ed0343ce-aff7-463d-8e41-453d1ed678ba)


## Features

- **Multi-AI Architecture**: Leverages multiple AI models for higher quality responses
- **Response Comparison**: Uses DeepSeek and Gemini models to generate responses
- **Response Selection**: Uses a third AI model to select the best response or combine them
- **Real-time Chat Interface**: Modern web interface with real-time response streaming
- **User Authentication**: Support for user registration and login
- **Conversation Management**: Save and browse multiple conversations
- **Debug Mode**: Built-in debugging features to inspect AI responses

## System Architecture

### Backend

- **Spring Boot**: Java-based backend framework
- **JPA/Hibernate**: Database ORM for entity management
- **REST API**: RESTful endpoints for chat functionality
- **Thymeleaf**: Server-side template rendering

### AI Integration

The system integrates with three AI services:
1. **DeepSeek API**: Primary AI response generation
2. **Gemini API**: Secondary AI response generation
3. **Local Ollama**: Response selection and combination

### Database Schema

- **Users**: User accounts and authentication data
- **Conversations**: Chat sessions linked to users
- **Messages**: Individual messages within conversations
- **AIResponses**: Stores responses from different AI models

## Setup Instructions

### Prerequisites

- Java 17+
- Maven
- MySQL (or compatible database)
- Ollama (local AI for response selection)

### API Keys Required

Before running the application, you'll need:
- DeepSeek API key
- Gemini API key

### Configuration

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/BigBangChat.git
   cd BigBangChat
   ```

2. Configure `application.properties` with your database and API details:
   ```properties
   # Database Configuration
   spring.datasource.url=jdbc:mysql://localhost:3306/bigbangchat
   spring.datasource.username=YOUR_DB_USERNAME
   spring.datasource.password=YOUR_DB_PASSWORD
   spring.jpa.hibernate.ddl-auto=update
   
   # API Configuration
   deepseek.api.url=https://api.deepseek.com/v1/chat/completions
   deepseek.api.key=YOUR_DEEPSEEK_API_KEY
   
   gemini.api.url=https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent
   gemini.api.key=YOUR_GEMINI_API_KEY
   ```

3. Set up Ollama locally:
   ```
   # Install Ollama
   curl -fsSL https://ollama.com/install.sh | sh
   
   # Pull the required model
   ollama pull deepseek-r1:7b
   
   # Start the Ollama server
   ollama serve
   ```

### Running the Application

1. Build the project:
   ```
   mvn clean install
   ```

2. Run the Spring Boot application:
   ```
   mvn spring-boot:run
   ```

3. Access the application:
   ```
   http://localhost:8080
   ```

## Usage

1. Register a new account or log in
2. Start a new conversation
3. Type your message and wait for the AI response
4. Use the debug button to see detailed information about AI responses

## Advanced Features

### Response Selection Mechanism

The system uses a unique approach to AI response generation:

1. User message is sent to both DeepSeek and Gemini API
2. Both responses are stored in the database
3. A third AI model (running on Ollama) evaluates both responses
4. The best response is selected or combined from the two options
5. The final response is sent back to the user

### Asynchronous Processing

The application uses `CompletableFuture` for asynchronous processing of AI requests, allowing for:
- Parallel API calls
- Non-blocking response handling
- Improved user experience with typing indicators

## Next Steps / Roadmap

The project is under active development. Here are the planned enhancements:

### In Progress

1. **Chat Memory System**:
   - Implementing efficient chat memory mechanisms
   - Optimizing memory usage for long conversations
   - Adding conversation summarization to maintain context while reducing token consumption

2. **Authentication System Improvements**:
   - Enhancing the login functionality with security best practices
   - Completing the registration process with email verification
   - Adding role-based access control for different user types

### Future Plans

3. **Conversation Analytics**:
   - Adding metrics for conversation quality and AI performance
   - Creating dashboards for conversation insights

4. **AI Model Customization**:
   - Allowing users to choose preferred AI models
   - Adding fine-tuning options for specific use cases

5. **Mobile-Responsive UI**:
   - Enhancing the UI for better mobile experience

6. **API Access**:
   - Creating a documented API for external integrations

## Development and Extension

### Adding New AI Models

To add a new AI model:
1. Create a new API service method in `ChatServiceImpl.java`
2. Modify the `AIResponseEntity` to include the new model's response
3. Update the response selection logic to consider the new model

### Customizing the UI

The chat interface uses Thymeleaf, HTML, CSS (Tailwind), and JavaScript. To customize:
1. Modify the templates in `src/main/resources/templates/`
2. Update the styles in the Chat.html file

## License

[MIT License](LICENSE)

## Contributors

- Hossam ahmed EL eissawy

## Acknowledgments

- Spring Boot
- DeepSeek API
- Gemini API
- Ollama Project
