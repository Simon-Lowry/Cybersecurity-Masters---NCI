from datetime import datetime


class time_calculator:
    def __init__(self, time_start = None):
        self.time_start = datetime.now()

    def set_pre_metadata_timer(self):
        time_start = datetime.now()

    def get_time_taken_for_wiping_completion(self):
        time_end = datetime.now()
        return str(time_end - self.time_start)