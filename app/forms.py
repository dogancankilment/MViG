from django import forms
from .models import Message


class MessageForm(forms.ModelForm):
    class Meta:
        model = Message
        fields = ('destination_number',
                  'message_content',)

        exclude = ['message_check']

    def save(self, commit=True):
        message = Message()
        message.destination_number = self.cleaned_data.get('destination_number')
        message.message_content = self.cleaned_data.get('message_content')
        message.message_check = False

        message.save()
