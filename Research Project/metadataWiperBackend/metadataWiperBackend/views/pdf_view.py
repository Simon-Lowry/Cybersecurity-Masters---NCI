from rest_framework.views import APIView
from rest_framework.parsers import MultiPartParser, FormParser
from rest_framework.response import Response
from rest_framework import status
from metadataWiperBackend.validators.filename_validator import Filename_Validator
from metadataWiperBackend.validators.virus_total_file_validator import VirusTotalFileValidator
from metadataWiperBackend.serializers import PDFSerializer
from metadataWiperBackend.services.pdf_metadata_wiper import PdfMetadataWiper
from django.http import HttpResponse
import os
import metadataWiperBackend.properties as properties
import logging


class PDFView(APIView):
    parser_classes = (MultiPartParser, FormParser)
    __logger = logging.getLogger('django')
    __class_name = "PDFView"


    def post(self, request, *args, **kwargs):
        __method_name = "post"
        self.__logger.info("Entered method: " + __method_name + ", in class: " + self.__class_name)
        pdf_serializer = PDFSerializer(data=request.data)
        file = request.FILES['pdf_file']
        filename = file.name

        if pdf_serializer.is_valid():
            try:
                Filename_Validator.validate(filename, file.size, Filename_Validator.PDF_FILE_TYPE)
            except ValueError as inappropriate_value:
                self.__logger.error("Error occurred: " + str(inappropriate_value))
                self.__logger.info("Exiting method: " + __method_name)
                return Response(str(inappropriate_value), status=status.HTTP_400_BAD_REQUEST)

            pdf_serializer.save()
            VirusTotalFileValidator.is_file_clean(filename)

            wiper = PdfMetadataWiper()
            wiper.perform_wipe_metadata(filename)

            wiped_pdf_file = open(properties.FILE_DIRECTORY + filename, 'rb')

            response = HttpResponse(content=wiped_pdf_file)
            response['Content-Type'] = 'application/pdf;charset=UTF-8'
            os.remove(properties.FILE_DIRECTORY + wiper.new_file_name)
            self.__logger.info("File successfully removed from server.")
            self.__logger.info("Exiting method: " + __method_name)
            return response
        else:
            self.__logger.error('Error occurred: ', pdf_serializer.errors)
            return Response(pdf_serializer.errors, status=status.HTTP_400_BAD_REQUEST)

