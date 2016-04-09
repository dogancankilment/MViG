from django.shortcuts import render, redirect, render_to_response, get_object_or_404
from django.http import HttpResponse, HttpResponseRedirect
from django.core.urlresolvers import reverse
from django.contrib.auth import authenticate
from django.contrib import auth
from django.contrib import messages
from django.utils.translation import ugettext as _

from forms import UserCreateForm, LoginForm


def signup(request, template_name="authentication/signup.html"):
    form = UserCreateForm(request.POST or None)

    if request.POST:

        if form.is_valid():
            form.save()

            return HttpResponseRedirect('app/index.html')

    return render(request,
                  template_name,
                  {'form': form})


def login(request):
    form = LoginForm(request.POST or None)

    if form.is_valid():
        user = authenticate(username=form.cleaned_data['username'],
                            password=form.cleaned_data['password'])

        if user:
            if user.is_active:
                auth.login(request, user)

                # Redirect to a success page
                return HttpResponseRedirect("app/home.html")

            # yakalanacak hata icin mail aktivasyonu eklenecek
            else:
                messages.error(request,
                               (_('Lutfen Hesabinizi aktif ediniz.')))

        else:
            messages.error(request,
                           (_('Boyle bir kullanici sistemde kayitli degil')))

    return render(request, 'authentication/login.html',
                  {'login_form': form})


def logout(request):
    auth.logout(request)
    return redirect("app/home.html")
