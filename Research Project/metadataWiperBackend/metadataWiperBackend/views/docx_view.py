from rest_framework.views import APIView
from rest_framework.parsers import MultiPartParser, FormParser
from rest_framework.response import Response
from rest_framework import status
from metadataWiperBackend.validators.filename_validator import Filename_Validator
from metadataWiperBackend.serializers import DOCXSerializer
from metadataWiperBackend.services.docx_metadata_wiper import DocxMetadataWiper
from django.http import HttpResponse
import os
import metadataWiperBackend.properties as properties
from metadataWiperBackend.validators.virus_total_file_validator import VirusTotalFileValidator

class DOCXView(APIView):
    parser_classes = (MultiPartParser, FormParser)

    def post(self, request, *args, **kwargs):
        docx_serializer = DOCXSerializer(data=request.data)
        file = request.FILES['docx_file']
        filename = file.name

        if docx_serializer.is_valid():
            is_valid_file = Filename_Validator.validate(filename, file.size, Filename_Validator.DOCX_FILE_TYPE)
            if (is_valid_file == 'valid'):
                docx_serializer.save()
                VirusTotalFileValidator.is_file_clean(filename)
                wiper = DocxMetadataWiper()
                wiper.perform_wipe_metadata(filename)

                wiped_docx_file = open(properties.FILE_DIRECTORY + filename, 'rb')

                response = HttpResponse(content=wiped_docx_file)
                response['Content-Type'] = 'application/vnd.openxmlformats-officedocument.wordprocessingml.document;charset=UTF-8'
                os.remove(properties.FILE_DIRECTORY + filename)
                print("File successfully removed from server.")
                return response
            else:
                print(is_valid_file)
                return Response(is_valid_file, status=status.HTTP_400_BAD_REQUEST)
        else:
            return Response(docx_serializer.errors, status=status.HTTP_400_BAD_REQUEST)