from datetime import datetime
from django.conf.urls import patterns, url, include
from django.contrib import admin
from tastypie.api import Api
from user_app.resources import MyModelResource

user_resource = MyModelResource()

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

                       url(r'^api/',
                           include(user_resource.urls)),
                       )
