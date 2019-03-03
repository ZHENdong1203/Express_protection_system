"""Second URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.11/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.conf.urls import url, include
    2. Add a URL to urlpatterns:  url(r'^blog/', include('blog.urls'))
"""
from django.conf.urls import url
from django.contrib import admin

from app import views

urlpatterns = [
    url(r'^admin/', admin.site.urls),

    url(r'^postmanregister/$',views.postmanregister),#快递员注册
    url(r'^postmanlogin/$',views.postmanlogin),#快递员登陆
    url(r'^adminlogin/$',views.adminlogin),#管理员登陆
    url(r'^addcargo/$',views.addcargo),#添加快递
    url(r'^querycargo/$',views.querycargo),#擦好看快递信息
    url(r'^addadminuser/$',views.addadminuser),#添加管理员

    url(r'^home/$',views.home),
    url(r'^add/$',views.test)
]
