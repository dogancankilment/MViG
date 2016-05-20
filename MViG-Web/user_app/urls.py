from django.conf.urls import patterns, url


urlpatterns = patterns('',
                       url(r'^signup$',
                           'user_app.views.signup',
                           name='signup'),

                       url(r'^login',
                           'user_app.views.mvig_login',
                           name='mvig_login'),

                       url(r'^logout$',
                           'user_app.views.logout',
                           name='logout'),

                       url(r'^activation/(?P<token_id>.*)',
                           'user_app.views.activation',
                           name='activation'),

                       url(r'^android/',
                           'user_app.views.android',
                           name='android'),

                       url(r'^andro/login',
                           'user_app.views.android_login',
                           name='android_login'),

                       url(r'^test_user',
                           'user_app.views.test_db_users',
                           name='test_db_users'),
                       )
