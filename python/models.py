from pydantic import BaseModel

class Message(BaseModel):
    question: str
    
class Response:
    question : str = ""
    probability : float = 0
    
    def __init__(self, question:str, probability:float):
        self.question = question
        self.probability = probability

class Class:
    name : str = None
    initials : str = None
    professor : str = None
    assistants : set = None
    
    def __init__(self, name:str, professor:str):
        self.name = name
        self.professor = professor
        self.assistants = set()
        
    def add_assistant(self, assistant:str):
        self.assistants.add(assistant)
        
    def __str__(self):
        return f"{self.name} {self.professor} {self.assistants}"