# -*- coding: utf-8 -*-
# Generated by Django 1.11.8 on 2019-01-16 13:43
from __future__ import unicode_literals

from django.db import migrations, models
import django.utils.timezone


class Migration(migrations.Migration):

    dependencies = [
        ('app', '0001_initial'),
    ]

    operations = [
        migrations.RenameField(
            model_name='cargolist',
            old_name='receivername',
            new_name='postmanname',
        ),
        migrations.AddField(
            model_name='cargolist',
            name='cargonum',
            field=models.CharField(default=django.utils.timezone.now, max_length=40),
            preserve_default=False,
        ),
    ]
