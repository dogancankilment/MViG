from django import forms
from .models import Message

import datetime


class MessageForm(forms.ModelForm):
    class Meta:
        model = Message
        fields = ('destination_number',
                  'message_content',
                  'image',)

    def save(self, commit=True):
        message = Message()
        message.destination_number = self.cleaned_data.get('destination_number')
        message.message_content = self.cleaned_data.get('message_content')
        message.created_at = datetime.datetime.now()
        message.image = self.cleaned_data.get('image')

        message.save()
