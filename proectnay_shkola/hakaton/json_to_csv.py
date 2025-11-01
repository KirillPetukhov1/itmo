import pandas as pd
from pathlib import Path   

current_dir = Path.cwd()

json_file = 'train_hackathon_dataset.json'
output_csv_file = 'raw_data.csv'

json_path = current_dir / json_file
output_csv_file_path = current_dir / output_csv_file

with open(json_path, encoding='utf-8') as input_file:
    df = pd.read_json(input_file)

df.to_csv(output_csv_file_path, encoding='utf-8', index=False)