import fastapi
import uvicorn
from pydantic import BaseModel
import json

from training import train
from chatbot import get_response,predict_class

class Message(BaseModel):
    question: str

host = "127.0.0.1"
port = 10046

intents = json.loads(open('intents.json').read())
train()

app = fastapi.FastAPI()


@app.get("/example")
def count_faces():
    return {"response": "example"}

@app.post("/question")
async def question(message: Message):
    ints = predict_class(message.question)
    res = get_response(ints, intents)
    return {"response": res}

if __name__ == "__main__":
    uvicorn.run(app, host=host, port=port)
