import logging

## ensure is in filetype allowlist and is expected type
## does not contain double dot in name
## protect against file name length
## protect against file size
class Filename_Validator():
    __class_name = "Filename_Validator"
    JPG_FILE_TYPE = '.jpg'
    XLSX_FILE_TYPE = '.xlsx'
    PDF_FILE_TYPE = '.pdf'
    DOCX_FILE_TYPE = '.docx'

    FILE_SIZE_LIMIT = 20000000  # 20mb

    def validate(filename:str, file_size, expected_file_type: str):
        __logger = logging.getLogger('django')
        __method_name = "validate"

        filename_length = len(filename)

        if (filename_length < 4 or filename_length > 30):
            raise ValueError('Error: file must contain filetype and be longer than 30 characters')

        if (file_size < 1 or file_size > Filename_Validator.FILE_SIZE_LIMIT):
            raise ValueError('Error: file size rejected')

        if (filename.count('.') != 1):
            raise ValueError('Error: file must only contain one dot/period')

        Filename_Validator.is_accepted_file_type(filename, expected_file_type)

        __logger.info("File validation process complete.")
        __logger.info("Exiting method: " + __method_name)
        return 'valid'
    def is_accepted_file_type(filename: str, expected_file_type: str):
        __logger = logging.getLogger('django')
        __method_name = "isAcceptedFileType"

        if (expected_file_type == Filename_Validator.JPG_FILE_TYPE):
            file_extension = filename[-4:]
            if (file_extension != Filename_Validator.JPG_FILE_TYPE):
                raise ValueError('Error: expected .jpg file type')
        elif (expected_file_type == Filename_Validator.PDF_FILE_TYPE):
            file_extension = filename[-4:]
            if (file_extension != Filename_Validator.PDF_FILE_TYPE):
                raise ValueError('Error: expected .pdf file type')
        elif (expected_file_type == Filename_Validator.DOCX_FILE_TYPE):
            file_extension = filename[-5:]
            if (file_extension != Filename_Validator.DOCX_FILE_TYPE):
                raise ValueError('Error: expected .docx file type')
        elif (expected_file_type == Filename_Validator.XLSX_FILE_TYPE):
            file_extension = filename[-5:]
            if (file_extension != Filename_Validator.XLSX_FILE_TYPE):
                raise ValueError('Error: expected .xlsx file type')
        else:
            raise ValueError('Error: unexpected file type')

        __logger.info("Valid file type established.")
        __logger.info("Exiting method: " + __method_name)
        return 'valid'   # meets accepted file types
