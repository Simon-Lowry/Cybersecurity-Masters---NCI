from PyPDF2 import PdfReader, PdfWriter, PdfFileMerger
from metadataWiperBackend.utils.file_operations import get_file_size
from metadataWiperBackend.utils.time_calculator import time_calculator
import metadataWiperBackend.properties as properties
import logging

class PdfMetadataWiper:
    __logger = logging.getLogger('django')
    __class_name = "PdfMetadataWiper"
    # All of the Document Information Dictionary Metadata attributes
    __METADATA_DID_ATTRIBUTES = ['/Title', '/Author', '/Subject', '/Keywords', '/Creator', '/Producer', '/CreationDate'
        , '/ModDate', '/Trapped']
    new_file_name = None

    def perform_wipe_metadata(self, filename):
        __method_name = "perform_wipe_metadata"
        self.__logger.info("Entered method: " + __method_name)

        self.wipePdfMetadata(filename)

    def wipePdfMetadata(self, filename):
        pdf = PdfReader(properties.FILE_DIRECTORY + filename)
        self.__logger.info("Beginning pdf wipe metdata process....")
        self.__logger.info("Filename: " + filename)
        self.__logger.info(len(pdf.pages))

        self.__logger.info("Did Metadata attributes prior to deletion: ")
        file_size_before_metadata_wipe = get_file_size(properties.FILE_DIRECTORY + filename)
        pdf_did_metadata = self.outputPDFDIDMetadata(filename)

        time = time_calculator()
        pdfContents = self.obtainPdfContents(filename)
        merged_pdf_data = addRedactedPDFMetadata(pdfContents)
        self.new_file_name = filename
        createNewPDFWithWipedMetadata( self.new_file_name, merged_pdf_data)
        time_taken_to_wipe_metadata = time.get_time_taken_for_wiping_completion()

        self.outputPDFDIDMetadata(self.new_file_name)
        file_size_after_metadata_wipe = get_file_size(properties.FILE_DIRECTORY + self.new_file_name)
        self.__logger.info("File size before in bytes: " + str(file_size_before_metadata_wipe))
        self.__logger.info("File size after in bytes: " + str(file_size_after_metadata_wipe))
        file_size_change = file_size_after_metadata_wipe - file_size_before_metadata_wipe
        self.__logger.info("Difference in file size after wiping metadata: " + str(file_size_change) + "kb")
        self.__logger.info("Time taken to wipe metadata: " + time_taken_to_wipe_metadata)

    def outputPDFDIDMetadata(self, pdfFile:str):
        """
        Collect Document Information Dictionary metadata
        """
        #Initializes a PdfFileReader object
        pdf_reader = PdfReader(properties.FILE_DIRECTORY + pdfFile)
        # Create an object containing the Document Information metadata
        didMetadataReader = pdf_reader.metadata
        didMetadata = {}
        for i in self.__METADATA_DID_ATTRIBUTES:
            try:
                didMetadata[i] = didMetadataReader.get(i)
            except:
                didMetadata[i] = ''
        self.__logger.info(didMetadata)
        return didMetadata

    def obtainPdfContents(self, pdfFile: str):
        pdfReader = PdfReader(properties.FILE_DIRECTORY + pdfFile)
        pdfContents = PdfWriter()

        for pageNum in range (len(pdfReader.pages)):
            page = pdfReader.pages[pageNum]
            pdfContents.add_page(page)
        return pdfContents



def addRedactedPDFMetadata(pdfContents: PdfWriter ):
    # pdf_merger.append(fileobj=open(pdfFile, 'rb'))
    emptyString = ''

    # created_date = obj.strftime('%Y-%m-%d')

    pdfContents.add_metadata({
        '/Author': emptyString
        , '/Subject': emptyString
        , '/Title': emptyString
        , '/Keywords': emptyString
        , '/Producer': emptyString
        , '/Creator': emptyString
   #     , '/CreationDate': metadataDictionary.get('CreationDate')  # need to fix
    #    , '/ModDate': metadataDictionary.get('ModDate')  # need to fix, not displaying well
    })
    return pdfContents

def createNewPDFWithWipedMetadata(filename:str, pdfContents: PdfWriter):
    pdf_out = open(properties.FILE_DIRECTORY + filename, 'wb')
    pdfContents.write(pdf_out)
    pdf_out.close()
    pdfContents.close()