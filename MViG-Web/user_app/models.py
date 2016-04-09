from django.contrib.auth.models import User
from django.db import models


class UserProfile(models.Model):
    user = models.OneToOneField(User, related_name="created_by_user")
    is_verified = models.BooleanField(default=False)

    def __unicode__(self):
        return self.user
