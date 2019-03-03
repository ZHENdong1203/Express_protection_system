from django.contrib import admin
from .models import *
# Register your models here.
@admin.register(Admin)
class AdminS (admin.ModelAdmin):#管理员表
    list_display = ('pk','username','password')

@admin.register(Cargolist)
class CargolistAdmin(admin.ModelAdmin):#快递表
    list_display = ('pk','sender','receiver','phone','address','cargoname','postmanname','cargonum')

@admin.register(Usertable)
class UsertableAdmin(admin.ModelAdmin):#快递员表
    list_display = ('pk','name','password','phonenum')