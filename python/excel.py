import pandas as pd

dataframe = pd.read_excel("employees.xlsx", sheet_name="OAS 2022-23")

CLASS = "Predmet"
PROFESSOR = "Nastavnik"
ASSISTANT = "Saradnik"
INVALID_VALUES = ["nan", "Vezbe", "Saradnik", "mentor"]

# Funkcija za konverziju ćirilice u latinicu za sve kolone
def cirilica_u_latinicu_dataframe(df):
    zamene = {
        'А': 'A', 'Б': 'B', 'Ц': 'C', 'Д': 'D', 'Е': 'E', 'Ф': 'F', 'Г': 'G', 'Х': 'H', 'И': 'I', 'Ј': 'J',
        'К': 'K', 'Л': 'L', 'Љ' : 'Lj' , 'М': 'M', 'Н': 'N', 'О': 'O', 'П': 'P', 'Р': 'R', 'С': 'S', 'Т': 'T', 'У': 'U',
        'В': 'V', 'З': 'Z', 'Ћ': 'C', 'Ш': 'S', 'Ч': 'C', 'Ж': 'Z', 'ћ': 'c', 'ш': 's', 'ч': 'c', 'ж': 'z',
        'Ђ': 'Dj', 'ђ': 'dj',
        'а': 'a', 'б': 'b', 'ц': 'c', 'д': 'd', 'е': 'e', 'ф': 'f', 'г': 'g', 'х': 'h', 'и': 'i', 'ј': 'j',
        'к': 'k', 'л': 'l', 'м': 'm', 'н': 'n', 'о': 'o', 'п': 'p', 'р': 'r', 'с': 's', 'т': 't', 'у': 'u',
        'в': 'v', 'з': 'z', 'ћ': 'c', 'ш': 's', 'ч': 'c', 'ж': 'z', 'ђ': 'dj', 'љ': 'lj', 'њ': 'nj', 'џ': 'dz', '-' : ' ' #, '\r\n': ' ', '\n' : ' ' 
    }

    for kolona in df.columns:
        if df[kolona].dtype == 'object':  # Provera da li je kolona tipa string (object)
            df[kolona] = df[kolona].apply(lambda x: ''.join([zamene.get(c, c) for c in str(x)]))
    
    return df


# Konvertovanje celog DataFrame-a
dataframe = cirilica_u_latinicu_dataframe(dataframe)
dataframe = dataframe[["Unnamed: 1", "Unnamed: 6", "Unnamed: 8"]]

dataframe.rename(columns={"Unnamed: 1":CLASS, "Unnamed: 6":PROFESSOR,"Unnamed: 8":ASSISTANT},inplace=True)

dataframe.reset_index(inplace=True)

current_class = None
current_professor = None
current_assistant = None

for index, row in dataframe.iterrows():
    # print(row[CLASS]) 
    
    if row[ASSISTANT] not in INVALID_VALUES:        
        if row[CLASS] not in INVALID_VALUES:
            current_class = row[CLASS].split("+")[0].split("\n")[0].strip()
            current_professor = row[PROFESSOR].strip()
        current_assistant = row[ASSISTANT].strip()
    
        print(current_class , " | ", current_professor, " | " , current_assistant)   
    
    # else:
    #     previous_class = None
    #     previos_professor = None
    
    
    previos_professor = row[PROFESSOR]
    previous_class = row[PROFESSOR]


# pd.set_option('display.max_rows', len(dataframe))
# print(dataframe)
# pd.reset_option('display.max_rows')