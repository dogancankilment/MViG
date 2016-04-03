from django.http import HttpResponse
from django.core.mail import EmailMultiAlternatives


def mail_sender(message_content, message_number):
        subject = message_number
        text_content = message_content
        from_email = 'surveydck@gmail.com'
        to = 'dogancankilment.2016@gmail.com'
        msg = EmailMultiAlternatives(subject,
                                     text_content,
                                     from_email,
                                     [to])

        msg.send()

        return HttpResponse("Your email has been sent")
