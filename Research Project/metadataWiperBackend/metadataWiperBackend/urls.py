"""
URL configuration for metadataWiperBackend project.

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/4.2/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from metadataWiperBackend.views.jpeg_view import JPEGView
from metadataWiperBackend.views.docx_view import DOCXView
from metadataWiperBackend.views.pdf_view import PDFView
from metadataWiperBackend.views.excel_view import XLSXView

from django.urls import path
from django.contrib import admin

urlpatterns = [
    path('admin/', admin.site.urls),
    path('api/jpeg/', JPEGView.as_view()),
    path('api/pdf/', PDFView.as_view()),
    path('api/docx/', DOCXView.as_view()),
    path('api/xlsx/', XLSXView.as_view()),
]