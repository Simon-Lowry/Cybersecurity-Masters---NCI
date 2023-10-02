import os
import logging

__logger = logging.getLogger('django')
__class_name = "file_operations"


def get_file_size(file_path: str):
    __method_name = "get_file_size"
    __logger.info("Entered method: " + __method_name)

    try:
        file_size = os.path.getsize(file_path)
        __logger.info(f"File Size in Bytes is {file_size}")
        __logger.info("Exiting method: " + __method_name)
        return file_size
    except FileNotFoundError:
        __logger.error("File not found.")
        __logger.info("Exiting method: " + __method_name)
    except OSError:
        __logger.error("OS error occurred.")
        __logger.info("Exiting method: " + __method_name)
