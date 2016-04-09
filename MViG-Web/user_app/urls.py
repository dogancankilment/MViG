from django.conf.urls import patterns, url


urlpatterns = patterns('',
                       url(r'^signup$',
                           'user_app.views.signup',
                           name='signup'),
                       )