from rest_framework import serializers
from .models import JPEGModel, PDFModel, XLSXModel, DOCXModel
#from metadataWiperBackend.models. import PDFModel

class JPEGSerializer(serializers.ModelSerializer):
    class Meta:
        model = JPEGModel
        fields = '__all__'


class PDFSerializer(serializers.ModelSerializer):
    class Meta:
        model = PDFModel
        fields = '__all__'

class DOCXSerializer(serializers.ModelSerializer):
    class Meta:
        model = DOCXModel
        fields = '__all__'


class XLSXSerializer(serializers.ModelSerializer):
    class Meta:
        model = XLSXModel
        fields = '__all__'
