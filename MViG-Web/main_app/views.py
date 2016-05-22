from django.shortcuts import render, redirect, render_to_response, HttpResponse
from .models import Message
from .forms import MessageForm
from django.core.context_processors import csrf
from django.core import serializers
from django.contrib.auth.models import User
from .models import Message
from django.contrib.auth.decorators import login_required
from django.core import serializers


def get_messages(request):
    email = request.GET['email']
    current_user = User.objects.get(email=email)
    current_id = current_user.id

    query = Message.objects.filter(which_user=current_id)
    result = serializers.serialize("json", query)

    return HttpResponse(result)


def current_user_is_active(request):
    email = request.GET['email']
    c_user = User.objects.get(email=email)

    return HttpResponse(str(c_user.id) + "-" + str(c_user.is_active))


def android_message_check(request):
    m_id = request.GET['id']
    m = Message.objects.get(id=m_id)
    m.message_check = True
    m.save()

    return HttpResponse("Basarili")


def android_send_message(request):
    destination_number = request.GET['number']
    message_content = request.GET['content']
    email = request.GET['email']

    message_content = message_content.replace("-", " ")

    user = User.objects.get(email=email)
    new_message = Message(destination_number=destination_number, message_content=message_content, which_user=user)
    new_message.save()

    return HttpResponse("basarili")


@login_required(login_url='/user/login/')
def send_message(request):
    if request.method == 'POST':
        form = MessageForm(request.POST, request.FILES)

        if form.is_valid():
            form.save(request.user)

            return success_page(request)

    else:
        form = MessageForm()

    c = {"form": form, "request": request}
    c.update(csrf(request))

    return render_to_response("app/sendMessage.html", c)


def show_messages(request):
    messages = Message.objects.filter(which_user=request.user)

    return render_to_response("app/show_messages.html",
                              {"request": request,
                               "messages": messages})


def show_messages_android(request):
    email = request.GET['email']
    user = User.objects.get(email=email)


    return HttpResponse("basarili")


def test_db_messages(request):
    messages = Message.objects.all()

    return render_to_response("app/test_db_messages.html",
                              {'messages': messages,
                               'request': request})


def home(request):
    return render_to_response("app/home.html",
                              {"request": request})


def success_page(request):
    return render_to_response("app/success.html")