from rest_framework.views import APIView
from rest_framework.parsers import MultiPartParser, FormParser
from rest_framework.response import Response
from rest_framework import status
from metadataWiperBackend.validators.filename_validator import Filename_Validator
from metadataWiperBackend.serializers import PDFSerializer
from metadataWiperBackend.services.pdf_metadata_wiper import PdfMetadataWiper
from django.http import HttpResponse
import os
import metadataWiperBackend.properties as properties

class PDFView(APIView):
    parser_classes = (MultiPartParser, FormParser)

    def post(self, request, *args, **kwargs):
        pdf_serializer = PDFSerializer(data=request.data)
        file = request.FILES['pdf_file']
        filename = file.name

        if pdf_serializer.is_valid():
            is_valid_file = Filename_Validator.validate(filename, file.size, Filename_Validator.PDF_FILE_TYPE)
            pdf_serializer.save()

            wiper = PdfMetadataWiper()
            wiper.perform_wipe_metadata(filename)

            if (is_valid_file == 'valid'):
                wiped_pdf_file = open(properties.FILE_DIRECTORY + filename, 'rb')

                response = HttpResponse(content=wiped_pdf_file)
                response['Content-Type'] = 'application/pdf;charset=UTF-8'
                os.remove(properties.FILE_DIRECTORY + wiper.new_file_name)
                print("File successfully removed from server.")
                return response
            else:
                print(is_valid_file)
                return Response(is_valid_file, status=status.HTTP_400_BAD_REQUEST)
        else:
            print('error', pdf_serializer.errors)
            return Response(pdf_serializer.errors, status=status.HTTP_400_BAD_REQUEST)

