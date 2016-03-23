from django.db import models

import uuid


class Message(models.Model):
    destination_number = models.CharField(max_length=11)
    message_content = models.TextField()