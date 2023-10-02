from metadataWiperBackend.serializers import JPEGSerializer
from metadataWiperBackend.models import JPEGModel
from rest_framework.views import APIView
from rest_framework.parsers import MultiPartParser, FormParser
from rest_framework.response import Response
from rest_framework import status
from metadataWiperBackend.validators.filename_validator import Filename_Validator
from metadataWiperBackend.validators.virus_total_file_validator import VirusTotalFileValidator
from metadataWiperBackend.services.jpeg_metadata_wiper import JpegMetadataWiper
from django.http import HttpResponse
import os
import metadataWiperBackend.properties as properties
import logging


class JPEGView(APIView):
    parser_classes = (MultiPartParser, FormParser)
    __logger = logging.getLogger('django')
    __class_name = "JPEGView"

    def post(self, request, *args, **kwargs):
        __method_name = "post"
        self.__logger.info("Entered method: " + __method_name + ", in class: " + self.__class_name)
        posts_serializer = JPEGSerializer(data=request.data)
        file = request.FILES['image']
        filename = file.name

        if posts_serializer.is_valid():
            try:
                Filename_Validator.validate(filename, file.size, Filename_Validator.JPG_FILE_TYPE)
            except ValueError as bad_filename_or_file_type_value:
                self.__logger.error("Error occurred: " + str(bad_filename_or_file_type_value))
                self.__logger.info("Exiting method: " + __method_name)
                return Response(str(bad_filename_or_file_type_value), status=status.HTTP_400_BAD_REQUEST)

            posts_serializer.save()
            VirusTotalFileValidator.is_file_clean(filename)
            wiper = JpegMetadataWiper()
            wiper.perform_wipe_metadata(filename)
            wiped_jpeg_file = open(properties.FILE_DIRECTORY + filename, 'rb')

            response = HttpResponse(content=wiped_jpeg_file)
            response['Content-Type'] = 'image/jpeg'
            os.remove(properties.FILE_DIRECTORY + filename)

            self.__logger.info("File successfully removed from server.")
            self.__logger.info("Exiting method: " + __method_name)
            return response
        else:
            self.__logger.error("Error occurred: " + posts_serializer.errors)
            return Response(posts_serializer.errors, status=status.HTTP_400_BAD_REQUEST)

