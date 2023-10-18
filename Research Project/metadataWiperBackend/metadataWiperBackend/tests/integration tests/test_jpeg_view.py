from django.test import Client
from tests import test_data as test_data
import unittest
import requests


class TestJpegMetadataWiper(unittest.TestCase):
    def test_post_returns_200_and_metadata_wiped_file(self):
        client = Client()

        files = {'image': open(test_data.TEST_IMAGE_1, 'rb')}
        api = test_data.BASE_URL + test_data.API_JPEG_POST
        print (api)
        response = requests.post(api, files=files)

        assert response is not None
        print (response)
        assert response.status_code == 200


if __name__ == '__main__':
    unittest.main()


# http://localhost:8000/api/jpeg/
# http://localhost:8000/api/jpeg/