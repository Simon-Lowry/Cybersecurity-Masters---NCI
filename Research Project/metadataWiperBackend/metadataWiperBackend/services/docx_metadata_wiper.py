import os
import docx
import datetime
from metadataWiperBackend.utils.file_operations import get_file_size
from metadataWiperBackend.utils.time_calculator import time_calculator
import metadataWiperBackend.properties as properties

class DocxMetadataWiper:
    def perform_wipe_metadata(self, docxFile: str):
        self.get_docx_file_metdata(docxFile)

        file_size_before_metadata_wipe = get_file_size(properties.FILE_DIRECTORY + docxFile)
        print("File size in bytes before wipe: " + str(file_size_before_metadata_wipe))

        # perform metadata wiping and measure time taken to complete wiping
        time = time_calculator()
        self.wipe_docx_file_metdata(docxFile, properties.FILE_DIRECTORY)
        time_taken_to_wipe_metadata = time.get_time_taken_for_wiping_completion()

        self.get_docx_file_metdata(docxFile)
        file_size_after_metadata_wipe = get_file_size(properties.FILE_DIRECTORY + docxFile)

        print("File size in bytes after wipe: " + str(file_size_after_metadata_wipe))
        print ("Time taken to wipe metadata: " + time_taken_to_wipe_metadata)



    def get_docx_file_metdata(self, docxFile: str):
        print("Entering get file metadata....")
        os.chdir(properties.FILE_DIRECTORY)
        doc = docx.Document(docxFile)

        prop = doc.core_properties
        metadata = {}
        for d in dir(prop):
            if not d.startswith('_'):
                metadata[d] = getattr(prop, d)

        print("Docx file metadata prior to wiping:")
        print(metadata)
        return metadata


    def wipe_docx_file_metdata(self, docxFile: str, directory: str):
        os.chdir(directory)

        doc = docx.Document('Research.docx')

        bogus_date = datetime.datetime(1900, 1, 1, 1, 1)
        doc.core_properties.created = bogus_date
        doc.core_properties.author = ''
        doc.core_properties.category = ''
        doc.core_properties.content_status = ''
        doc.core_properties.identifier = ''
        doc.core_properties.keywords = ''
        doc.core_properties.language = ''
        doc.core_properties.last_modified_by = bogus_date
        doc.core_properties.last_printed = bogus_date
        doc.core_properties.modified = bogus_date
        doc.core_properties.revision = 1
        doc.core_properties.subject = ''
        doc.core_properties.title = ''
        doc.core_properties.version = '1.0'
        doc.core_properties.comments = ''

        doc.save("Research.docx")



