from datetime import datetime
from django.conf.urls import patterns, url

# Uncomment the next lines to enable the admin:
from django.conf.urls import include
from django.contrib import admin
admin.autodiscover()


urlpatterns = patterns('',
                       url(r'^$', 'main_app.views.home', name='home'),
                       url(r'^rss', 'main_app.views.rss', name='rss'),
                       url(r'^send_message$', 'main_app.views.send_message', name='send_message'),
                       url(r'^success', 'main_app.views.success_page', name='success'),
                       url(r'^message_serializer', 'main_app.views.message_serializer', name='message_serializer'),
                       url(r'^admin/', include(admin.site.urls)),
                       )
