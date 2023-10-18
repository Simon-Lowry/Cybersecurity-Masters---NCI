from PIL import Image
from PIL.ExifTags import TAGS
from exif import Image as ExifImage
from metadataWiperBackend.utils.file_operations import get_file_size
from metadataWiperBackend.utils.time_calculator import time_calculator
import metadataWiperBackend.properties as properties
import logging


class JpegMetadataWiper:
    __logger = logging.getLogger('django')
    __class_name = "JpegMetadataWiper"
    def perform_wipe_metadata(self, filename: str):
        __method_name = "perform_wipe_metadata"
        self.__logger.info("Entered method: " + __method_name)

        self.read_jpeg_metadata(filename)
        file_size_before_metadata_wipe = get_file_size(properties.FILE_DIRECTORY + filename)

        time = time_calculator()
        self.wipe_jpeg_metadata(filename)

        time_taken_to_wipe_metadata = time.get_time_taken_for_wiping_completion()
        self.read_jpeg_metadata(filename)
        file_size_after_metadata_wipe = get_file_size(properties.FILE_DIRECTORY + filename)
        self.__logger.info("File size in bytes prior to metadata wiping: " + str(file_size_before_metadata_wipe))
        self.__logger.info ("File size after in bytes: " + str(file_size_after_metadata_wipe))
        file_size_change = file_size_after_metadata_wipe - file_size_before_metadata_wipe
        self.__logger.info("Difference in file size after wiping metadata: " + str(file_size_change) + "kb")
        self.__logger.info("Time taken to wipe metadata: " + time_taken_to_wipe_metadata )

    def read_jpeg_metadata(self, filename: str):
        self.__logger.info("Performing read of jpeg metadata")
        file_path = properties.FILE_DIRECTORY + filename
        jpeg_file = Image.open(properties.FILE_DIRECTORY + filename)
        # Print the attributes of the image
        exifdata = jpeg_file._getexif()

        if exifdata is None:
            raise Exception("Unable to read file metadata, no values returned.")

        # iterating over all EXIF data fields
        for tag_id in exifdata:
            # get the tag name, instead of human unreadable tag id
            tag = TAGS.get(tag_id, tag_id)
            data = exifdata.get(tag_id)
            # decode bytes
            if isinstance(data, bytes):
                data = data.decode()
            self.__logger.info(f"{tag:25}: {data}")

    def wipe_jpeg_metadata(self, filename: str):
        image_path = properties.FILE_DIRECTORY + filename
        with open(image_path, "rb") as input_file:
            exif_img = ExifImage(input_file)

        exif_img.artist = ""
        exif_img.copyright = ""
        exif_img.make = ""
        exif_img.model = ""
        exif_img.software = ""
        exif_img.gps_latitude = (0.0, 0.0, 0.0)
        exif_img.gps_longitude = (0.0, 0.0, 0.0)
        exif_img.gps_altitude = 0.0
        exif_img.gps_latitude_ref = ''
        exif_img.gps_longitude_ref = ''
        #TODO: change datetime_original to a fake date

        with open(image_path, "wb") as ofile:
            ofile.write(exif_img.get_file())
