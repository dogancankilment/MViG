from django import forms
from .models import Post


class MessageForm(forms.ModelForm):
    class Meta:
        model = Post

    def save(self, commit=True):
        post = Post()
        post.message_receiver = self.cleaned_data.get('message_receiver')
        post.message_content = self.cleaned_data.get('message_content')

        post.save()
