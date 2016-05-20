from django.conf.urls import patterns, url


urlpatterns = patterns('',

                       url(r'^send_message$',
                           'main_app.views.send_message',
                           name='send_message'),

                       url(r'^success',
                           'main_app.views.success_page',
                           name='success'),

                       url(r'^android',
                           'main_app.views.android_send_message',
                           name='android_send_message'),

                       url(r'^show_messages',
                           'main_app.views.show_messages',
                           name='show_messages'),

                       url(r'^test_message',
                           'main_app.views.test_db_messages',
                           name='test_db_messages'),
                       )
