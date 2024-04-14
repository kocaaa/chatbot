from models import Message, Response
from fastapi import FastAPI
import uvicorn
import json
import os

import chatbot
import excel
import pdf

app_env = os.environ.get('APP_ENV', 'dev')
host = "0.0.0.0" if app_env == "pro" else "127.0.0.1"
port = 10046

intents = json.loads(open('intents.json', encoding="utf8").read())
app = FastAPI()

@app.post("/question")
async def question(message: Message):
    intents = chatbot.predict_class(message.question.lower())
    probability = float(intents[0]["probability"])

    if probability > chatbot.bad_input():
        response = Response(intents[0]['intent'], probability)
    else:
        response = Response("bad_question", probability)
        
    return response

@app.get("/all_subjects")
async def all_subjects():
    return excel.get_all_subjects()

@app.get("/all_exam_schedules")
async def schedule():
    return pdf.get_exam_schedule_hierarchy()

if __name__ == "__main__":
    uvicorn.run(app, host=host, port=port)
