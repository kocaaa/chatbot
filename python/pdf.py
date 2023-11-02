from models import Exam, YearExams

import string_utils
import pdfplumber

def get_exam_schedule():
    exam_schedules = []
    with pdfplumber.open("raspored_ispita.pdf") as pdf:
        for page in pdf.pages:
            table = page.extract_table()
            if table:
                for row in table:
                    exam_schedules.append(row)
                    
    return exam_schedules

def year_check(input_string):
    substrings = [". godina", "Master"]
    for substring in substrings:
        if substring in input_string:
            return True
    return False

def get_exam_schedule_hierarchy():
    schedule = string_utils.convert_matrix(get_exam_schedule())
    current_year = None
    exam_schedule = []

    for exam in schedule:
        if year_check(exam[0]):
            exam = YearExams(exam[0], exam[1:])
            exam_schedule.append(exam)
            current_year = exam
        else:
            exam = Exam(exam[0], exam[1:])
            current_year.add_exam(exam)
            
    return exam_schedule
