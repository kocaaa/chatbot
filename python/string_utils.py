swap = {
        'А': 'A', 'Б': 'B', 'Ц': 'C', 'Д': 'D', 'Е': 'E', 'Ф': 'F', 'Г': 'G', 'Х': 'H', 'И': 'I', 'Ј': 'J',
        'К': 'K', 'Л': 'L', 'Љ' : 'Lj' , 'М': 'M', 'Н': 'N', 'О': 'O', 'П': 'P', 'Р': 'R', 'С': 'S', 'Т': 'T', 'У': 'U',
        'В': 'V', 'З': 'Z', 'Ћ': 'C', 'Ш': 'S', 'Ч': 'C', 'Ж': 'Z', 'ћ': 'c', 'ш': 's', 'ч': 'c', 'ж': 'z',
        'Ђ': 'Dj', 'ђ': 'dj',
        'а': 'a', 'б': 'b', 'ц': 'c', 'д': 'd', 'е': 'e', 'ф': 'f', 'г': 'g', 'х': 'h', 'и': 'i', 'ј': 'j',
        'к': 'k', 'л': 'l', 'м': 'm', 'н': 'n', 'о': 'o', 'п': 'p', 'р': 'r', 'с': 's', 'т': 't', 'у': 'u',
        'в': 'v', 'з': 'z', 'ћ': 'c', 'ш': 's', 'ч': 'c', 'ж': 'z', 'ђ': 'dj', 'љ': 'lj', 'њ': 'nj', 'џ': 'dz', '-' : ' '
    }

def convert_dataframe(df):
    for column in df.columns:
        if df[column].dtype == 'object':
            df[column] = df[column].apply(lambda x: ''.join([swap.get(c, c) for c in str(x)]))
    
    return df

def convert_matrix(matrix):
    for array in matrix:
        for i in range(len(array)):
            array[i] = ''.join([swap.get(c, c) for c in str(array[i])])
    
    return matrix

def get_first_letters(input_string):
    words = not_old_programe(input_string).split()
    valid_words = []

    for word in words:
        if len(word) >= 3 or (len(word) == 1 and word.isdigit()):
            valid_words.append(word[0])

    result = ''.join(valid_words).lower()
    return result

def not_old_programe(string):
    substring_to_remove = "(stari program)"
    if string.startswith(substring_to_remove):
        string = string[len(substring_to_remove):].strip()
    return string
