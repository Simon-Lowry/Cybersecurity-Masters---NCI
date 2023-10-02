import openpyxl
from metadataWiperBackend.utils.file_operations import get_file_size
from metadataWiperBackend.utils.time_calculator import time_calculator
import metadataWiperBackend.properties as properties
import logging

class XLSXMetadataWiper:
    __logger = logging.getLogger('django')
    __class_name = "XLSXMetadataWiper"
    def perform_wipe_metadata(self, filename:str):
        __method_name = "perform_wipe_metadata"
        self.__logger.info("Entered method: " + __method_name)

        self.read_excel_file_metadata(filename)
        file_size_before_metadata_wipe = get_file_size(properties.FILE_DIRECTORY + filename)

        time = time_calculator()
        self.wipe_excel_file_metadata(filename)
        time_taken_to_wipe_metadata = time.get_time_taken_for_wiping_completion()
        self.read_excel_file_metadata(filename)
        file_size_after_metadata_wipe = get_file_size(properties.FILE_DIRECTORY + filename)
        self.__logger.info("File size before in bytes: " + str(file_size_before_metadata_wipe))
        self.__logger.info("File size after in bytes: " + str(file_size_after_metadata_wipe))
        self.__logger.info("Time taken to wipe metadata: " + time_taken_to_wipe_metadata)



      ######NOTE:
        #TODO: Must account for null and empty values, check for strings and dates
    def read_excel_file_metadata(self, filename: str):
        self.__logger.info("Reading excel file metadata: ")
        excel_workbook = openpyxl.load_workbook(properties.FILE_DIRECTORY + filename)
     #   self.__logger.info("Type: " + type(excel_workbook))
        created_date = excel_workbook.properties.created.strftime('%Y-%m-%d')
        last_modified = excel_workbook.properties.last_modified_by
        subject = excel_workbook.properties.subject
        keywords = excel_workbook.properties.keywords
        title = excel_workbook.properties.title
     #   company = excel_workbook.properties.company
        category = excel_workbook.properties.category

        self.__logger.info("Metadata of Excel file ")
        if type(title) == str:
            self.__logger.info("Title: " + subject)
        else:
            self.__logger.info("Title: empty")

        if type(created_date) == str:
            self.__logger.info("Created date: " + created_date)
        else:
            self.__logger.info("Created date: " + created_date)

        if type(last_modified) == str:
            self.__logger.info("Last modified by: " + last_modified)
        else:
            self.__logger.info("Last modified by: empty")

        if type(subject) == str:
            self.__logger.info("Subject: " + subject)
        else:
            self.__logger.info("Subject: empty")

        if type(keywords) == str:
            self.__logger.info("Keywords: " + keywords)
        else:
            self.__logger.info("Keywords: empty")

        if type(category) == str:
            self.__logger.info("Category: " + category)
        else:
            self.__logger.info("Category: empty")


    def wipe_excel_file_metadata(self, filename: str):
        self.__logger.info("Wiping file metadata....")
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
        self.__logger.info("Wiping file metadata process complete.")




