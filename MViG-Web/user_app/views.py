from django.shortcuts import render, redirect, render_to_response, get_object_or_404
from django.http import HttpResponse, HttpResponseRedirect
from django.core.urlresolvers import reverse
from django.contrib.auth import authenticate
from django.contrib import auth
from django.contrib import messages
from django.utils.translation import ugettext as _

from forms import UserCreateForm, LoginForm
from utils.token_generator import tokens_email, tokens_expire_date
from utils.mail_sender import mail_sender
from .models import User

import datetime


def signup(request, template_name="authentication/signup.html"):
    form = UserCreateForm(request.POST or None)

    if request.POST:

        if form.is_valid():
            form.save()

            return HttpResponseRedirect(reverse('home'))

    return render(request,
                  template_name,
                  {'form': form})


def mvig_login(request):
    form = LoginForm(request.POST or None)
    import pdb
    if form.is_valid():
        user = authenticate(username=form.cleaned_data['username'],
                            password=form.cleaned_data['password'])

        if user:
            if user.is_active:
                pdb.set_trace()
                auth.login(request, user)
                pdb.set_trace()
                # Redirect to a success page
                return HttpResponseRedirect(reverse('home'))

            else:
                messages.error(request,
                               (_('Lutfen Hesabinizi aktif ediniz.')))

        else:
            messages.error(request,
                           (_('Boyle bir kullanici sistemde kayitli degil')))

    return render(request, 'authentication/login.html',
                  {'login_form': form})


def activation(request, token_id, template_name="authentication/activation.html"):
    if token_id:
        try:
            email_in_token = tokens_email(token_id)
        except TypeError:
            messages.error(request,
                           (_('Hatali aktivasyon kodu')))
            return render(request,
                          template_name)

        result = User.objects.filter(email=email_in_token).exists()

        if result:
            expire_date_in_token = tokens_expire_date(token_id)

            if str(expire_date_in_token) > str(datetime.datetime.today()):
                user = User.objects.get(email=email_in_token)
                user.is_active = True
                user.save()

                messages.success(request,
                                 (_('Hesabiniz aktif edilmistir. Lutfen giris yapiniz.')))

                return render(request,
                              template_name)

            else:
                mail_sender(email_in_token)
                messages.success(request, (_('Eski aktivasyon mailinin suresi bitmistir,'
                                             'yeni bir email yolladik,'
                                             'lutfen posta kutunuzu ziyaret ediniz.')))

        else:
            messages.success(request, (_('Eslesen email bulunamadi.')))

    else:
        messages.success(request, (_('Boyle bir token yoktur')))

    return render(request,
                  template_name)


def logout(request):
    auth.logout(request)
    return redirect(reverse('home'))
