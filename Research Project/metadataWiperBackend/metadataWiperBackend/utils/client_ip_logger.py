from ipware import get_client_ip
import logging

class ClientIPLogger():
    @staticmethod
    def log_ip(request):
        __logger = logging.getLogger('django')

        ip, is_routable = get_client_ip(request)
        if ip is None:
            # Unable to get the client's IP address
            __logger.info("Unable to get the client's IP address.")
        else:
            __logger.info("Client's IP address: "  + ip)
