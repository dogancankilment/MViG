from django.shortcuts import render, redirect, render_to_response, HttpResponse
from .models import Message
from .forms import MessageForm
from django.core.context_processors import csrf
from django.core import serializers
from utils.ftp_fileupload import ftp_upload


def rss(request):
    messages = Message.objects.all()
    data = {"messages": messages}

    return render_to_response("app/rss.html", data)


def send_message(request):
    if request.method == 'POST':
        form = MessageForm(request.POST, request.FILES)

        if form.is_valid():
            form.save(commit=True)
            message_serializer(request)
            ftp_upload()

            return success_page(request)

    else:
        form = MessageForm()

    c = {"form": form, "request": request}
    c.update(csrf(request))

    return render_to_response("app/sendMessage.html", c)


def message_serializer(request):
    clean_data = serializers.serialize('json', Message.objects.all(),
                                       fields=('destination_number',
                                               'message_content',
                                               'message_check'))

    upload_data = open("templates/app/index.html", "w")
    upload_data.write(clean_data)

    return render_to_response("app/index.html")


def home(request):
    return render_to_response("app/home.html",
                              {"request": request})


def success_page(request):
    return render_to_response("app/success.html")