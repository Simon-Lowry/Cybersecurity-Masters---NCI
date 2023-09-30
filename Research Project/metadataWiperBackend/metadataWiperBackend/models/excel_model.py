from django.db import models

# lets us explicitly set upload path and filename
def upload_to(instance, filename):
    return 'images/{filename}'.format(filename=filename)

class XLSXModel(models.Model):
    file = models.FileField(upload_to=upload_to)
