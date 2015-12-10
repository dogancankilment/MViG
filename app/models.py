from django.db import models


class Post(models.Model):
    message_receiver = models.CharField(max_length=100)
    message_content = models.TextField()