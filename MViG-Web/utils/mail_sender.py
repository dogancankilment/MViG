from django.core.mail import EmailMultiAlternatives
from django.template import Context
from django.template.loader import render_to_string
from django.http import HttpResponse
from django.utils.translation import ugettext as _

from .token_generator import activation_key_generator


def mail_sender(email):
        subject, from_email = 'hello',\
                              'surveydck@gmail.com'
        if email:
            to = email

            hash_key_example = activation_key_generator(to)
            transmitted_key = Context(
                {'hash_key': hash_key_example})

            text_content = render_to_string('authentication/email_content.html',
                                            transmitted_key)
            msg = EmailMultiAlternatives(subject,
                                         text_content,
                                         from_email,
                                         [to])
            msg.send()

            return HttpResponse(_("mailiniz gonderildi"))

        else:
            return HttpResponse(_("Gondereceginiz kisinin"
                                "email adresi belli olmalidir."))