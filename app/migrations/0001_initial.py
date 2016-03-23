# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Message',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('destination_number', models.CharField(max_length=11)),
                ('message_content', models.TextField()),
            ],
            options={
            },
            bases=(models.Model,),
        ),
    ]
