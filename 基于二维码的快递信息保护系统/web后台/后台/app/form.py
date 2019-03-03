#!/usr/bin/env python 
# -*- coding:utf-8 -*-
from django import forms

class CargolistForm(forms.Form):
    sender = forms.CharField(max_length=20)  # 用户名唯一
    receiver = forms.CharField(max_length=20)
    phone = forms.IntegerField()
    address = forms.CharField(max_length=50)
    cargoname = forms.CharField(max_length=20)
    postmanname = forms.CharField(max_length=20)
    cargonum = forms.CharField(max_length=40)
    #pngname = forms.CharField(max_length=30)