import pandas as pd

dataframe = pd.read_excel("employees.xlsx", sheet_name="OAS 2022-23")

pd.set_option('display.max_rows', len(dataframe))
print(dataframe[["Unnamed: 1", "Unnamed: 2", "Unnamed: 3", "Unnamed: 4", "Unnamed: 5", "Unnamed: 6", "Unnamed: 7"]])
pd.reset_option('display.max_rows')