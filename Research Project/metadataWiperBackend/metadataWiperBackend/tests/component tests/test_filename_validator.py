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


    def test_validate_with_invalid_file_lengths_throw_exception(self):
        method_name = "test_validate_with_invalid_file_lengths_throw_exception"
        print("Running test: " + method_name)

        with self.assertRaises(ValueError) as cm:
            Filename_Validator.validate("", 100, ".jpg")
        self.assertEqual(str(cm.exception), 'Error: file must contain filetype and be no longer than 30 characters')

        with self.assertRaises(ValueError) as cm:
            Filename_Validator.validate(".jp", 100, ".jpg")
        self.assertEqual(str(cm.exception), 'Error: file must contain filetype and be no longer than 30 characters')

        with self.assertRaises(ValueError) as cm:
            Filename_Validator.validate(
                "wooooooooooooooiiiiiiiiiiiiiiiiiiiiiiijjjjjjoooooooooooooooooooo.jpg",
                100, ".jpg")
        self.assertEqual(str(cm.exception), 'Error: file must contain filetype and be no longer than 30 characters')

        print("Test complete for: " + method_name)

    def test_validate_with_invalid_file_size_throw_exception(self):
        method_name="test_validate_with_invalid_file_size_throw_exception"
        print("Running test: " + method_name)
        with self.assertRaises(ValueError) as cm:
            Filename_Validator.validate("somefile.jpg", 0, ".jpg")
        self.assertEqual(str(cm.exception), 'Error: file size rejected')

        with self.assertRaises(ValueError) as cm:
            Filename_Validator.validate("somefile.jpg", -1, ".jpg")
        self.assertEqual(str(cm.exception), 'Error: file size rejected')

        with self.assertRaises(ValueError) as cm:
            Filename_Validator.validate("somefile.jpg", 20000001, ".jpg")
        self.assertEqual(str(cm.exception), 'Error: file size rejected')

        with self.assertRaises(ValueError) as cm:
            Filename_Validator.validate("somefile.jpg", 20000002, ".jpg")
        self.assertEqual(str(cm.exception), 'Error: file size rejected')
        print("Test complete for: " + method_name)


    def test_validate_with_multiple_file_extensions(self):
        method_name = "test_validate_with_multiple_fileextensions"
        print("Running test: " + method_name)

        with self.assertRaises(ValueError) as cm:
            Filename_Validator.validate("somefile.jpg.exe", 20, ".jpg")
        self.assertEqual(str(cm.exception), 'Error: filename must only contain one dot/period')

        with self.assertRaises(ValueError) as cm:
            Filename_Validator.validate("somefile.jpg.png", 5, ".jpg")
        self.assertEqual(str(cm.exception), 'Error: filename must only contain one dot/period')

        with self.assertRaises(ValueError) as cm:
            Filename_Validator.validate("somefile..jpg", 5, ".jpg")
        self.assertEqual(str(cm.exception), 'Error: filename must only contain one dot/period')

        with self.assertRaises(ValueError) as cm:
            Filename_Validator.validate("somefile.exe.jpg", 5, ".jpg")
        self.assertEqual(str(cm.exception), 'Error: filename must only contain one dot/period')

        with self.assertRaises(ValueError) as cm:
            Filename_Validator.validate("somefile.file.jpg", 5, ".jpg")
        self.assertEqual(str(cm.exception), 'Error: filename must only contain one dot/period')
        print("Test complete for: " + method_name)

if __name__ == '__main__':
    unittest.main()