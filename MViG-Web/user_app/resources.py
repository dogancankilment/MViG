from tastypie.resources import ModelResource
from tastypie.constants import ALL
from .models import User


class MyModelResource(ModelResource):
    class Meta:
        queryset = User.objects.all()
        resource_name = 'user'
        filtering = {"email": ALL}
