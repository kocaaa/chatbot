import json
import uvicorn
from models import Message, Response
from fastapi import FastAPI

import chatbot
import training

host = "127.0.0.1"
port = 10046

training.train()

intents = json.loads(open('intents.json').read())
app = FastAPI()

@app.post("/question")
async def question(message: Message):
    intents = chatbot.predict_class(message.question)
    probability = float(intents[0]["probability"])

    if probability > chatbot.bad_input():
        response = Response(intents[0]['intent'], probability)
    else:
        response = Response("bad_question", probability)
        
    return response

if __name__ == "__main__":
    uvicorn.run(app, host=host, port=port)
