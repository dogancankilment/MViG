from django.shortcuts import render, redirect, render_to_response, HttpResponse
from django.http import HttpRequest
from django.template import RequestContext
from datetime import datetime
from .models import Post
from .forms import MessageForm
from django.core.context_processors import csrf
from django.core.urlresolvers import reverse


def test(request):
    all_messages = Post.objects.all()

    return render_to_response("rss/rssfeed",
                              {"all_messages": all_messages})


def new_message(request):
    if request.method == 'POST':
        form = MessageForm(request.POST)

        if form.is_valid():
            form.save(commit=True)

            return success_page(request)

    else:
        form = MessageForm()

    c = {"form": form, "request": request}
    c.update(csrf(request))

    return render_to_response("app/addMessage.html",c)


def success_page(request):
    return render_to_response("app/success.html")