import pandas as pd

dataframe = pd.read_excel("employees.xlsx", sheet_name="OAS 2022-23")



# Funkcija za konverziju ćirilice u latinicu za sve kolone
def cirilica_u_latinicu_dataframe(df):
    zamene = {
        'А': 'A', 'Б': 'B', 'Ц': 'C', 'Д': 'D', 'Е': 'E', 'Ф': 'F', 'Г': 'G', 'Х': 'H', 'И': 'I', 'Ј': 'J',
        'К': 'K', 'Л': 'L', 'М': 'M', 'Н': 'N', 'О': 'O', 'П': 'P', 'Р': 'R', 'С': 'S', 'Т': 'T', 'У': 'U',
        'В': 'V', 'З': 'Z', 'Ћ': 'C', 'Ш': 'S', 'Ч': 'C', 'Ж': 'Z', 'ћ': 'c', 'ш': 's', 'ч': 'c', 'ж': 'z',
        'Ђ': 'Dj', 'ђ': 'dj',
        'а': 'a', 'б': 'b', 'ц': 'c', 'д': 'd', 'е': 'e', 'ф': 'f', 'г': 'g', 'х': 'h', 'и': 'i', 'ј': 'j',
        'к': 'k', 'л': 'l', 'м': 'm', 'н': 'n', 'о': 'o', 'п': 'p', 'р': 'r', 'с': 's', 'т': 't', 'у': 'u',
        'в': 'v', 'з': 'z', 'ћ': 'c', 'ш': 's', 'ч': 'c', 'ж': 'z', 'ђ': 'dj', 'љ': 'lj', 'њ': 'nj', 'џ': 'dz'
    }

    for kolona in df.columns:
        if df[kolona].dtype == 'object':  # Provera da li je kolona tipa string (object)
            df[kolona] = df[kolona].apply(lambda x: ''.join([zamene.get(c, c) for c in str(x)]))
    
    return df


# Konvertovanje celog DataFrame-a
dataframe = cirilica_u_latinicu_dataframe(dataframe)



pd.set_option('display.max_rows', len(dataframe))
print(dataframe[["Unnamed: 1", "Unnamed: 2", "Unnamed: 3", "Unnamed: 4", "Unnamed: 5", "Unnamed: 6", "Unnamed: 7"]])
pd.reset_option('display.max_rows')