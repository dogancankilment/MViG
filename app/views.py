from django.shortcuts import render, redirect, render_to_response, HttpResponse
from django.template.response import TemplateResponse
from django.http import HttpRequest
from django.template import RequestContext
from datetime import datetime
from .models import Message
from .forms import MessageForm
from django.core.context_processors import csrf
from django.core.urlresolvers import reverse
from django.template.loader import render_to_string
from django.core import serializers


def rss(request):
    messages = Message.objects.all()

    data = {"messages": messages}

    return render_to_response("app/test.html", data)
    # data = serializers.serialize("xml", Message.objects.all())
    # return TemplateResponse(request, 'app/test.html', {'data': data})


def home(request):
    if request.method == 'POST':
        form = MessageForm(request.POST)

        if form.is_valid():
            form.save(commit=True)

            return success_page(request)

    else:
        form = MessageForm()

    c = {"form": form, "request": request}
    c.update(csrf(request))

    return render_to_response("app/index.html", c)


def success_page(request):
    return render_to_response("app/success.html")