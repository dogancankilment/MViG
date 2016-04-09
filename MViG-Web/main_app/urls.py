from django.conf.urls import patterns, url


urlpatterns = patterns('',
                       url(r'^rss',
                           'main_app.views.rss', name='rss'),

                       url(r'^send_message$',
                           'main_app.views.send_message', name='send_message'),

                       url(r'^success',
                           'main_app.views.success_page', name='success'),

                       url(r'^message_serializer',
                           'main_app.views.message_serializer', name='message_serializer'),
                       )
