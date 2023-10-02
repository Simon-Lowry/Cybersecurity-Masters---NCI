import hashlib
import requests
import metadataWiperBackend.properties as properties
import os
import logging

class VirusTotalFileValidator:
    __logger = logging.getLogger('django')
    __class_name = "VirusTotalFileValidator"
    @staticmethod
    def is_file_clean(filename:str):
        __method_name = "is_file_clean"
        __logger = logging.getLogger('django')
        __logger.info("Virus total check on file underway....")
        file_hash = hashlib.sha256(open(properties.FILE_DIRECTORY + filename, 'rb').read()).hexdigest()
        __logger.info("File Sha256 Hash: " + file_hash)

        headers = {"x-apikey": properties.VT_API_KEY}
        response = requests.get(properties.VT_API + file_hash, headers=headers)

        __logger.info(response)
        if (response.status_code == 404):
            __logger.info("File not found by virus total. Proceeding to metadata wiping.")
            return

        # print json content
        response_json = response.json()
        __logger.info("Total malicious votes:")
        __logger.info(response_json['data']['attributes']['total_votes']['malicious'])
        malicious_votes = int(response_json['data']['attributes']['total_votes']['malicious'])
        if (malicious_votes > 4):
            __logger.error("Malicious file! File flagged by virus total, deleting file.")
            os.remove(properties.FILE_DIRECTORY + filename)
            #TODO: throw exception here!
        else:
            __logger.info("File deemed safe by virus total. Proceed to metadata wiping.")





