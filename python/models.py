from pydantic import BaseModel
import string_utils

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
    
class YearExams:
    def __init__(self, year, months):
        self.year = year
        self.months = []
        self.exams = []
        
        for month in months:
            self.months.append(Month(month))

    def add_exam(self, exam):
        self.exams.append(exam)
        
    def __str__(self) -> str:
        return f"{self.year} {self.months} {self.exams}"
        
class Exam:
    def __init__(self, name, dates):
        self.name = name
        self.dates = dates
        self.initials = string_utils.get_first_letters(name)
    
    def __str__(self) -> str:
        return f"{self.name} {self.dates}"
    
class Month:
    def __init__(self, string):
        self.name, self.time = string.split("\n")
    
    def __str__(self) -> str:
        return f"{self.name} {self.time}"