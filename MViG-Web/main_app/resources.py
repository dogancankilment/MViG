from tastypie.resources import ModelResource
from .models import Message
from django.db import models

import tastypie


class MessageResource(ModelResource):
    class Meta:
        queryset = Message.objects.all()
        resource_name = 'message'
