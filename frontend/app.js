class Chatbot {
    constructor() {
        this.args = {
            openButton: document.querySelector('.chatbot__button'),
            chatBot: document.querySelector('.chatbot__support'),
            sendButton: document.querySelector('.send__button'),
        }

        this.state = false;
        this.messages = [];
    }

    display() {
        const { openButton, chatBot, sendButton } = this.args;

        openButton.addEventListener('click', () => this.toggleState(chatBot))
        sendButton.addEventListener('click', () => this.onSendButton(chatBot))

        const node = chatBot.querySelector('input');
        node.addEventListener("keyup", ({ key }) => {
            if (key === "Enter") {
                this.onSendButton(chatBot)
            }
        })
    }

    toggleState(chatbot) {
        this.state = !this.state;

        if (this.state) {
            chatbot.classList.add('chatbot--active')
        } else {
            chatbot.classList.remove('chatbot--active')
        }
    }

    onSendButton(chatbot) {
        var textField = chatbot.querySelector('input');
        let text1 = textField.value
        if (text1 === "") {
            return;
        }

        let msg1 = { name: "User", message: text1 }
        this.messages.push(msg1);

        fetch('http://localhost:8080/chatbot/getMessage', {
            method: 'POST',
            body: JSON.stringify({ question: text1 }),
            mode: 'cors',
            headers: {
                'Content-Type': 'application/json'
            },
        })
            .then(r => r.json())
            .then(r => {
                let msg2 = { name: "Chatbot", message: r.question };
                this.messages.push(msg2);
                this.updateChatText(chatbot)
                textField.value = ''

            }).catch((error) => {
                console.error('Error:', error);
                this.updateChatText(chatbot)
                textField.value = ''
            });
    }

    updateChatText(chatbot) {
        var html = '';
        this.messages.slice().reverse().forEach(function (item, index) {
            if (item.name === "Chatbot") {
                html += '<div class="messages__item messages__item--visitor">' + item.message + '</div>'
            }
            else {
                html += '<div class="messages__item messages__item--operator">' + item.message + '</div>'
            }
        });

        const chatmessage = chatbot.querySelector('.chatbot__messages');
        chatmessage.innerHTML = html;
    }
}


const chatbot = new Chatbot();
chatbot.display();
