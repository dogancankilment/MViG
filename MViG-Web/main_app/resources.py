from tastypie.resources import ModelResource
from tastypie.constants import ALL
from .models import Message


class MessageResource(ModelResource):
    class Meta:
        queryset = Message.objects.all()
        resource_name = 'message'
