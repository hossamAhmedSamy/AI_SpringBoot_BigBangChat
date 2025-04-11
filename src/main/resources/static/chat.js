document.addEventListener('DOMContentLoaded', () => {
    const conversationId = window.location.pathname.split('/').pop();
    const chatMessages = document.getElementById('chat-messages');
    const userMessageInput = document.getElementById('user-message');
    const sendMessageButton = document.getElementById('send-message');

    // Function to load conversation messages
    async function loadMessages() {
        try {
            const response = await fetch(`/api/chat/${conversationId}`);
            const messages = await response.json();

            chatMessages.innerHTML = ''; // Clear existing messages
            messages.forEach(message => {
                const messageElement = document.createElement('div');
                messageElement.classList.add('message');
                messageElement.classList.add(
                    message.role === 'USER' ? 'user-message' : 'bot-message'
                );
                messageElement.textContent = message.text;
                chatMessages.appendChild(messageElement);
            });

            // Scroll to bottom
            chatMessages.scrollTop = chatMessages.scrollHeight;
        } catch (error) {
            console.error('Error loading messages:', error);
        }
    }

    // Function to send a message
    async function sendMessage() {
        const messageText = userMessageInput.value.trim();
        if (!messageText) return;

        try {
            await fetch('/api/chat/send', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `conversationId=${conversationId}&text=${encodeURIComponent(messageText)}`
            });

            userMessageInput.value = ''; // Clear input
            await loadMessages(); // Reload messages
        } catch (error) {
            console.error('Error sending message:', error);
        }
    }

    // Event listeners
    sendMessageButton.addEventListener('click', sendMessage);
    userMessageInput.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
            sendMessage();
        }
    });

    // Initial load of messages
    loadMessages();

    // Optional: Periodic message refresh
    setInterval(loadMessages, 5000);
});