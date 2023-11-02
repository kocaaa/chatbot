from models import Class

import pandas as pd
import string_utils

CLASS = "Predmet"
ASSISTANT = "Saradnik" 
PROFESSOR = "Nastavnik"
INVALID_VALUES = ["nan", "Vezbe", "Saradnik", "mentor"]

def get_all_subjects():
    dataframe = pd.read_excel("employees.xlsx", sheet_name="OAS 2022-23")
    
    dataframe = string_utils.convert_dataframe(dataframe)
    dataframe = dataframe[["Unnamed: 1", "Unnamed: 6", "Unnamed: 8"]]
    dataframe.rename(columns={"Unnamed: 1":CLASS, "Unnamed: 6":PROFESSOR,"Unnamed: 8":ASSISTANT}, inplace=True)
    dataframe.reset_index(inplace=True)
        
    classes = []
    new_class : Class = None

    for index, row in dataframe.iterrows():
        if row[ASSISTANT] not in INVALID_VALUES:        
            if row[CLASS] not in INVALID_VALUES:
                if new_class is not None:
                    classes.append(new_class)
                new_class = Class(row[CLASS].split("+")[0].split("\n")[0].strip(), row[PROFESSOR].strip())
                new_class.initials = string_utils.get_first_letters(new_class.name)
            new_class.add_assistant(row[ASSISTANT].strip())
        else:
            if new_class is not None:
                classes.append(new_class)
                new_class = None
        
    return classes
