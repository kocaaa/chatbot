import fastapi
import uvicorn
from pydantic import BaseModel

class Message(BaseModel):
    question: str

host = "127.0.0.1"
port = 10046

app = fastapi.FastAPI()

@app.get("/example")
def count_faces():
    return {"response": "example"}

@app.post("/question")
async def question(message: Message):
    return message

if __name__ == "__main__":
    uvicorn.run(app, host=host, port=port)
