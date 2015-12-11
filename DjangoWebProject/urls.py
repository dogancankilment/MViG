from datetime import datetime
from django.conf.urls import patterns, url

# Uncomment the next lines to enable the admin:
from django.conf.urls import include
from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    url(r'^$', 'app.views.home', name='home'),
    url(r'^test', 'app.views.test', name='test'),
    url(r'^sendmessage', 'app.views.new_message', name='newMessage'),
    url(r'^success', 'app.views.success_page', name='success'),
    url(r'^admin/', include(admin.site.urls)),

    # url(r'^hello/$', 'app.views.hello', name='hello'),

)
