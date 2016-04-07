from datetime import datetime
from django.conf.urls import patterns, url

# Uncomment the next lines to enable the admin:
from django.conf.urls import include
from django.contrib import admin
admin.autodiscover()


urlpatterns = patterns('',
                       url(r'^$', 'app.views.home', name='home'),
                       url(r'^rss', 'app.views.rss', name='rss'),
                       url(r'^send_message$', 'app.views.send_message', name='send_message'),
                       url(r'^success', 'app.views.success_page', name='success'),
                       url(r'^message_serializer', 'app.views.message_serializer', name='message_serializer'),
                       url(r'^admin/', include(admin.site.urls)),
                       )
