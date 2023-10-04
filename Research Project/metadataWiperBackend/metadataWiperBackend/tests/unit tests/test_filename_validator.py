import unittest

from metadataWiperBackend.validators.filename_validator import Filename_Validator

class TestFilenameValidor(unittest.TestCase):
    def test_is_accepted_file_type_accepts_valid_file_types(self):
        method_name = "test_is_accepted_file_type_accepts_valid_file_types"
        print("Running test: " + method_name)

        validator = Filename_Validator()
        self.assertTrue(validator.is_accepted_file_type("someimage.jpg",".jpg"))
        self.assertTrue(validator.is_accepted_file_type("someexcel.xlsx", ".xlsx"))
        self.assertTrue(validator.is_accepted_file_type("somepdf.pdf", ".pdf"))
        self.assertTrue(validator.is_accepted_file_type("somedocx.docx", ".docx"))

        print("Test complete: " + method_name)

    def test_is_accepted_file_type_rejects_invalid_file_types(self):
        method_name = "test_is_accepted_file_type_rejects_invalid_file_types"
        print("Running test: " + method_name)

        validator = Filename_Validator()
        self.assertRaises(ValueError, validator.is_accepted_file_type, "someimage.jpeg", ".jpg")
        self.assertRaises(ValueError, validator.is_accepted_file_type, "someimage.png", ".jpeg")
        self.assertRaises(ValueError, validator.is_accepted_file_type, "someexcel.excel", ".xlsx")
        self.assertRaises(ValueError, validator.is_accepted_file_type, "somepdf._pdf", ".pdf")
        self.assertRaises(ValueError, validator.is_accepted_file_type, "somedocx.doc", ".docx")
        print("Test complete: " + method_name)




if __name__ == '__main__':
    unittest.main()