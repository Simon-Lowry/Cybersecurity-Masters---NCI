from django.db import models

# lets us explicitly set upload path and filename
def upload_to(instance, filename):
    return 'images/{filename}'.format(filename=filename)

class Post(models.Model):
    title = models.CharField(max_length=100, blank=False, null=False)
    content = models.TextField()
    image = models.ImageField(upload_to=upload_to)

    def __str__(self):
        return self.title