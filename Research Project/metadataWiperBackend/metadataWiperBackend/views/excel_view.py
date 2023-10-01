from rest_framework.views import APIView
from rest_framework.parsers import MultiPartParser, FormParser
from rest_framework.response import Response
from rest_framework import status
from metadataWiperBackend.validators.filename_validator import Filename_Validator
from metadataWiperBackend.validators.virus_total_file_validator import VirusTotalFileValidator
from metadataWiperBackend.serializers import XLSXSerializer
from metadataWiperBackend.services.xlsx_metadata_wiper import XLSXMetadataWiper
from django.http import HttpResponse
import os
import metadataWiperBackend.properties as properties


class XLSXView(APIView):
    parser_classes = (MultiPartParser, FormParser)

    def post(self, request, *args, **kwargs):
        xlsx_serializer = XLSXSerializer(data=request.data)

        file = request.FILES['xlsx_file']
        filename = file.name
        if xlsx_serializer.is_valid():
            is_valid_file = Filename_Validator.validate(filename, file.size, Filename_Validator.XLSX_FILE_TYPE)

            if (is_valid_file == 'valid'):
                xlsx_serializer.save()
                VirusTotalFileValidator.is_file_clean(filename)
                wiper = XLSXMetadataWiper()
                wiper.perform_wipe_metadata(filename)

                wiped_xlsx_file = open(properties.FILE_DIRECTORY + filename, 'rb')

                response = HttpResponse(content=wiped_xlsx_file)
                response['Content-Type'] = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8'
                os.remove(properties.FILE_DIRECTORY + filename)
                print("File successfully removed from server.")
                return response
            else:
                print(is_valid_file)
                return Response(is_valid_file, status=status.HTTP_400_BAD_REQUEST)
        else:
            print('error', xlsx_serializer.errors)
            return Response(xlsx_serializer.errors, status=status.HTTP_400_BAD_REQUEST)