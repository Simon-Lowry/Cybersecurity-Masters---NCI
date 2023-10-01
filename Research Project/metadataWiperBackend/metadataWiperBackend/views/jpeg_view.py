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

class JPEGView(APIView):
    parser_classes = (MultiPartParser, FormParser)

    def post(self, request, *args, **kwargs):
        posts_serializer = JPEGSerializer(data=request.data)
        file = request.FILES['image']
        filename = file.name

        if posts_serializer.is_valid():
            is_valid_file = Filename_Validator.validate(filename, file.size, Filename_Validator.JPG_FILE_TYPE)
            if (is_valid_file == 'valid'):
                posts_serializer.save()
                VirusTotalFileValidator.is_file_clean(filename)
                wiper = JpegMetadataWiper()
                wiper.perform_wipe_metadata(filename)
                wiped_jpeg_file = open(properties.FILE_DIRECTORY + filename, 'rb')

                response = HttpResponse(content=wiped_jpeg_file)
                response['Content-Type'] = 'image/jpeg'
                os.remove(properties.FILE_DIRECTORY + filename)
                print("File successfully removed from server.")
                return response
            else:
                print(is_valid_file)
                return Response(is_valid_file, status=status.HTTP_400_BAD_REQUEST)
        else:
            print('error', posts_serializer.errors)
            return Response(posts_serializer.errors, status=status.HTTP_400_BAD_REQUEST)

