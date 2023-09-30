from PyPDF2 import PdfReader, PdfWriter, PdfFileMerger
from metadataWiperBackend.utils.file_operations import get_file_size
from metadataWiperBackend.utils.time_calculator import time_calculator
import metadataWiperBackend.properties as properties

class PdfMetadataWiper:
    # All of the Document Information Dictionary Metadata attributes
    __METADATA_DID_ATTRIBUTES = ['/Title', '/Author', '/Subject', '/Keywords', '/Creator', '/Producer', '/CreationDate'
        , '/ModDate', '/Trapped']
    new_file_name = None

    def perform_wipe_metadata(self, filename):
        self.wipePdfMetadata(filename)

    def wipePdfMetadata(self, filename):
        pdf = PdfReader(properties.FILE_DIRECTORY + filename)
        print ("Beginning pdf wipe metdata process....")
        print ("Filename: " + filename)
        print(len(pdf.pages))

        print ("Did Metadata attributes prior to deletion: ")
        file_size_before_metadata_wipe = get_file_size(properties.FILE_DIRECTORY + filename)
        pdf_did_metadata = self.outputPDFDIDMetadata(filename)

        time = time_calculator()
        pdfContents = self.obtainPdfContents(filename)
        merged_pdf_data = addRedactedPDFMetadata(pdfContents)
        print (merged_pdf_data)
        self.new_file_name = filename
        createNewPDFWithWipedMetadata( self.new_file_name, merged_pdf_data)
        time_taken_to_wipe_metadata = time.get_time_taken_for_wiping_completion()

        self.outputPDFDIDMetadata(self.new_file_name)
        file_size_after_metadata_wipe = get_file_size(properties.FILE_DIRECTORY + self.new_file_name)
        print("File size before in bytes: " + str(file_size_before_metadata_wipe))
        print("File size after in bytes: " + str(file_size_after_metadata_wipe))
        print("Time taken to wipe metadata: " + time_taken_to_wipe_metadata)

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
        print(didMetadata)
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