import pandas as pd
from models import Class

CLASS = "Predmet"
PROFESSOR = "Nastavnik"
ASSISTANT = "Saradnik"
INVALID_VALUES = ["nan", "Vezbe", "Saradnik", "mentor"]

zamene = {
        'А': 'A', 'Б': 'B', 'Ц': 'C', 'Д': 'D', 'Е': 'E', 'Ф': 'F', 'Г': 'G', 'Х': 'H', 'И': 'I', 'Ј': 'J',
        'К': 'K', 'Л': 'L', 'Љ' : 'Lj' , 'М': 'M', 'Н': 'N', 'О': 'O', 'П': 'P', 'Р': 'R', 'С': 'S', 'Т': 'T', 'У': 'U',
        'В': 'V', 'З': 'Z', 'Ћ': 'C', 'Ш': 'S', 'Ч': 'C', 'Ж': 'Z', 'ћ': 'c', 'ш': 's', 'ч': 'c', 'ж': 'z',
        'Ђ': 'Dj', 'ђ': 'dj',
        'а': 'a', 'б': 'b', 'ц': 'c', 'д': 'd', 'е': 'e', 'ф': 'f', 'г': 'g', 'х': 'h', 'и': 'i', 'ј': 'j',
        'к': 'k', 'л': 'l', 'м': 'm', 'н': 'n', 'о': 'o', 'п': 'p', 'р': 'r', 'с': 's', 'т': 't', 'у': 'u',
        'в': 'v', 'з': 'z', 'ћ': 'c', 'ш': 's', 'ч': 'c', 'ж': 'z', 'ђ': 'dj', 'љ': 'lj', 'њ': 'nj', 'џ': 'dz', '-' : ' '
    }

def cirilica_u_latinicu_dataframe(df):
    for kolona in df.columns:
        if df[kolona].dtype == 'object':
            df[kolona] = df[kolona].apply(lambda x: ''.join([zamene.get(c, c) for c in str(x)]))
    
    return df

def get_first_letters(input_string):
    words = input_string.split()
    valid_words = []

    for word in words:
        if len(word) >= 3 or (len(word) == 1 and word.isdigit()):
            valid_words.append(word[0])

    result = ''.join(valid_words).lower()
    return result
    
def get_all_subjects():
    dataframe = pd.read_excel("employees.xlsx", sheet_name="OAS 2022-23")
    
    dataframe = cirilica_u_latinicu_dataframe(dataframe)
    dataframe = dataframe[["Unnamed: 1", "Unnamed: 6", "Unnamed: 8"]]
    dataframe.rename(columns={"Unnamed: 1":CLASS, "Unnamed: 6":PROFESSOR,"Unnamed: 8":ASSISTANT},inplace=True)
    dataframe.reset_index(inplace=True)
        
    classes = []
    new_class : Class = None

    for index, row in dataframe.iterrows():
        if row[ASSISTANT] not in INVALID_VALUES:        
            if row[CLASS] not in INVALID_VALUES:
                if new_class is not None:
                    classes.append(new_class)
                new_class = Class(row[CLASS].split("+")[0].split("\n")[0].strip(), row[PROFESSOR].strip())
                new_class.initials = get_first_letters(new_class.name)
            new_class.add_assistant(row[ASSISTANT].strip())
        else:
            if new_class is not None:
                classes.append(new_class)
                new_class = None
        
    return classes