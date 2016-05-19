from django.conf.urls import patterns, url


urlpatterns = patterns('',
                       url(r'^rss',
                           'main_app.views.rss', name='rss'),

                       url(r'^send_message$',
                           'main_app.views.send_message', name='send_message'),

                       url(r'^success',
                           'main_app.views.success_page', name='success'),

                       url(r'^android',
                           'main_app.views.android_send_message', name='android_send_message'),
                       )
