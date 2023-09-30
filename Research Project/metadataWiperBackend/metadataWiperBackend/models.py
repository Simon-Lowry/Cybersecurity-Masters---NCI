from django.db import models

# lets us explicitly set upload path and filename
def upload_to(instance, filename):
    return 'files/{filename}'.format(filename=filename)

class JPEGModel(models.Model):
    image = models.ImageField(upload_to=upload_to)

class PDFModel(models.Model):
    pdf_file = models.FileField(upload_to=upload_to)

class XLSXModel(models.Model):
    xlsx_file = models.FileField(upload_to=upload_to)

class DOCXModel(models.Model):
    docx_file = models.FileField(upload_to=upload_to)