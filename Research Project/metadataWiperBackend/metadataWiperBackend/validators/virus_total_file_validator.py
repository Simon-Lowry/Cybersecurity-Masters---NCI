import hashlib
import requests
import metadataWiperBackend.properties as properties
import os


class VirusTotalFileValidator:
    @staticmethod
    def is_file_clean(filename:str):
        print("Virus total check on file underway....")
        file_hash = hashlib.sha256(open(properties.FILE_DIRECTORY + filename, 'rb').read()).hexdigest()
        print("File Sha256 Hash: " + file_hash)

        headers = {"x-apikey": properties.VT_API_KEY}
        response = requests.get(properties.VT_API + file_hash, headers=headers)

        print(response)
        if (response.status_code == 404):
            print("File not found by virus total. Proceeding to metadata wiping.")
            return

        # print json content
        response_json = response.json()
        print("Total malicious votes:")
        print(response_json['data']['attributes']['total_votes']['malicious'])
        malicious_votes = int(response_json['data']['attributes']['total_votes']['malicious'])
        if (malicious_votes > 4):
            print("Malicious file! File flagged by virus total, deleting file.")
            os.remove(properties.FILE_DIRECTORY + filename)
            #TODO: throw exception here!
        else:
            print("File deemed safe by virus total. Proceed to metadata wiping.")





