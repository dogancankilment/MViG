from django.shortcuts import render, redirect, render_to_response, HttpResponse
from django.http import HttpRequest
from django.template import RequestContext
from datetime import datetime
from .models import Post
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


def contact(request):
    """Renders the contact page."""
    assert isinstance(request, HttpRequest)
    return render(
        request,
        'app/contact.html',
        context_instance = RequestContext(request,
        {
            'title':'Contact',
            'message':'Your contact page.',
            'year':datetime.now().year,
        })
    )


def about(request):
    """Renders the about page."""
    assert isinstance(request, HttpRequest)
    return render(
        request,
        'app/about.html',
        context_instance = RequestContext(request,
        {
            'title':'About',
            'message':'Your application description page.',
            'year':datetime.now().year,
        })
    )


# def hello(request):
#     return HttpResponse("Hello world")