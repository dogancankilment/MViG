from django.shortcuts import render, redirect, render_to_response, get_object_or_404
from django.http import HttpResponse, HttpResponseRedirect
from django.core.urlresolvers import reverse

from forms import UserCreateForm


def signup(request, template_name="authentication/signup.html"):
    form = UserCreateForm(request.POST or None)

    if request.POST:

        if form.is_valid():
            form.save()

            return HttpResponseRedirect('app/index.html')

    return render(request,
                  template_name,
                  {'form': form})
