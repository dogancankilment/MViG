from datetime import datetime
from django.conf.urls import patterns, url, include
from django.contrib import admin
admin.autodiscover()


urlpatterns = patterns('',
                       url(r'^$',
                           'main_app.views.home', name='home'),

                       url(r'^message/',
                           include('main_app.urls')),

                       url(r'^user/',
                           include('user_app.urls')),

                       url(r'^admin/',
                           include(admin.site.urls)),
                       )
