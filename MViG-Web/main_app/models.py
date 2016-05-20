from django.db import models
from django.contrib.auth.models import User


class Message(models.Model):
    destination_number = models.CharField(max_length=11)
    message_content = models.TextField()
    message_check = models.BooleanField(default=False)
    which_user = models.ForeignKey(User)
