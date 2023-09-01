from pydantic import BaseModel

class Message(BaseModel):
    question: str
    
class Response:
    question : str = ""
    probability : float = 0
    
    def __init__(self, question:str, probability:float):
        self.question = question
        self.probability = probability
