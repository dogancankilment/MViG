# token
import base64
import datetime


def activation_key_generator(email):
    expire_date = datetime.datetime.today() + datetime.timedelta(3)
    activation_key = base64.b64encode(
        str(expire_date)) + base64.b64encode(
        str(email)).split('=')[0]

    return activation_key


def tokens_email(token_id):
    if "=" in token_id:
        # if len(str(token_id)) == 70:
        # this line controlled in views
        # with try except block
        email = token_id.split('=')[1] + '=='
        email = base64.b64decode(email)

        return email


def tokens_expire_date(token_id):
    expire_date_in = token_id.split('=')[0] + '='
    expire_date = base64.b64decode(expire_date_in)

    return expire_date