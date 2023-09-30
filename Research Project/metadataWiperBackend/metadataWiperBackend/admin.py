from django.contrib import admin
from .models import JPEGModel, PDFModel, XLSXModel, DOCXModel


# Register your models here.
admin.site.register(JPEGModel)
admin.site.register(PDFModel)
admin.site.register(XLSXModel)
admin.site.register(DOCXModel)
