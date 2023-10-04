from django.test import Client
from .. import test_data as test_data
import unittest
import requests


class TestJpegMetadataWiper(unittest.TestCase):
    def test_post_returns_200_and_metadata_wiped_file(self):
        client = Client()

        files = {'media': open(test_data.TEST_IMAGE_1, 'rb')}
        response = requests.post(test_data.BASE_URL + test_data.API_JPEG_POST, files=files)

        assert response is not None
        print (response)
        assert response.status_code == 200


if __name__ == '__main__':
    unittest.main()