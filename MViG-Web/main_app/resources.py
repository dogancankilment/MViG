from tastypie.resources import ModelResource, Resource
from tastypie.constants import ALL
from .models import Message
from user_app.models import User


class MessageResource(ModelResource):
    class Meta:
        queryset = Message.objects.all()
        resource_name = 'message'


class ShowMessagesAndroid(ModelResource):
    class Meta:
        queryset_user = User.objects.all()
        filtering = {'email': ALL}

        user = queryset_user[0]
        queryset_message = Message.objects.filter(which_user=user)
        resource_name = 'selected_user'