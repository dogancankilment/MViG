from django import forms
from .models import User
from django.contrib.auth.forms import UserCreationForm
from django.forms.models import ModelForm
from utils.mail_sender import mail_sender


class UserCreateForm(UserCreationForm):
    email = forms.EmailField(required=True)

    class Meta:
        model = User
        fields = ("username", "email", "password1", "password2")

    def clean_email(self):
        email = self.cleaned_data.get('email')
        username = self.cleaned_data.get('username')

        if email and User.objects.filter(email=email).exclude(username=username).count():
            raise forms.ValidationError(u'Email addresses must be unique.')

        return email

    def save(self, commit=True):
        user = super(UserCreateForm, self).save(commit=False)
        user.email = self.cleaned_data["email"]

        user.is_active = True

        if commit:
            user.save()

            # mail_sender(user.email)
        return user


class LoginForm(forms.Form):
    username = forms.CharField(required=True, label="email")
    password = forms.CharField(widget=forms.PasswordInput, required=True)

    def clean_form(self):
        return self.cleaned_data