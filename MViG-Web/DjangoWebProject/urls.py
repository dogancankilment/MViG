from datetime import datetime
from django.conf.urls import patterns, url, include
from django.contrib import admin
from tastypie.api import Api
from user_app.resources import UserResource
from main_app.resources import MessageResource, ShowMessagesAndroid


user_resource = UserResource()
message_resource = MessageResource()
selected = ShowMessagesAndroid()

admin.autodiscover()


urlpatterns = patterns('',
                       url(r'^$',
                           'main_app.views.home',
                           name='home'),

                       url(r'^message/',
                           include('main_app.urls')),

                       url(r'^user/',
                           include('user_app.urls')),

                       url(r'^admin/',
                           include(admin.site.urls)),

                       url(r'^user-api/',
                           include(user_resource.urls)),

                       url(r'^message-api/',
                           include(message_resource.urls)),

                       url(r'^select-api/',
                           include(selected.urls)),

                       )
