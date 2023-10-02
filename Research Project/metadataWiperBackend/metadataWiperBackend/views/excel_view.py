from rest_framework.views import APIView
from rest_framework.parsers import MultiPartParser, FormParser
from rest_framework.response import Response
from rest_framework import status
from metadataWiperBackend.validators.filename_validator import Filename_Validator
from metadataWiperBackend.validators.virus_total_file_validator import VirusTotalFileValidator
from metadataWiperBackend.serializers import XLSXSerializer
from metadataWiperBackend.services.xlsx_metadata_wiper import XLSXMetadataWiper
from metadataWiperBackend.utils.client_ip_logger import ClientIPLogger
from django.http import HttpResponse
import os
import metadataWiperBackend.properties as properties
import logging


class XLSXView(APIView):
    parser_classes = (MultiPartParser, FormParser)
    __logger = logging.getLogger('django')
    __class_name = "XLSXView"

    def post(self, request, *args, **kwargs):
        __method_name = "post"
        self.__logger.info("Entered method: " + __method_name + ", in class: " + self.__class_name)
        ClientIPLogger.log_ip(request)

        xlsx_serializer = XLSXSerializer(data=request.data)

        file = request.FILES['xlsx_file']
        filename = file.name
        if xlsx_serializer.is_valid():
            try:
                Filename_Validator.validate(filename, file.size, Filename_Validator.XLSX_FILE_TYPE)
            except ValueError as bad_filename_or_file_type_value:
                self.__logger.error("Error occurred: " + str(bad_filename_or_file_type_value))
                self.__logger.info("Exiting method: " + __method_name)
                return Response(str(bad_filename_or_file_type_value), status=status.HTTP_400_BAD_REQUEST)

            xlsx_serializer.save()
            VirusTotalFileValidator.is_file_clean(filename)
            wiper = XLSXMetadataWiper()
            wiper.perform_wipe_metadata(filename)

            wiped_xlsx_file = open(properties.FILE_DIRECTORY + filename, 'rb')

            response = HttpResponse(content=wiped_xlsx_file)
            response['Content-Type'] = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8'
            os.remove(properties.FILE_DIRECTORY + filename)
            self.__logger.info("File successfully removed from server.")
            return response
        else:
            self.__logger.error('error', xlsx_serializer.errors)
            return Response(xlsx_serializer.errors, status=status.HTTP_400_BAD_REQUEST)