from django.shortcuts import render, redirect, render_to_response, HttpResponse
from .models import Message
from .forms import MessageForm
from django.core.context_processors import csrf
from django.core import serializers
from utils.ftp_fileupload import ftp_upload
from django.contrib.auth.models import User
from .models import Message


def android_send_message(request):
    destination_number = request.GET['number']
    message_content = request.GET['content']

    new_message = Message(destination_number=destination_number, message_content=message_content)
    new_message.save()

    return HttpResponse("basarili")


def rss(request):
    messages = Message.objects.all()
    users = User.objects.all()
    data = {"messages": messages, "users":users}

    return render_to_response("app/rss.html", data)


def send_message(request):
    if request.method == 'POST':
        form = MessageForm(request.POST, request.FILES)

        if form.is_valid():
            form.save(commit=True)

            return success_page(request)

    else:
        form = MessageForm()

    c = {"form": form, "request": request}
    c.update(csrf(request))

    return render_to_response("app/sendMessage.html", c)


def home(request):
    return render_to_response("app/home.html",
                              {"request": request})


def success_page(request):
    return render_to_response("app/success.html")