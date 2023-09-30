import os


def get_file_size(file_path: str):
    try:
        file_size = os.path.getsize(file_path)
        print(f"File Size in Bytes is {file_size}")
        return file_size
    except FileNotFoundError:
        print("File not found.")
    except OSError:
        print("OS error occurred.")