from django.conf.urls import patterns, url


urlpatterns = patterns('',
                       url(r'^signup$',
                           'user_app.views.signup',
                           name='signup'),

                       url(r'^login$',
                           'user_app.views.mvig_login',
                           name='mvig_login'),

                       url(r'^logout$',
                           'user_app.views.logout',
                           name='logout'),

                       url(r'^activation/(?P<token_id>.*)',
                           'user_app.views.activation',
                           name='activation'),
                       )
