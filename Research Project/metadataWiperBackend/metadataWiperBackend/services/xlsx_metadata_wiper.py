import openpyxl
from metadataWiperBackend.utils.file_operations import get_file_size
from metadataWiperBackend.utils.time_calculator import time_calculator
import metadataWiperBackend.properties as properties

class XLSXMetadataWiper:
    def perform_wipe_metadata(self, filename:str):
        self.read_excel_file_metadata(filename)
        file_size_before_metadata_wipe = get_file_size(properties.FILE_DIRECTORY + filename)
        print()

        time = time_calculator()
        self.wipe_excel_file_metadata(filename)
        print()
        time_taken_to_wipe_metadata = time.get_time_taken_for_wiping_completion()
        self.read_excel_file_metadata(filename)
        file_size_after_metadata_wipe = get_file_size(properties.FILE_DIRECTORY + filename)
        print("File size before in bytes: " + str(file_size_before_metadata_wipe))
        print("File size after in bytes: " + str(file_size_after_metadata_wipe))
        print ("Time taken to wipe metadata: " + time_taken_to_wipe_metadata)



      ######NOTE:
        #TODO: Must account for null and empty values, check for strings and dates
    def read_excel_file_metadata(self, filename: str):
        print("Reading excel file metadata: ")
        excel_workbook = openpyxl.load_workbook(properties.FILE_DIRECTORY + filename)
     #   print("Type: " + type(excel_workbook))
        created_date = excel_workbook.properties.created.strftime('%Y-%m-%d')
        last_modified = excel_workbook.properties.last_modified_by
        subject = excel_workbook.properties.subject
        keywords = excel_workbook.properties.keywords
        title = excel_workbook.properties.title
     #   company = excel_workbook.properties.company
        category = excel_workbook.properties.category

        print ("Metadata of Excel file ")
        if type(title) == str:
            print ("Title: " + subject)
        else:
            print ("Title: empty")

        if type(created_date) == str:
            print("Created date: " + created_date)
        else:
            print("Created date: " + created_date)

        if type(last_modified) == str:
            print("Last modified by: " + last_modified)
        else:
            print("Last modified by: empty")

        if type(subject) == str:
            print("Subject: " + subject)
        else:
            print("Subject: empty")

        if type(keywords) == str:
            print("Keywords: " + keywords)
        else:
            print("Keywords: empty")

        if type(category) == str:
            print("Category: " + category)
        else:
            print("Category: empty")


    def wipe_excel_file_metadata(self, filename: str):
        print("Wiping file metadata....")
        excel_workbook = openpyxl.load_workbook(properties.FILE_DIRECTORY + filename)
        #   print("Type: " + type(excel_workbook))
        #TODO: fix this for dates
     #   created_date = excel_workbook.properties.created.strftime('%Y-%m-%d')
        excel_workbook.properties.last_modified_by = ''
        excel_workbook.properties.subject = ''
        excel_workbook.properties.keywords = ''
        excel_workbook.properties.title = ''
        #   company = excel_workbook.properties.company
        excel_workbook.properties.category = ''
        excel_workbook.save(properties.FILE_DIRECTORY + filename)
        print("Wiping file metadata process complete.")




