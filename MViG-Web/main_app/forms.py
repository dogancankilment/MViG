from django import forms
from .models import Message


class MessageForm(forms.ModelForm):
    class Meta:
        model = Message
        fields = ('destination_number',
                  'message_content',)

        exclude = ['message_check', 'which_user']

    def save(self, user):
        message = Message()
        message.destination_number = self.cleaned_data.get('destination_number')
        message.message_content = self.cleaned_data.get('message_content')
        message.message_check = False
        message.which_user = user

        message.save()
