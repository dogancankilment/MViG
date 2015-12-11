from django.shortcuts import render, redirect, render_to_response, HttpResponse
from django.http import HttpRequest
from django.template import RequestContext
from datetime import datetime
from .models import Post
from .forms import MessageForm
from django.core.context_processors import csrf
from django.core.urlresolvers import reverse


def home(request):
    c = {"request": request}
    c.update(csrf(request))

    return render_to_response("app/index.html", c)


def test(request):
    post_list = Post.objects.all()
    return render_to_response("app/test.html",
                              {"posts": post_list})


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