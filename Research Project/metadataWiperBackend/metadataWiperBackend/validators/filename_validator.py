
## ensure is in filetype allowlist and is expected type
## does not contain double dot in name
## protect against file name length
## protect against file size
class Filename_Validator():
    JPG_FILE_TYPE = '.jpg'
    XLSX_FILE_TYPE = '.xlsx'
    PDF_FILE_TYPE = '.pdf'
    DOCX_FILE_TYPE = '.docx'

    FILE_SIZE_LIMIT = 20000000  # 20mb

    def validate(filename:str, file_size, expected_file_type: str):
        filename_length = len(filename)

        if (filename_length < 4 or filename_length > 30):
            return 'Error: file must contain filetype and be longer than 30 characters'

        if (file_size < 1 or file_size > Filename_Validator.FILE_SIZE_LIMIT):
            return 'Error: file size rejected'

        if (filename.count('.') != 1):
            return 'Error: file must only contain one dot/period'

        error_on_file_type = Filename_Validator.isAcceptedFileType(filename, expected_file_type)
        if (error_on_file_type != 'valid'):
            return error_on_file_type

        print ("File validation process complete.")
        return 'valid'
    def isAcceptedFileType(filename: str, expected_file_type: str):
        if (expected_file_type == Filename_Validator.JPG_FILE_TYPE):
            file_extension = filename[-4:]
            if (file_extension != Filename_Validator.JPG_FILE_TYPE):
                return 'Error: expected .jpg file type'
        elif (expected_file_type == Filename_Validator.PDF_FILE_TYPE):
            file_extension = filename[-4:]
            if (file_extension != Filename_Validator.PDF_FILE_TYPE):
                return 'Error: expected .pdf file type'
        elif (expected_file_type == Filename_Validator.DOCX_FILE_TYPE):
            file_extension = filename[-5:]
            if (file_extension != Filename_Validator.DOCX_FILE_TYPE):
                return 'Error: expected .docx file type'
        elif (expected_file_type == Filename_Validator.XLSX_FILE_TYPE):
            file_extension = filename[-5:]

            if (file_extension != Filename_Validator.XLSX_FILE_TYPE):
                return 'Error: expected .xlsx file type'
        else:
            return 'Error: unexpected file type'

        return 'valid'   # meets accepted file types
