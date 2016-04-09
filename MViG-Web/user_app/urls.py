from django.conf.urls import patterns, url


urlpatterns = patterns('',
                       url(r'^signup$',
                           'user_app.views.signup',
                           name='signup'),

                       url(r'^login$',
                           'user_app.views.login',
                           name='login'),

                       url(r'^logout$',
                           'user_app.views.logout',
                           name='logout'),
                       )
